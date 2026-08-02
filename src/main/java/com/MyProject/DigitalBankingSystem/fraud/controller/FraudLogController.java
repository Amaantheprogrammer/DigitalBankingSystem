package com.MyProject.DigitalBankingSystem.fraud.controller;

import com.MyProject.DigitalBankingSystem.fraud.dto.FraudLogRequest;
import com.MyProject.DigitalBankingSystem.fraud.dto.FraudLogResponse;
import com.MyProject.DigitalBankingSystem.fraud.service.FraudLogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/fraud-logs")
@Slf4j
public class FraudLogController {

    private final FraudLogService fraudLogService;

    @GetMapping("/{fraudLogId}")
    public ResponseEntity<FraudLogResponse> getFraudLogById(@PathVariable Long fraudLogId) {
        log.info("Fetching fraud log with ID: {}", fraudLogId);
        return ResponseEntity.ok(fraudLogService.getFraudLogById(fraudLogId));
    }

    @GetMapping
    public ResponseEntity<Page<FraudLogResponse>> getAllFraudLogs(@PageableDefault(size = 10) Pageable pageable) {
        log.info("Fetching all fraud logs");
        return ResponseEntity.ok(fraudLogService.getAllFraudLogs(pageable));
    }

    @PatchMapping("/{fraudLogId}/status")
    public ResponseEntity<FraudLogResponse> updateFraudStatus(
            @PathVariable Long fraudLogId,
            @Valid @RequestBody FraudLogRequest request) {
        log.info("Updating fraudLog {} to status {}", fraudLogId, request.getFraudStatus());
        return ResponseEntity.ok(fraudLogService.updateFraudStatus(fraudLogId, request));
    }
}
