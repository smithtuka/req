package com.galbern.req.dao;

import com.galbern.req.jpa.entities.ApprovalStatus;
import com.galbern.req.jpa.entities.Requisition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RequisitionDao extends JpaRepository<Requisition, Long> {
    @Query("select r from Requisition  r where r.stage.id in ( ?1)")
    List<Requisition> findRequisitionsByStageIds(List<Long> stageIds);
    @Query("select r from Requisition  r where r.requester.id in ( ?1)")
    List<Requisition> findRequisitionsByRequesterId(List<Long> requesterIds);
    List<Requisition> findRequisitionsByApprovalStatus(ApprovalStatus approvalStatus);
    @Query("select r from Requisition  r where r.requestDate > ( ?1)")
    List<Requisition> findRequisitionsByRequestDate(LocalDate submissionDate);
}
