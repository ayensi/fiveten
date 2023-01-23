package com.altun.fiveten.job;

import com.altun.fiveten.constants.MailConstants;
import com.altun.fiveten.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.logging.Level;
import java.util.logging.Logger;

public class EmailJob extends QuartzJobBean {

    @Autowired
    private MailProperties mailProperties;
    @Autowired
    private JavaMailSender javaMailSender;
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getMergedJobDataMap();

        String subject = jobDataMap.getString("subject");
        String body = jobDataMap.getString("body");
        String recipient = jobDataMap.getString("email");

        sendMail(recipient,subject,body);
    }

    private void sendMail(String recipient, String subject, String mailBody) {

        MimeMessage mailMessage = null;
        try{
            mailMessage = javaMailSender.createMimeMessage();
            mailMessage.setSubject(subject);

            MimeMessageHelper helper;
            helper = new MimeMessageHelper(mailMessage, true);
            helper.setFrom(MailConstants.mailFrom);
            helper.setTo(recipient);
            helper.setText(mailBody, true);

            javaMailSender.send(mailMessage);

        }
        catch (MessagingException e){
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
