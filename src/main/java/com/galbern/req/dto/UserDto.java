package com.galbern.req.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {

    private String username;
    private boolean active;
    private String role;

}
