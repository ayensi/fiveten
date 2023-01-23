package com.altun.fiveten.service;

import com.altun.fiveten.job.EmailJob;
import com.altun.fiveten.model.ConfirmationToken;
import com.altun.fiveten.model.PasswordRenewalToken;
import com.altun.fiveten.model.User;
import com.altun.fiveten.dto.EmailRequestDTO;
import jakarta.mail.internet.MimeMessage;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.TimeZone;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service

public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private Scheduler scheduler;

    public void createConfirmationMail(User user, ConfirmationToken token){
        try{
            String subject = "Account Activation Needed!";
            String body = "To confirm your account, please click here : "
                    +"http://localhost:8080/api/auth/confirm-account?token="+token.getConfirmationToken();

            EmailRequestDTO emailRequestDTO = new EmailRequestDTO(user.getEmail(), subject, body, LocalDateTime.now(), TimeZone.getDefault().toZoneId());
            ZonedDateTime dateTime = ZonedDateTime.of(emailRequestDTO.getDateTime(), emailRequestDTO.getTimeZone());

            scheduleEmail(emailRequestDTO,dateTime);


        }
        catch (SchedulerException e){
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    public void createForgotPasswordMail(String email, PasswordRenewalToken token){
        try{
            String subject = "Reset Your Password!";

            String body = "To reset your password, please click here : "
                    +"http://localhost:8080/api/auth/password-renewal?token="+token.getRenewalToken();

            EmailRequestDTO emailRequestDTO = new EmailRequestDTO(email, subject, body, LocalDateTime.now(), TimeZone.getDefault().toZoneId());
            ZonedDateTime dateTime = ZonedDateTime.of(emailRequestDTO.getDateTime(), emailRequestDTO.getTimeZone());

            scheduleEmail(emailRequestDTO,dateTime);
        } catch (SchedulerException e) {
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private JobDetail buildJobDetail(EmailRequestDTO scheduleEmailRequestDTO){
        JobDataMap jobDataMap = new JobDataMap();

        jobDataMap.put("email", scheduleEmailRequestDTO.getEmail());
        jobDataMap.put("subject", scheduleEmailRequestDTO.getSubject());
        jobDataMap.put("body", scheduleEmailRequestDTO.getBody());

        return JobBuilder.newJob(EmailJob.class).withIdentity(UUID.randomUUID().toString(), "email-jobs")
                .withDescription("Send Email Job")
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();

    }

    private Trigger buildTrigger(JobDetail jobDetail, ZonedDateTime startedAt){
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(),"email-triggers")
                .withDescription("Send Email Trigger")
                .startAt(Date.from(startedAt.toInstant()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();

    }
    private void scheduleEmail(EmailRequestDTO emailRequestDTO, ZonedDateTime dateTime) throws SchedulerException {
        JobDetail jobDetail = buildJobDetail(emailRequestDTO);
        Trigger trigger = buildTrigger(jobDetail,dateTime);

        scheduler.scheduleJob(jobDetail,trigger);
    }
}
