package com.galbern.req.jpa.dao;

import com.galbern.req.jpa.entities.ApprovalStatus;
import com.galbern.req.jpa.entities.Requisition;
import com.galbern.req.jpa.entities.Stage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
//    Query("SELECT r from Requisition r JOIN FETCH r.stage where r.id = :id")
    @Query("SELECT r.stage from Requisition r where r.id = :id")
    Stage getStageId(@Param("id") Long id);

}
