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
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Service
public class ApprovalBO {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApprovalBO.class);
    @Autowired
    private MailService mailService;
    @Autowired
    private RequisitionBO requisitionBO;
    @Autowired
    private ExcelUtil excelUtil;

    private ExecutorService executorService = Executors.newCachedThreadPool(); // use later

    public void notify(Requisition requisition) throws IOException, MessagingException {
        String subject = String.format("Requisition %s by {} {}",
                requisition.getId(), (requisition.getRequester()).getFirstName(), requisition.getApprovalStatus());
        File file = excelUtil.findRequisitionFile(requisition);
        try{
            mailService.sendGcwMail(subject, //"%,.2f", amount.setScale(2, RoundingMode.DOWN)
                    String.format("%s", requisitionBO.computeRequisitionAmount(requisition.getId())),
                            Arrays.asList(requisition.getRequester().getEmail()), file);
        } catch (MessagingException | IOException e) {
            LOGGER.error("Exception notifying about requisition status for: {}", new Gson().toJson(requisition).replaceAll("[\n\r]+",""), e);
            throw e;
        }
    }

    public void createRequisition(Requisition requisition) throws IOException, MessagingException {
        notify(requisition);
    }

    public String handleApproval(Long requisitionId, ApprovalStatus approvalStatus) throws IOException, MessagingException {
         // execute parallel to improve response time ----
        Requisition requisition = null;
        try {
            requisition = requisitionBO.handleApproval(requisitionId, approvalStatus) ;
            return "success";
        } catch (Exception e){
            LOGGER.error("FAILED-TO-HANDLE-APPROVAL/REJECTION", e);
            throw e;
        } finally {
            if(!requisition.getApprovalStatus().equals(ApprovalStatus.PARTIAL) ) notify(requisition);
            // improve -- avoid fetching multiple times and execute parallel
        }

    }
}
