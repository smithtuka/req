package com.galbern.req.utilities;

import com.galbern.req.exception.MailSenderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.List;

//@UtilityClass
@Component
public class MailSender {

    public static Logger LOGGER = LoggerFactory.getLogger(MailSender.class);

    @Autowired
    private EmailUtils emailUtils;

    @Retryable(value =MessagingException.class, maxAttempts = 2, backoff = @Backoff(delay=1000))
    public void sendMail(String subject, String body, List<String> recipients){
        body = String.format( body + "%s", "\n\n best wishes from\n Team GCW-RMS");
        try{
            emailUtils.sendSimpleGmail(subject, body, recipients);
        } catch (MailSenderException e){
            LOGGER.error("[GMAIL-FAILURE]error sending Gmail, ", e);
            sendGalbernMail( new MessagingException(),  subject,  body,  recipients);
        }
    }
    @Recover
    public void sendGalbernMail(MessagingException ex, String subject, String body, List<String> recipients){
        try{
            emailUtils.sendSimpleGalbernMail(subject, body, recipients);
        } catch (MailSenderException e){
            LOGGER.info("[GCW-MAILER-FAILURE] error sending  GCW Mail, ", e);
        }
    }
}
