package com.galbern.req.controller;

import com.galbern.req.jpa.entities.Stage;
import com.galbern.req.service.BO.StageServiceBO;
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

@Api(value = "controller for RMS Stages version v1", tags={"RMSV1StagesController"})
@RestController
@RequestMapping("/v1/stages")
public class StagesController {

    public static Logger LOGGER = LoggerFactory.getLogger(StagesController.class);
    @Autowired
    private StageServiceBO stageServiceBO;

    @ApiOperation(value = "ping", nickname = "ping", notes = "to ping")
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS", response = String.class),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR", response = String.class)
    })
    @GetMapping("/ping")
    public String ping(@RequestParam(value="echo", required = false) String echo){
        LOGGER.info("GET /v1/stages in {}", Thread.currentThread().getStackTrace()[1].getMethodName());
        return "ping pong!\n\t"+echo;
    }


    @ApiOperation(value = "createStage", nickname = "createAStage", notes = "use to create a Stage")
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS", response = Stage.class),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR", response = Stage.class)
    })
    @PostMapping
    public @ResponseBody
    ResponseEntity<Stage> makeStage(@RequestBody Stage stage) {

        try {
            LOGGER.info("POST /v1/stages in {}", Thread.currentThread().getStackTrace()[1].getMethodName());
            return new ResponseEntity<>(stageServiceBO.createStage(stage), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("error - failed to create a stage", e);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ApiOperation(value = "findStageById", nickname = "Stages",
            notes = "to fetch a single Stage by Id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS", response = Stage.class),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR", response = String.class)
    })
    @GetMapping("/{stageId}")
    public ResponseEntity<Stage> findStageById(@PathVariable("stageId") Long stageId) {
        try {
            LOGGER.info("GET /v1/stages {}", Thread.currentThread().getStackTrace()[1].getMethodName());
            return new ResponseEntity<>(stageServiceBO.findById(stageId), HttpStatus.OK);
        } catch (RuntimeException ex) {
            LOGGER.error("error executing findStageById", ex);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiOperation(value = "findStagesOfAProject", nickname = "ALL Stages in a project",
            notes = "to fetch all stages by a projectId")
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS", response = Stage.class),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR", response = String.class)
    })
    @GetMapping
    public ResponseEntity<List<Stage>> findStagesByProjectId(@RequestParam(value = "projectId") Long stageId) {
        try {
            LOGGER.info("GET /v1/stages {}", Thread.currentThread().getStackTrace()[1].getMethodName());
            return new ResponseEntity<>(stageServiceBO.findStagesByProjectId(stageId), HttpStatus.OK);
        } catch (RuntimeException ex) {
            LOGGER.error("error executing findAllStages", ex);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ApiOperation(value = "updateStage", nickname = "updateStage",
            notes = "to update Stages")
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS", response = Stage.class),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR", response = String.class)
    })
    @PutMapping
    public ResponseEntity<Stage> updateStage(@RequestBody Stage stage) {
        try {
            LOGGER.info("PUT /v1/stages in {}", Thread.currentThread().getStackTrace()[1].getMethodName());
            return new ResponseEntity<>(stageServiceBO.updateStage(stage), HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("error updateStage in handler", ex);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiOperation(value = "deleteStage", nickname = "delete",
            notes = "to delete a single Stage by Id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS", response = String.class),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR", response = String.class)
    })
    @DeleteMapping("/{stageId}")
    public ResponseEntity<String> deleteStage(@PathVariable("stageId") Long stageId) {
        try {
            LOGGER.info("DELETE /v1/Stages in {}", Thread.currentThread().getStackTrace()[1].getMethodName());
            stageServiceBO.deleteStage(stageId);
            return new ResponseEntity<>("successfully deleted stage : " + stageId, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("error executing deleteStage in handler", ex);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
}
