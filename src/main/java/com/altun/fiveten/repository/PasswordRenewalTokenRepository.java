package com.altun.fiveten.repository;

import com.altun.fiveten.model.ConfirmationToken;
import com.altun.fiveten.model.PasswordRenewalToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordRenewalTokenRepository extends JpaRepository<PasswordRenewalToken, Long> {
    PasswordRenewalToken findByRenewalToken(String renewalToken);
}
