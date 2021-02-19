package com.galbern.req.controller;

import com.galbern.req.exception.RequisitionExecutionException;
import com.galbern.req.jpa.entities.ApprovalStatus;
import com.galbern.req.jpa.entities.Requisition;
import com.galbern.req.service.BO.RequisitionBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Api(value = "controller for RMS service version v1", tags={"RMSV1Controller"})
@RestController
@RequestMapping("/v1/requisitions")
public class RequisitionController {
    public static Logger LOGGER = LoggerFactory.getLogger(RequisitionController.class);
    @Autowired
    private RequisitionBO requisitionBO;


    @ApiOperation(value = "ping", nickname = "ping", notes = "to ping")
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS", response = String.class),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR", response = String.class)
    })
    @GetMapping("/ping")
    public String ping(@RequestParam(value="echo", required = false) String echo){
        LOGGER.info("GET /v1/requisitions in {}", Thread.currentThread().getStackTrace()[1].getMethodName());
        return "ping pong!\n\t"+echo;
    }

    @ApiOperation(value = "makeRequisition", nickname = "makeRequisition", notes = "use to create a requisition")
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS", response = Requisition.class),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR", response = Requisition.class)
    })
    @PostMapping
    public @ResponseBody ResponseEntity<Requisition> makeRequisition(@RequestBody Requisition requisition){

        try {
            LOGGER.info("POST /v1/requisitions in {}", requisition.getRequester().getId());
            return new ResponseEntity<>(requisitionBO.createRequisition(requisition), HttpStatus.OK);
        } catch (Exception e){
            LOGGER.error("[REQUISITION-CREATION-FAILURE]- failed to create requisition", e);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ApiOperation(value = "findRequisitionById", nickname = "requisitions",
            notes = "to fetch a single requisitions by Id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS", response = Requisition.class),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR", response = String.class)
    })
    @GetMapping("/{requisitionId}")
    public ResponseEntity<Requisition> findRequisitionById(@PathVariable("requisitionId") Long requisitionId){
        try{
            LOGGER.info("GET /v1/requisitions {}", Thread.currentThread().getStackTrace()[1].getMethodName());
            return new ResponseEntity<>(requisitionBO.findRequisitionById(requisitionId), HttpStatus.OK);
        } catch (RuntimeException ex){
            LOGGER.error("error executing findRequisitionById in handler", ex);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiOperation(value = "findRequisitions", nickname = "requisitions",
            notes = "to fetch lists of requisitions by stageId, projectId, requesterId, approvalStatus, submissionDate")
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS", response = String.class),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR", response = String.class)
    })
    @GetMapping
    public ResponseEntity<List<Requisition>> findRequisitions(
            @RequestParam(value = "stageId", required = false) List<Long> stageIds,
            @RequestParam(value = "projectId", required = false) List<Long> projectIds,
            @RequestParam(value = "requesterId ", required = false) List<Long> requesterIds,
            @RequestParam(value = "approvalStatus", required = false) ApprovalStatus approvalStatus,
            @RequestParam(value = "submissionDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate submissionDate
            ){
        try{
            LOGGER.info("GET /v1/requisitions {}", Thread.currentThread().getStackTrace()[1].getMethodName());
            return new ResponseEntity<>(requisitionBO.findRequisitions(stageIds, projectIds, requesterIds, approvalStatus, submissionDate), HttpStatus.OK);
        } catch (RequisitionExecutionException ex){
            LOGGER.error("error executing findRequisitions - GENERAL in handler", ex);
            return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "deleteRequisition", nickname = "delete",
            notes = "to delete a single requisitions by Id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS", response = String.class),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR", response = String.class)
    })
    @DeleteMapping("/{requisitionId}")
    public ResponseEntity<String> deleteRequisition(@PathVariable("requisitionId") Long requisitionId){
        try{
            LOGGER.info("DELETE /v1/requisitions in {}", Thread.currentThread().getStackTrace()[1].getMethodName());
            return new ResponseEntity<>(requisitionBO.deleteRequisition(requisitionId), HttpStatus.OK);
        } catch (RequisitionExecutionException ex){
            LOGGER.error("error executing deleteRequisition in handler", ex);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiOperation(value = "updateRequisition", nickname = "update",
            notes = "to update a requisitions")
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS", response = Requisition.class),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR", response = String.class)
    })
    @PutMapping
    public ResponseEntity<Requisition> updateRequisition(@RequestBody Requisition requisition){
        try{
            LOGGER.info("PUT /v1/requisitions in {}", Thread.currentThread().getStackTrace()[1].getMethodName());
            return new ResponseEntity<>(requisitionBO.updateRequisition(requisition), HttpStatus.OK);
        } catch (RequisitionExecutionException ex){
            LOGGER.error("error updateRequisition in handler", ex);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
