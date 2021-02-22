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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MailService {

    public static Logger LOGGER = LoggerFactory.getLogger(MailService.class);

    public static final String DEFAULT_CC = "rms.galbern@gmail.com"; // externalize
    @Autowired
    private EmailUtils emailUtils;

    @Retryable(value = Exception.class, maxAttempts = 4, backoff = @Backoff(delay = 500))
    public String sendGcwMail(String subject, String body, List<String> recipients, File file) throws MessagingException, IOException {
        LOGGER.info("[GCW-MAIL-SERVICE-MAIN] -- starting  GCW MAIN Mailer ...");
        body = String.format("\n" + body + "%s", "\n\nregards,\nTeam GCW-RMS");
        List<String> allRecipients = new ArrayList<>(recipients);
        allRecipients.add(DEFAULT_CC);
        return emailUtils.sendSimpleGcwMail(subject, body, allRecipients, file);
    }

    @Recover
    public String recoverMail(Exception ex,
                              String subject, String body,
                              List<String> recipients,
                              File file) {
        return sendGMail(subject, body, recipients, file);
    }

    @Retryable(value = Exception.class, maxAttempts = 4, backoff = @Backoff(delay = 500))
    public String sendGMail(String subject, String body, List<String> recipients, File file) {
        LOGGER.info("[GMAIL-SENDER-BACKUP] -- starting  GCW BACKUP Mailer ...");
        try {
            return emailUtils.sendSimpleGmail(subject, body, recipients, file);
        } catch (Exception e) {
            LOGGER.error("[GMAIL-FAILURE] error sending Gmail, ", e);
            throw e;
        }
    }
}
