package com.galbern.req.dto;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class RequisitionDto {
    private Long id;
    private String name;
}
