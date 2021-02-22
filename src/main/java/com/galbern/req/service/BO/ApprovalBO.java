package com.galbern.req.service.BO;

import com.galbern.req.jpa.entities.ApprovalStatus;
import com.galbern.req.jpa.entities.Requisition;
import com.galbern.req.utilities.ExcelUtil;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;


@Service
public class ApprovalBO {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApprovalBO.class);
    @Autowired
    private MailService mailService;
    @Autowired
    private RequisitionBO requisitionBO;
    private ExcelUtil excelUtil; // need autowire?

    public Requisition approveRequisition(Requisition requisition) {
        requisition.setApprovalStatus(ApprovalStatus.RECEIVED.equals(requisition.getApprovalStatus()) ?
                ApprovalStatus.PARTIAL : ApprovalStatus.APPROVED);
        return requisition;
    }

    public Requisition rejectRequisition(Requisition requisition) {
        requisition.setApprovalStatus(ApprovalStatus.REJECTED);
        return requisition;
    }

    public void notify(Requisition requisition) throws IOException, MessagingException {
        String subject = String.format("Requisition %s by {} {}",
                requisition.getId(), (requisition.getRequester()).getFirstName(), requisition.getApprovalStatus());
        try{
            File file = excelUtil.findRequisitionFile(requisition);
            mailService.sendGcwMail(subject,
                    requisitionBO.computeRequisitionAmount(requisition.getId()).toString(),
                            Arrays.asList(requisition.getRequester().getEmail()), file);
        } catch (MessagingException | IOException e) {
            LOGGER.error("Exception notifying about requisition status for: {}", new Gson().toJson(requisition).replaceAll("[\n\r]+",""), e);
            throw e;
        }
    }

}
