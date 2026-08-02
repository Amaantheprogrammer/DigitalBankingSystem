package com.MyProject.DigitalBankingSystem.fraud.service;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.MyProject.DigitalBankingSystem.exception.AccessDeniedException;
import com.MyProject.DigitalBankingSystem.exception.DuplicateResourceException;
import com.MyProject.DigitalBankingSystem.exception.ResourceNotFoundException;
import com.MyProject.DigitalBankingSystem.fraud.dto.FraudLogRequest;
import com.MyProject.DigitalBankingSystem.fraud.dto.FraudLogResponse;
import com.MyProject.DigitalBankingSystem.fraud.entity.FraudLog;
import com.MyProject.DigitalBankingSystem.fraud.repository.FraudLogRepository;
import com.MyProject.DigitalBankingSystem.user.entity.Role;
import com.MyProject.DigitalBankingSystem.user.entity.User;
import com.MyProject.DigitalBankingSystem.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FraudLogService {

    private final FraudLogRepository fraudLogRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveFraudLog(FraudLog fraudLog) {
        fraudLogRepository.save(fraudLog);
    }

    @Transactional(readOnly = true)
    public FraudLogResponse getFraudLogById(Long fraudLogId) {
        FraudLog fraudLog = getFraudLogOrThrow(fraudLogId);
        return mapToResponse(fraudLog);
    }

    @Transactional(readOnly = true)
    public Page<FraudLogResponse> getAllFraudLogs(Pageable pageable) {
        Page<FraudLog> fraudLogs = fraudLogRepository.findAllByOrderByDetectedAtDesc(pageable);
        return fraudLogs.map(this::mapToResponse);
    }

    @Transactional
    public FraudLogResponse updateFraudStatus(Long fraudLogId, FraudLogRequest request) {
        User admin = getSecuredUserAndCheckIfAdmin();
        FraudLog fraudLog = getFraudLogOrThrow(fraudLogId);
        if (fraudLog.getStatus() == request.getFraudStatus()) {
            throw new DuplicateResourceException("Fraud log already has status: " + fraudLog.getStatus());
        }
        fraudLog.setStatus(request.getFraudStatus());
        fraudLog.setReviewedBy(admin);
        fraudLog.setReviewedAt(LocalDateTime.now());

        FraudLog savedFraudLog = fraudLogRepository.save(fraudLog);
        return mapToResponse(savedFraudLog);
    }

    // Private methods
    private FraudLogResponse mapToResponse(FraudLog fraudLog) {
        FraudLogResponse fraudLogResponse = modelMapper.map(fraudLog, FraudLogResponse.class);
        if (fraudLog.getReviewedBy() != null) {
            fraudLogResponse.setReviewedByEmail(fraudLog.getReviewedBy().getEmail());
        }
        return fraudLogResponse;
    }

    private User getSecuredUserAndCheckIfAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("User not authenticated");
        }
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email"));
        if (user.getRole() != Role.ROLE_ADMIN) {
            throw new AccessDeniedException("Only administrator can view fraud log operations");
        }
        return user;
    }

    private FraudLog getFraudLogOrThrow(Long fraudLogId) {
        return fraudLogRepository.findById(fraudLogId)
                .orElseThrow(() -> new ResourceNotFoundException("Fraud log not found with ID: " + fraudLogId));
    }
}
