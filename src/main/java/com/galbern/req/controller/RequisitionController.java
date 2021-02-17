package com.galbern.req.controller;

import com.galbern.req.jpa.entities.ApprovalStatus;
import com.galbern.req.jpa.entities.Requisition;
import com.galbern.req.service.BO.RequisitionBO;
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
    public String ping(){
        return "ping";
    }

    @GetMapping("/{requisitionId}")
    public ResponseEntity<Requisition> findRequisitionById(@PathVariable("requisitionId") Long requisitionId){
        try{
            LOGGER.info("entering findRequisitionById in {}", this.getClass().getName());
            return new ResponseEntity<>(requisitionBO.findRequisitionById(requisitionId), HttpStatus.OK);
        } catch (RuntimeException ex){
            LOGGER.error("error executing findRequisitionById", ex);
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
            LOGGER.info("entering findRequisitions - GENERAL in {}", this.getClass().getName());
            return new ResponseEntity<>(requisitionBO.findRequisitions(stageIds, projectIds, requesterIds, approvalStatus, submissionDate), HttpStatus.OK);
        } catch (RuntimeException ex){
            LOGGER.error("error executing findRequisitions - GENERAL", ex);
            return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }





}
