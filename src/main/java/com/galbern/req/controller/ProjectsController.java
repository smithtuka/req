package com.galbern.req.controller;

import com.galbern.req.dto.ProjectDto;
import com.galbern.req.dto.StageDto;
import com.galbern.req.jpa.entities.Project;
import com.galbern.req.service.BO.ProjectServiceBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Api(value = "controller for RMS Projects version v1", tags={"RMSV1ProjectsController"})
@RestController
@RequestMapping("/v1/projects")
public class ProjectsController {
    private static Logger LOGGER = LoggerFactory.getLogger(ProjectsController.class);
    @Autowired
    private ProjectServiceBO projectServiceBO;


    @ApiOperation(value = "ping", nickname = "ping", notes = "to ping")
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS", response = String.class),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR", response = String.class)
    })
    @GetMapping("/ping")
    public String ping(@RequestParam(value = "echo", required = false) String echo) {
        LOGGER.info("GET /v1/projects in {}", Thread.currentThread().getStackTrace()[1].getMethodName());
        return "ping pong!\n\t" + echo;
    }

    @ApiOperation(value = "createProject", nickname = "createAProject", notes = "use to create a Project")
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS", response = Project.class),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR", response = Project.class)
    })
    @PostMapping
    public @ResponseBody
    ResponseEntity<Project> makeProject(@RequestBody Project project) {

        try {
            LOGGER.info("POST /v1/projects in {}", Thread.currentThread().getStackTrace()[1].getMethodName());
            return new ResponseEntity<>(projectServiceBO.createProject(project), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("error - failed to create a project", e);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ApiOperation(value = "findProjectById", nickname = "Projects",
            notes = "to fetch a single Project by Id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS", response = Project.class),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR", response = String.class)
    })
    @GetMapping("/{projectId}")
    public ResponseEntity<Project> findProjectById(@PathVariable("projectId") Long projectId) {
        try {
            LOGGER.info("GET /v1/projects {}", Thread.currentThread().getStackTrace()[1].getMethodName());
            return new ResponseEntity<>(projectServiceBO.findById(projectId), HttpStatus.OK);
        } catch (RuntimeException ex) {
            LOGGER.error("error executing findProjectById", ex);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiOperation(value = "findProjects", nickname = "ALL",
            notes = "to fetch all projects")
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS", response = Project.class),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR", response = String.class)
    })
    @GetMapping
    public ResponseEntity<List<Project>> findProjects() {
        try {
            LOGGER.info("GET /v1/projects {}", Thread.currentThread().getStackTrace()[1].getMethodName());
            return new ResponseEntity<>(projectServiceBO.findAllProjects(), HttpStatus.OK);
        } catch (RuntimeException ex) {
            LOGGER.error("error executing findAllProjects", ex);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ApiOperation(value = "updateProject", nickname = "updateProject",
            notes = "to update Projects")
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS", response = Project.class),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR", response = String.class)
    })
    @PutMapping
    public ResponseEntity<Project> updateProject(@RequestBody Project project) {
        try {
            LOGGER.info("PUT /v1/projects in {}", Thread.currentThread().getStackTrace()[1].getMethodName());
            return new ResponseEntity<>(projectServiceBO.updateProject(project), HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("error updateProject in handler", ex);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiOperation(value = "deleteProject", nickname = "delete",
            notes = "to delete a single Project by Id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS", response = String.class),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR", response = String.class)
    })
    @DeleteMapping("/{projectId}")
    public ResponseEntity<String> deleteProject(@PathVariable("projectId") Long projectId) {
        try {
            LOGGER.info("DELETE /v1/Projects in {}", Thread.currentThread().getStackTrace()[1].getMethodName());
            projectServiceBO.deleteProject(projectId);
            return new ResponseEntity<>("successfully deleted project : " + projectId, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("error executing deleteProject in handler", ex);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }


//    DTO Based
@ApiOperation(value = "Project DTO", nickname = "",
        notes = "proj dto", tags = " dto for projects")
@ApiResponses({
        @ApiResponse(code = 200, message = "SUCCESS", response = String.class),
        @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR", response = String.class)
})

@GetMapping("/dto")
public ResponseEntity<List<ProjectDto>> getProjectDto() {
    try {
        LOGGER.info("GET /v1/Projects in {}", Thread.currentThread().getStackTrace()[1].getMethodName());
        return  ResponseEntity.ok(projectServiceBO.findAllProjects().stream()
                .map(project ->
                        ProjectDto.builder().id(project.getId()).name(project.getName()).stages(
                                project.getStages().stream().map(stage -> StageDto.builder().id(stage.getId()).name(stage.getName()).build()).collect(Collectors.toList())
                        ).build()
                ).collect(Collectors.toList()));
    } catch (Exception ex) {
        LOGGER.error("error executing project dto in handler", ex);
    }
    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
}

}
