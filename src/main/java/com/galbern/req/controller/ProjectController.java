package com.galbern.req.controller;

import com.galbern.req.service.BO.ProjectServiceBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/projects")
public class ProjectController {
    @Autowired
    private ProjectServiceBO projectServiceBO;

}
