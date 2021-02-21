package com.galbern.req.controller;

import com.galbern.req.service.BO.StageServiceBO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/stages")
public class StageController {

    public static Logger LOGGER = LoggerFactory.getLogger(StageController.class);
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
}
