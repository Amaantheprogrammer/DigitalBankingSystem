package com.MyProject.DigitalBankingSystem.user.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.MyProject.DigitalBankingSystem.audit.entity.AuditableEntity;
import com.MyProject.DigitalBankingSystem.user.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse implements Serializable, AuditableEntity {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private LocalDateTime createdAt;
}
