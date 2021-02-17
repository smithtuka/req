package com.galbern.req.service.BO;

import com.galbern.req.dao.RequisitionDao;
import com.galbern.req.jpa.entities.ApprovalStatus;
import com.galbern.req.jpa.entities.Requisition;
import com.galbern.req.service.RequisitionService;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

@Service
public class RequisitionBO implements RequisitionService {
    public static Logger LOGGER = LoggerFactory.getLogger(RequisitionBO.class);

    @PersistenceContext
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private RequisitionDao requisitionDao;

    public Requisition createRequisition(Requisition requisition){
        Requisition createdRequisition;
        try{
            createdRequisition = requisitionDao.save(requisition);
        } catch (RuntimeException ex){

            // do retry and alerts here
            throw new RuntimeException(ex);
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

    public List<Requisition> findAllRequisitions(){
        try {
            return requisitionDao.findAll();
        } catch (Exception e){
            LOGGER.debug(" exception fetching Requisition data");
            throw new RuntimeException(" didn't fetch requisition with ID", e);
        }
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
                case "STAGES": return requisitionDao.findRequisitionsByStageIds(stageIds);
                case "PROJECTS": {
                    query = entityManager.createQuery("select r from Requisition r join Project p on r.stage.project.id = p.id where  r.stage.project.id in (:projectIds)", Requisition.class)
                            .setParameter("projectIds", projectIds);
                    return query.getResultList();
                }
                case "REQUESTERS": return requisitionDao.findRequisitionsByRequesterId(requesterIds);
                case "APPROVALSTATUS": return requisitionDao.findRequisitionsByApprovalStatus(approvalStatus);
                case "SUBMISSIONDATE": return requisitionDao.findRequisitionsByRequestDate(submissionDate);
                default: return requisitionDao.findAll();
                }


        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
