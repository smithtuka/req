package com.galbern.req.utilities;

import com.galbern.req.constants.RmsConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private ConfigProvider configProvider;


    private  static final String defaultFromEmail1 = "rms.galbern@gmail.com";
    private  static final String defaultFromEmail = "info@galbern.com";
    private  static final String mailHost = "mail.galbern.com"; //set this up for galbern
    private  static final String gmailHost = "smtp.gmail.com"; // set this up for gmail
    @Value("${mailer.secret}")
    private String secret;
    private AtomicReference<MimeMessage> message1= new AtomicReference<>();



    public String sendByGmail(String subjectText,
                              String messageText,
                              List<String> recipientEmails,
                              File file) {
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
            recipientEmails.forEach(mailAddress -> {
                try {
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailAddress));
                    if(null!=file && file.exists() && file.isFile()){ sendMultipartMail(message,
                            file,
                            messageText);}
                    else{
                        message.setSubject(subjectText);
                        message.setText(messageText);
                        Transport.send(message);
                        LOGGER.info("Sent from Gmail messages successfully....{}", recipientEmails.toString());
                    }
                } catch (MessagingException e) {
                    LOGGER.error("[GMAIL-FAILURE] failure to set recipient address  :: {}", recipientEmails.toString(), e);
                }
            });

        } catch (MessagingException mex) {
            LOGGER.error("[GMAIL-FAILURE] - dispatch failure for {} ", recipientEmails, mex);
            return "failed";
        }
        return "success";
    }

    private String sendByGcwMail(String subjectText, String messageText,
                                List<String> recipientMails,
                                File file) throws MessagingException, IOException {
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
            recipientMails.forEach(mailAddress -> {
                try {
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailAddress));
                    message1.set(message);
                    if (null != file && file.exists() && file.isFile()) {
                        sendMultipartMail(message, file, messageText);
                    } else {
                        message.setSubject(subjectText);
                        message.setText(messageText);
                        Transport.send(message);
                    }

                } catch (MessagingException e) {
                    LOGGER.debug("failure to set recipient address  in GalbernMail", e);
                }
            });
        } catch (MessagingException mex) {
            LOGGER.error("[GCW-MAIL-FAILURE] GCW Mail dispatch failed to send for {} to {}", message1.get().getContent().toString(), recipientMails, mex);
            return "failed";
        }
        return "success";
    }

         private String sendMultipartMail(MimeMessage message, File file, String textMessage) {

            try {
                Multipart multipart = new MimeMultipart();
                MimeBodyPart attachmentPart = new MimeBodyPart();
                MimeBodyPart textPart = new MimeBodyPart();

                try {
                    attachmentPart.attachFile(file);
                    textPart.setText(textMessage); // might pick from mime already
                    multipart.addBodyPart(textPart);
                    multipart.addBodyPart(attachmentPart);

                } catch (IOException e) {
                    LOGGER.error("[MULTIPART-EMAIL-FAILURE] - exception sending multipart email with attachment {}", file.getName(), e);
                    throw e;
                }

                message.setContent(multipart);
                LOGGER.debug("sending multi-part");
                Transport.send(message);
            } catch (MessagingException | IOException mex) {
                LOGGER.error("[MULTIPART-EMAIL-FAILURE] - exception sending multipart email", mex);
                return "failed";
            }
            LOGGER.info("successfully sent multi-part mail");
            return "success";

        }

        public String sendMailAlert(String subjectText, String messageText,
                               List<String> recipientMails,
                               File file) throws IOException, MessagingException {
        return RmsConstants.DEFAULT_EMAIL_DISPATCHER_SERVICE.equalsIgnoreCase(configProvider.getMailDispatcher()) ?
                this.sendByGcwMail(subjectText,messageText,recipientMails,file) :
                this.sendByGmail(subjectText,messageText,recipientMails,file) ;
        }


//    public String sendSimpleGmail(String subjectText,
//                              String messageText,
//                              List<String> recipientEmails) {
//        LOGGER.info("sendSimpleGMail - entering");
//
//        // Get system properties
//        Properties properties = System.getProperties();
//        properties.put("mail.smtp.host", gmailHost);
//        properties.put("mail.smtp.port", "465");
//        properties.put("mail.smtp.ssl.enable", "true");
//        properties.put("mail.smtp.auth", "true");
//
//        // Get the default Session object.
//        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication("rms.galbern", secret);
//            }
//        });
//
//        session.setDebug(true);
//
//        try {
//            // Create a default MimeMessage object.
//            MimeMessage message = new MimeMessage(session);
//            message.setFrom(new InternetAddress(defaultFromEmail1));
//            recipientEmails.forEach(mailAddress -> { // forkPool candidate
//                try {
//                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailAddress));
//                    message.setSubject(subjectText);
//                    message.setText(messageText);
//                    Transport.send(message);
//                } catch (MessagingException e) {
//                    LOGGER.debug("[SIMPLE-GMAIL-FAILURE] failure to set recipient address  ", e);
//                }
//            });
//            LOGGER.info("Sent from Gmail messages successfully to {}", (defaultFromEmail+recipientEmails.toString().replace("[","").replace("]","")));
//        } catch (MessagingException mex) {
//            LOGGER.error("[SIMPLE-GMAIL-FAILURE] - dispatch failure for :: {} ", recipientEmails, mex);
//            return "failed";
//        }
//        return "success";
//    }

}
