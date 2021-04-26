package com.galbern.req.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class StageDto {
    private Long id;
    private String name;
}
