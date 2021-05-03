package com.galbern.req.dto;

import com.galbern.req.jpa.entities.ApprovalStatus;
import com.galbern.req.jpa.entities.Comment;
import com.galbern.req.jpa.entities.Item;
import com.galbern.req.jpa.entities.User;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
@Data
@Getter
@Builder
public class RequisitionStageDto {
    private Long id;
    private Date requiredDate;
    private Timestamp createdAt;
    private User requester;
    private Long stage;
    private List<Item> items;
    private ApprovalStatus approvalStatus = ApprovalStatus.RECEIVED;
    private List<Comment> requisitionComments;
}
