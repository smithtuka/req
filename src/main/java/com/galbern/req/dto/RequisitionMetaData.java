package com.galbern.req.dto;

import lombok.Builder;
import lombok.Data;
import software.amazon.ion.Decimal;

import java.math.BigDecimal;

@Data
@Builder
public class RequisitionMetaData {
    private String projectName;
    private String stageName;
    private BigDecimal stageBudget;
    private BigDecimal stageProvisionalSum;
}
