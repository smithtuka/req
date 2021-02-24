package com.galbern.req.controller;

import com.galbern.req.service.BO.MailService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/setup")
    public String setup(@RequestParam(value="h2pass") String h2P,
                           @RequestParam(value="mailpass") String mailP,
                           @RequestParam(value="retrieve") boolean retrieve
                        ) throws RuntimeException{
        LOGGER.info("entering mail setup");
        try {
            LOGGER.info("entering try in mail controller");
            System.setProperty("h2p", h2P);
            System.setProperty("mailP", mailP);
            String sendBack = String.format("success\n%s",System.getenv());
            return !retrieve? "successfully set h2Pa&MailPa": sendBack;
        } catch (Exception ex) {
            LOGGER.debug("EXCEPTION MAILING", ex);
        }
        return HttpStatus.INTERNAL_SERVER_ERROR.toString();
    }

}
