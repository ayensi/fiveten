package com.altun.fiveten.service;

import com.altun.fiveten.constants.MailConstants;
import com.altun.fiveten.model.ConfirmationToken;
import com.altun.fiveten.model.PasswordRenewalToken;
import com.altun.fiveten.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service

public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;


    @Async
    public void sendEmail(MimeMessage email) {
        javaMailSender.send(email);
    }

    public MimeMessage createConfirmationMail(User user, ConfirmationToken token){
        MimeMessage mailMessage = null;
        try{
            mailMessage = javaMailSender.createMimeMessage();
            mailMessage.setSubject("Account Activation Needed!");

            MimeMessageHelper helper;
            helper = new MimeMessageHelper(mailMessage, true);
            helper.setFrom(MailConstants.mailFrom);
            helper.setTo(user.getEmail());
            helper.setText("To confirm your account, please click here : "
                    +"http://localhost:8080/confirm-account?token="+token.getConfirmationToken(), true);
        }
        catch (MessagingException e){
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, e);
        }

        return mailMessage;
    }

    public MimeMessage createForgotPasswordMail(String email, PasswordRenewalToken token){
        MimeMessage mailMessage = null;
        try{
            mailMessage = javaMailSender.createMimeMessage();
            mailMessage.setSubject("Reset Your Password!");

            MimeMessageHelper helper;
            helper = new MimeMessageHelper(mailMessage, true);
            helper.setFrom(MailConstants.mailFrom);
            helper.setTo(email);
            helper.setText("To reset your password, please click here : "
            +"http://localhost:8080/password-renewal?token="+token.getRenewalToken(), true);
        }
        catch (MessagingException e){
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, e);
        }
        return mailMessage;
    }
}
