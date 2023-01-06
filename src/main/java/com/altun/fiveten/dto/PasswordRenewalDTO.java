package com.altun.fiveten.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordRenewalDTO {
    private String newPassword;
    private String passwordConfirmation;
}
