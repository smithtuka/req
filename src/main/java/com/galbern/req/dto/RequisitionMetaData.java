package com.galbern.req.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class RequisitionMetaData {
    private String projectName;
    private String stageName;
    private BigDecimal stageBudget;
    private BigDecimal stageProvisionalSum;
}
