package com.galbern.req.utilities;

import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.mail.PasswordAuthentication;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component

public class EmailUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailUtils.class);

    private  static final String defaultRecipientEmail = "rms.galbern@gmail.com";
    private  static final String defaultFromEmail1 = "rms.galbern@gmail.com";
    private  static final String defaultFromEmail = "info@galbern.com";
    private  static final String mailHost = "mail.galbern.com"; // set this up for galbern
    private  static final String gmailHost = "smtp.gmail.com"; // set this up for gmail
    @Value("${mailer.secret}")
    private String secret;



    public void sendSimpleGmail(String subjectText, String messageText, List<String> recipientEmails) {
        LOGGER.info("sendGMail - entering");

        // Get system properties
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", gmailHost);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");


        // Get the default Session object.
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("rms.galbern", secret);
            }
        });

        session.setDebug(true);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(defaultFromEmail1));
            recipientEmails.add(defaultRecipientEmail);
            recipientEmails.forEach(mailAddress -> {
                try {

                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailAddress));
                } catch (MessagingException e) {
                    LOGGER.debug("failure to set recipient address  ", e);
                }
            });

            message.setSubject(subjectText);
            message.setText(messageText);
            Transport.send(message);
            LOGGER.info("Sent from Gmail messages successfully....");
        } catch (MessagingException mex) {
            LOGGER.error("GMail dispatch failure for {} ", recipientEmails, mex);
            mex.printStackTrace();
        }
    }

    public void sendSimpleGalbernMail(String subjectText, String messageText, List<String> recipientMails){
        Properties properties = System.getProperties();
        // Setup Galbern server
        properties.put("mail.host", mailHost);
        properties.put("mail.smtp.port", "587"); // 465 for authenticated ssl / tls
//        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

//        Session session = Session.getDefaultInstance(properties);
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("info@galbern.com", secret);
            }

        });

        session.setDebug(true);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(defaultFromEmail));
            recipientMails.add(defaultRecipientEmail);
            recipientMails.forEach(mailAddress -> {
                try {

                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailAddress));
                } catch (MessagingException e) {
                    LOGGER.debug("failure to set recipient address  in GalbernMail", e);
                }
            });

            message.setSubject(subjectText);
            message.setText(messageText);
            Transport.send(message);
            LOGGER.info("Sent Messages from Galbern Mail successfully....");
        } catch (MessagingException mex) {
            LOGGER.error("GalbernMail dispatch failure for {} ", recipientMails, mex);
            mex.printStackTrace();
        }

    }
}
