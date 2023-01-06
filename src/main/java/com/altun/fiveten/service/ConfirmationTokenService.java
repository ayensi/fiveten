package com.altun.fiveten.service;

import com.altun.fiveten.model.ConfirmationToken;
import com.altun.fiveten.repository.ConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfirmationTokenService {
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    public ConfirmationToken findByConfirmationToken(String confirmationToken){
        return this.confirmationTokenRepository.findByConfirmationToken(confirmationToken);
    }

    public void save(ConfirmationToken token) {
        this.confirmationTokenRepository.save(token);
    }
}
