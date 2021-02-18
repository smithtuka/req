package com.galbern.req.controller;

import com.galbern.req.service.BO.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.util.List;

@RestController
@RequestMapping("/mail")
public class MailController {
    private static Logger LOGGER = LoggerFactory.getLogger(MailController.class);
    @Autowired
    private MailService mailAgent;

    @GetMapping
    public String sendMail(@RequestParam(value="subject") String subject,
                           @RequestParam(value="body") String body,
                           @RequestParam(value="isMultipart") boolean isMultipart,
                           @RequestParam(value="recipient") List<String> recipients) throws RuntimeException{
        LOGGER.info("entering mail controller");
        try {
            LOGGER.info("entering try in mail controller");
            mailAgent.sendGcwMail(new Exception(""), subject, body, recipients, isMultipart);
            return "success";
        } catch (RuntimeException | MessagingException ex) {
            LOGGER.debug("EXCEPTION MAILING", ex);
        }
        return HttpStatus.INTERNAL_SERVER_ERROR.toString();
    }
}
