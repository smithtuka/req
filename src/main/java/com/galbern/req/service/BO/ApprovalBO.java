package com.galbern.req.service.BO;

import com.galbern.req.jpa.entities.ApprovalStatus;
import com.galbern.req.jpa.entities.Item;
import com.galbern.req.jpa.entities.Requisition;
import com.galbern.req.jpa.dao.RequisitionDao;
import com.galbern.req.utilities.ExcelUtil;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;


@Service
@Profile("!firebase")
public class ApprovalBO implements com.galbern.req.service.ApprovalService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApprovalBO.class);
    @Autowired
    private MailService mailService;
    @Autowired
    private ExcelUtil excelUtil;
    @Autowired
    private RequisitionDao requisitionDao;
    @Autowired
    private ItemServiceBO itemServiceBO;

    public void asyncNotify(Requisition requisition) throws IOException, MessagingException {
        String subject = String.format("Requisition %s by %s %s",
                requisition.getId(), (requisition.getRequester()).getFirstName(), requisition.getApprovalStatus());
        LOGGER.info("Subject :: {}", subject);
        File file = excelUtil.findRequisitionFile(requisition);
            ForkJoinPool pool = new ForkJoinPool(1);
            pool.submit( () -> List.of(requisition).parallelStream()
            .forEach(requisition1 -> buildConsumer(requisition, subject, file).accept(requisition)));
    }

    private Consumer buildConsumer(Requisition requisition, String subject, File file) {
        return request -> {
            LOGGER.info("sending email. subject :: {}", subject);
            try{
                mailService.sendGcwMail("RMS "+ subject, //"%,.2f", amount.setScale(2, RoundingMode.DOWN)
                        String.format(" Amount :: UGX %s", computeRequisitionAmount(requisition.getId())), // %d ? instead
                        Arrays.asList(requisition.getRequester().getEmail()), file);
            } catch (MessagingException | IOException e) {
                LOGGER.error("Exception notifying about requisition status for: {}", new Gson().toJson(requisition).replaceAll("[\n\r]+",""), e);
            }

        };
    }

    @Override
    public void createRequisition(Requisition requisition) throws IOException, MessagingException {
        asyncNotify(requisition);
    }

    @Override
    public String handleApproval(Long requisitionId, ApprovalStatus approvalStatus) throws IOException, MessagingException, InterruptedException {
         // execute parallel to improve response time ----
        Requisition requisition = null;
        try {
            requisition = requisitionDao.findById(requisitionId).orElseThrow(() -> new IllegalArgumentException("Requisition not found: " + requisitionId));
            if (approvalStatus.equals(ApprovalStatus.REJECTED)) {
                requisition.setApprovalStatus(ApprovalStatus.REJECTED);
            } else {
                requisition.setApprovalStatus(
                        requisition.getApprovalStatus().equals(ApprovalStatus.RECEIVED) ? ApprovalStatus.AUTHORIZED : ApprovalStatus.APPROVED
                );
            }
            requisitionDao.save(requisition);
            return "success";
        } catch (Exception e){
            LOGGER.error("FAILED-TO-HANDLE-APPROVAL/REJECTION", e);
            throw e;
        } finally {
//            executorService.invokeAll(Arrays.asList());
            if(requisition != null && !requisition.getApprovalStatus().equals(ApprovalStatus.AUTHORIZED) ) asyncNotify(requisition);

        }

    }

    private BigDecimal computeRequisitionAmount(Long requisitionId) {
        List<Item> items = itemServiceBO.findItems(requisitionId, null, null, null);
        return items.stream()
                .map(item -> item.getQuantity().multiply(item.getPrice()))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }
}
