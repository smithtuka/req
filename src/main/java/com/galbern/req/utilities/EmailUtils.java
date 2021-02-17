package com.galbern.req.utilities;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component

public class EmailUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailUtils.class);

    private  String recipientEmail = "smithtuka@gmail.com";

    private  String fromEmail = "donotreply@galbern.com";

    private  String mailHost = ""; // set this up


    /**
     * @param body
     * @param body
     */
    public void sendSimpleEmail(String subject, String body) {
        LOGGER.trace("sendSimpleEmail - sending email with subject {}", subject);
        body = String.format( body + "%s", "\n\n best wishes:\n GCW RMS");
        sendMail(subject, body);
        LOGGER.trace("sendSimpleEmail - exiting");
    }

    /**
     * @param subjectText
     * @param messageText
     */

    private  void sendMail(String subjectText, String messageText) {
        LOGGER.info("sendMail - entering");

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.host",mailHost);

        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(fromEmail));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));

            // Set Subject: header field
            message.setSubject(subjectText);

            // Set Message
            message.setContent(messageText, "text/plain; charset=utf-8");

            // Send message
            Transport.send(message);
            LOGGER.info("Email Sent Successfully");

        } catch (MessagingException mex) {
            LOGGER.error("Error during sending email :: ", mex);
        }
    }
}
