package com.galbern.req.dto;

import com.galbern.req.jpa.entities.Stage;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ProjectDto {
    private Long id;
    private String name;
    private List<StageDto> stages;
}
