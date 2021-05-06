package com.galbern.req.controller;

import com.galbern.req.schedulers.StageBudgetMonitor;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api
public class StageBudgetSchedulerController {

    public static Logger LOGGER = LoggerFactory.getLogger(StageBudgetSchedulerController.class);

    @Autowired
    private StageBudgetMonitor stageBudgetMonitor;

    @GetMapping("/budget")
    public String executeScheduler(){
        try {
            long startTime = System.currentTimeMillis();
            stageBudgetMonitor.checkStageBudgetStatus();
            return "Successfully executed in "+ (System.currentTimeMillis()-startTime);
        } catch (Exception exception){
            LOGGER.error(" error executing StageBudgetMonitorScheduler ", exception);
            return "failed scheduler";
        }
    }
}
