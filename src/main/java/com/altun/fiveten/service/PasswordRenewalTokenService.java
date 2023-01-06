package com.altun.fiveten.service;

import com.altun.fiveten.model.PasswordRenewalToken;
import com.altun.fiveten.repository.PasswordRenewalTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PasswordRenewalTokenService {
    @Autowired
    private PasswordRenewalTokenRepository tokenRepository;

    public PasswordRenewalToken findByRenewalToken(String passwordRenewalToken){
        return this.tokenRepository.findByRenewalToken(passwordRenewalToken);
    }

    public void save(PasswordRenewalToken passwordRenewalToken){
        this.tokenRepository.save(passwordRenewalToken);
    }
}
