package com.MyProject.DigitalBankingSystem.user.dto;

import com.MyProject.DigitalBankingSystem.account.entity.Account;
import com.MyProject.DigitalBankingSystem.user.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long userId;
    private String name;
    private String email;
    private Role role;
    private LocalDateTime createdAt;
}
