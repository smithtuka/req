package com.galbern.req.service.BO;

import com.galbern.req.dao.RequisitionDao;
import com.galbern.req.exception.RequisitionExecutionException;
import com.galbern.req.jpa.entities.ApprovalStatus;
import com.galbern.req.jpa.entities.Item;
import com.galbern.req.jpa.entities.Requisition;
import com.galbern.req.service.RequisitionService;
import com.galbern.req.utilities.ExcelUtil;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RequisitionBO implements RequisitionService {
    public static Logger LOGGER = LoggerFactory.getLogger(RequisitionBO.class);

    @Autowired
    private RequisitionDao requisitionDao;
    @Autowired
    private ItemServiceBO itemServiceBO;
    @Autowired
    private MailService mailService;
    @Autowired
    private ExcelUtil excelUtil;

    @Retryable(value = {DataAccessResourceFailureException.class,
            TransactionSystemException.class}, maxAttempts = 2, backoff = @Backoff(delay = 1000))
    public Requisition createRequisition(Requisition requisition) throws IOException, MessagingException {
        Requisition createdRequisition;
        try{
            createdRequisition = requisitionDao.save(requisition);
            File file   =       excelUtil.findRequisitionFile(requisition); // testing
            mailService.sendGcwMail(String.format("Requisition-%s successfully submitted", requisition.getId()),
                    String.format("Hello %s\n\nYour Requisition totalling UGX %s has been successfully submitted!", requisition.getRequester().getFirstName(),  this.computeRequisitionAmount(requisition.getId())),
                    Arrays.asList(requisition.getRequester().getEmail()) ,file);
        } catch (Exception ex){
            LOGGER.error("[REQUISITION-CREATION-FAILURE] - failed to persist a requisition {}", new Gson().toJson(requisition).replaceAll("[\r\n]+", ""), ex);
            mailService.sendGcwMail(String.format("Requisition-%s creation failed {}\n{}", requisition.getId()),
                    String.format("Hello {},\n\nIt looks like your Requisition totalling UGX %s has failed to execute!\n{}\n You mau kindly try again!!",requisition.getRequester().getFirstName(), this.computeRequisitionAmount(requisition.getId()), new Gson().toJson(requisition)),
                    Arrays.asList(requisition.getRequester().getEmail()) ,null);
            throw new RequisitionExecutionException("failed to persist requisition", ex);
        }
        return createdRequisition;
    }

    public Requisition findRequisitionById(Long id){
        try {
            return requisitionDao.findById(id).get();
        } catch (Exception e){
            LOGGER.debug(" exception fetching Requisition data");
            throw new RuntimeException(" didn't fetch requisition with ID" + id, e);
        }
    }

    public Requisition updateRequisition(Requisition requisition){
        Long id = requisition.getId();
        Optional<Requisition> former = requisitionDao.findById(id);
        Requisition newRequisition = null;
        if(former.isPresent()){
            newRequisition = former.get();
            newRequisition.builder()
                    .id(requisition.getId())
                    .approvalStatus(requisition.getApprovalStatus())
                    .items(requisition.getItems())
                    .stage(requisition.getStage());
        }
        return requisitionDao.save(newRequisition);
    }

    public String deleteRequisition(Long id){
        Optional<Requisition> requisition = requisitionDao.findById(id);
        if(requisition.isPresent()){
            requisitionDao.deleteById(id);
            return id +" successfully deleted";
        };
        return "not deleted";
    }
    public List<Requisition> findRequisitions(List<Long> stageIds,
                                              List<Long> projectIds,
                                              List<Long> requesterIds,
                                              ApprovalStatus approvalStatus,
                                              Date submissionDate) {
        try {
            if (null==stageIds && null==projectIds && null==requesterIds && null==approvalStatus && null==submissionDate)
                return determineRequisitions("ALL", null, null, null, null, null);
            else if (null!=stageIds && null==projectIds && null==requesterIds && null==approvalStatus && null==submissionDate){
                return determineRequisitions("STAGES", stageIds, projectIds, requesterIds, approvalStatus, submissionDate);
            }  else if (null==stageIds && null!=projectIds && null==requesterIds && null==approvalStatus && null==submissionDate){
                return determineRequisitions("PROJECTS", stageIds, projectIds, requesterIds, approvalStatus, submissionDate);
            } else if (null==stageIds && null==projectIds && null!=requesterIds && null==approvalStatus && null==submissionDate) {
                return determineRequisitions("REQUESTERS", stageIds, projectIds, requesterIds, approvalStatus, submissionDate);
            } else if (null==stageIds && null==projectIds && null==requesterIds && null!=approvalStatus && null==submissionDate){
                return determineRequisitions("APPROVALSTATUS", stageIds, projectIds, requesterIds, approvalStatus, submissionDate);
            }else if (null==stageIds && null==projectIds && null==requesterIds && null==approvalStatus && null!=submissionDate){
                return determineRequisitions("SUBMISSIONDATE", stageIds, projectIds, requesterIds, approvalStatus, submissionDate);
            }

        } catch ( Exception e){
            LOGGER.debug("exception in ", getClass().getName(), e);
            throw new RequisitionExecutionException(" exception fetching requisitions from the database");
        }

        return null;

    }

    List<Requisition> determineRequisitions( String determinant,List<Long> stageIds,
                                             List<Long> projectIds,
                                             List<Long> requesterIds,
                                             ApprovalStatus approvalStatus,
                                             Date submissionDate){
        try {
            switch(determinant){
                case "ALL": return requisitionDao.findAll();
                case "STAGES": return requisitionDao.findRequisitionsByStageIdIn(stageIds);
                case "PROJECTS": return requisitionDao.findRequisitionsByStageProjectIdIn(projectIds);
                case "REQUESTERS": return requisitionDao.findRequisitionsByRequesterIdIn(requesterIds);
                case "APPROVALSTATUS": return requisitionDao.findRequisitionsByApprovalStatus(approvalStatus);
                case "SUBMISSIONDATE": return requisitionDao.findRequisitionsByRequiredDate(submissionDate);// rectify
                default: return requisitionDao.findAll();
                }
        } catch (Exception e){
            throw new RequisitionExecutionException(e);
        }
    }

    public BigDecimal computeRequisitionAmount(Long id){
        try {
            List<Item> items = itemServiceBO.findItems(id,null,null,null);
            items.forEach(i -> System.out.println(i.getPrice()+ " " + i.getQuantity()));
            return items.stream()
                    .map(item -> item.getQuantity().multiply(item.getPrice()))
                    .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        }catch (Exception e){
            LOGGER.error("[compute RequisitionAmount Failure] for {} Req id: ", id);
            throw e;
        }
    }

}
