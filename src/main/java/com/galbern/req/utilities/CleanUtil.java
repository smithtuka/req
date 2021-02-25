package com.galbern.req.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

public class CleanUtil {
    public static Logger LOGGER = LoggerFactory.getLogger(CleanUtil.class);
    @Scheduled(cron = "0 * 23 * * ?")
    public void cleanResources(){
        // all clean up here
        LOGGER.info("************---------CLEANING SERVICES KICKED OFF---------------***************");
    }
}
