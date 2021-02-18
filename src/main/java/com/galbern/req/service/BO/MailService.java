package com.galbern.req.service.BO;

import com.galbern.req.exception.MailSenderException;
import com.galbern.req.utilities.EmailUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

@Component
public class MailService {

    public static Logger LOGGER = LoggerFactory.getLogger(MailService.class);

    @Autowired
    private EmailUtils emailUtils;

    @Retryable(value =Exception.class, maxAttempts = 4, backoff = @Backoff(delay=500))
    public String sendGMail(String subject, String body, List<String> recipients, boolean isMultipart) throws MessagingException {
        body = String.format( body + "%s", "\n\n best wishes from\n Team GCW-RMS");
        String result="failed";
        try{
            result=emailUtils.sendSimpleGmail(subject, body, recipients, isMultipart);
        } catch (Exception e){
            LOGGER.error("[GMAIL-FAILURE]error sending Gmail, ", e);
//            sendGalbernMail( new MessagingException(),  subject,  body,  recipients, isMultipart);
        }
        return result;
    }
    @Recover
    public String sendGcwMail(Exception ex,
                                String subject, String body,
                                List<String> recipients,
                                boolean isMultipart) throws MessagingException {
        String result="failed";
            LOGGER.info("starting  GCW Mailer .. due to {}", ex);
        try{
            result = emailUtils.sendSimpleGcwMail(subject, body, recipients, isMultipart);
        } catch (MailSenderException | IOException e){
            LOGGER.error("[GCW-MAILER-FAILURE] error sending  GCW Mail, ", e);
        }
        return result;
    }
}
