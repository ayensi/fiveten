package com.altun.fiveten.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor

public class PasswordRenewalToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @Column(name="renewal_token")
    private String renewalToken;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public PasswordRenewalToken(User user) {
        this.user = user;
        createdDate = new Date();
        renewalToken = UUID.randomUUID().toString();
    }
}
