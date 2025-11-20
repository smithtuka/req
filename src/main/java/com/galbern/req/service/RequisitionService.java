package com.galbern.req.service;

import com.galbern.req.dto.RequisitionMetaData;
import com.galbern.req.jpa.entities.ApprovalStatus;
import com.galbern.req.jpa.entities.Requisition;

import jakarta.mail.MessagingException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface RequisitionService {

    List<Requisition> findAllRequisitions();

    Requisition createRequisition(Requisition requisition) throws IOException, MessagingException;

    Requisition findRequisitionById(Long requisitionId);

    List<Requisition> findRequisitions(List<Long> stageIds, List<Long> projectIds, List<Long> requesterIds, ApprovalStatus approvalStatus, Date submissionDate);

    String deleteRequisition(Long requisitionId);

    Requisition updateRequisition(Requisition requisition);

    RequisitionMetaData getRequisitionMetaData(Long id);
}
