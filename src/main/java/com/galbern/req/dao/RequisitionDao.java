package com.galbern.req.dao;

import com.galbern.req.jpa.entities.ApprovalStatus;
import com.galbern.req.jpa.entities.Requisition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RequisitionDao extends JpaRepository<Requisition, Long> {
    List<Requisition> findRequisitionsByStageIdIn(List<Long> stageIds);
    List<Requisition> findRequisitionsByStageProjectIdIn(List<Long> projectIds);
    List<Requisition> findRequisitionsByRequesterIdIn(List<Long> requesterIds);
    List<Requisition> findRequisitionsByApprovalStatus(ApprovalStatus approvalStatus);
    List<Requisition> findRequisitionsByRequiredDate(Date submissionDate);
}
