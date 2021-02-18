package com.galbern.req.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

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
    private AtomicReference<MimeMessage> message1= new AtomicReference<>();



    public String sendSimpleGmail(String subjectText,
                                  String messageText,
                                  List<String> recipientEmails,
                                  boolean isMultipart) {
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
                    if(isMultipart) sendMultipartMail(message,
                            new File("/Users/smithtuka/Documents/Projects/req/src/main/resources/data.txt"),
                            messageText);
                } catch (MessagingException e) {
                    LOGGER.debug("[GMAIL-FAILURE] failure to set recipient address  ", e);
                }
            });
            message.setSubject(subjectText);
            message.setText(messageText);
            Transport.send(message);
            LOGGER.info("Sent from Gmail messages successfully....");
        } catch (MessagingException mex) {
            LOGGER.error("[GMAIL-FAILURE] - dispatch failure for {} ", recipientEmails, mex);
            return "failed";
        }
        return "success";
    }

    public String sendSimpleGcwMail(String subjectText, String messageText,
                                        List<String> recipientMails,
                                        boolean isMultipart) throws MessagingException, IOException {
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
            message.setSubject(subjectText);
            message.setFrom(new InternetAddress(defaultFromEmail));
            recipientMails.add(defaultRecipientEmail);
            recipientMails.forEach(mailAddress -> {
                try {
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailAddress));
                    message1.set(message);
                    if(isMultipart) sendMultipartMail(message,
                            new File("/Users/smithtuka/Documents/Projects/req/src/main/resources/data.txt"),
                            messageText);
                } catch (MessagingException e) {
                    LOGGER.debug("failure to set recipient address  in GalbernMail", e);
                }
            });
            message.setText(messageText);
            Transport.send(message);
        } catch (MessagingException mex) {
            LOGGER.error("[GCW-MAIL-FAILURE] GCW Mail dispatch failed to send for {} ", message1.get().getContent().toString(), recipientMails, mex);
            return "failed";
        }
        return "success";
    }

         public String sendMultipartMail(MimeMessage message, File file, String textMessage) {

            try {
                Multipart multipart = new MimeMultipart();
                MimeBodyPart attachmentPart = new MimeBodyPart();
                MimeBodyPart textPart = new MimeBodyPart();

                try {
                    File f = file;
                    attachmentPart.attachFile(f);
                    textPart.setText(textMessage); // might pick from mime already

                    multipart.addBodyPart(textPart);
                    multipart.addBodyPart(attachmentPart);

                } catch (IOException e) {
                    LOGGER.error("[MULTIPART-EMAIL-FAILURE] - exception sending multipart email", e);
                }

                message.setContent(multipart);
                LOGGER.debug("sending multi-part");
                Transport.send(message);
            } catch (MessagingException mex) {
                LOGGER.error("[MULTIPART-EMAIL-FAILURE] - exception sending multipart email", mex);
                return "failed";
            }
            LOGGER.info("successfully sent multi-part mail");
            return "success";

        }


}
