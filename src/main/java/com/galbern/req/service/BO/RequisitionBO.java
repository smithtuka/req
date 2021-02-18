package com.galbern.req.service.BO;

import com.galbern.req.dao.RequisitionDao;
import com.galbern.req.exception.RequisitionExecutionException;
import com.galbern.req.jpa.entities.ApprovalStatus;
import com.galbern.req.jpa.entities.Requisition;
import com.galbern.req.service.RequisitionService;
import com.galbern.req.utilities.ConfigProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RequisitionBO implements RequisitionService {
    public static Logger LOGGER = LoggerFactory.getLogger(RequisitionBO.class);

    @PersistenceContext
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ConfigProvider configProvider;

    @Autowired
    private RequisitionDao requisitionDao;

    @Retryable(value = {DataAccessResourceFailureException.class,
            TransactionSystemException.class}, maxAttempts = 2, backoff = @Backoff(delay = 1000))
    public Requisition createRequisition(Requisition requisition){
        Requisition createdRequisition;
        try{
            createdRequisition = requisitionDao.save(requisition);
        } catch (RuntimeException ex){
            LOGGER.error("[REQUISITION-PERSIST-FAILURE] - failed to persist a requisition", ex);
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
                                              LocalDate submissionDate) {
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

        } catch ( RuntimeException e){
            LOGGER.debug("exception in ", getClass().getName(), e);
            throw new RuntimeException(" exception fetching requisitions from the database");
        }

        return null;

    }

    List<Requisition> determineRequisitions( String determinant,List<Long> stageIds,
                                             List<Long> projectIds,
                                             List<Long> requesterIds,
                                             ApprovalStatus approvalStatus,
                                             LocalDate submissionDate){
        TypedQuery<Requisition> query;
        try {
            switch(determinant){
                case "ALL": return requisitionDao.findAll();
                case "STAGES": return requisitionDao.findRequisitionsByStageIdIn(stageIds);
                case "PROJECTS": {
//                    query = entityManager.createQuery("select r from Requisition r join Project p on r.stage.project.id = p.id where  r.stage.project.id in (:projectIds)", Requisition.class)
//                            .setParameter("projectIds", projectIds);
//                    return query.getResultList();
                    return requisitionDao.findRequisitionsByStageProjectIdIn(projectIds);
                }
                case "REQUESTERS": return requisitionDao.findRequisitionsByRequesterIdIn(requesterIds);
                case "APPROVALSTATUS": return requisitionDao.findRequisitionsByApprovalStatus(approvalStatus);
                case "SUBMISSIONDATE": return requisitionDao.findRequisitionsByRequestDate(submissionDate);// rectify
                default: return requisitionDao.findAll();
                }


        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
