package com.galbern.req.controller;

import com.galbern.req.service.BO.MailService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Api(value = "controller for RMS mailing service version v1", tags={"RMSV1MailsController"})
@RestController
@RequestMapping("/mail")
public class MailsController {
    private static Logger LOGGER = LoggerFactory.getLogger(MailsController.class);
    @Autowired
    private MailService mailAgent;

    @GetMapping
    public String sendMail(@RequestParam(value="subject") String subject,
                           @RequestParam(value="body") String body,
                           @RequestParam(value="file") File file,
                           @RequestParam(value="recipient") List<String> recipients) throws RuntimeException{
        LOGGER.info("entering mail controller");
        try {
            LOGGER.info("entering try in mail controller");
            mailAgent.sendGcwMail(subject, body, recipients, file);
            return "success";
        } catch (RuntimeException | MessagingException | IOException ex) {
            LOGGER.debug("EXCEPTION MAILING", ex);
        }
        return HttpStatus.INTERNAL_SERVER_ERROR.toString();
    }
}
