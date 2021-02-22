package com.galbern.req.jpa.entities;

public enum ApprovalStatus {
    RECEIVED, // after submission
    PARTIAL, // 1st approval, 2nd pending
    APPROVED, // both approved
    REJECTED // any of the two rejects -- goes to saved for changes
}
