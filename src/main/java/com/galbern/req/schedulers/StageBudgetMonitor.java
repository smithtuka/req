package com.galbern.req.schedulers;

import com.galbern.req.jpa.dao.RequisitionDao;
import com.galbern.req.jpa.dao.StageDao;
import com.galbern.req.jpa.entities.ApprovalStatus;
import com.galbern.req.jpa.entities.Stage;
import com.galbern.req.service.BO.StageServiceBO;
import com.galbern.req.utilities.EmailUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
public class StageBudgetMonitor {

    public static Logger LOGGER = LoggerFactory.getLogger(StageBudgetMonitor.class);

    @Autowired
    private StageDao stageDao;

    @Autowired
    private RequisitionDao requisitionDao;

    @Autowired
    private StageServiceBO stageServiceBO;

    @Autowired
    private EmailUtils emailUtils;

    @Value("${default.email.recipient.list}")
    public String defaultEmailRecipients;

    @Scheduled(cron = "0 0/30 10 * * ?") // implement checks upon submission of fresh requisition
    public void checkStageBudgetStatus(){
        LOGGER.info("STAGE BUDGET STATUS CHECK SCHEDULER - STARTED");

        List<Stage> activeStages = stageServiceBO.findStagesByActive(true);

        for(Stage stg : activeStages) {
            BigDecimal stageRequisitionAmount = List.of(stg).parallelStream()
                    .flatMap(stage -> stage.getRequisitions().stream())
                    .filter(r->r.getApprovalStatus()== ApprovalStatus.APPROVED)
                    .flatMap(requisition -> requisition.getItems().stream())
                    .filter(item -> null != item.getPrice() && null != item.getQuantity())
                    .map(item ->  item.getQuantity().multiply(item.getPrice()))
                    .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
            LOGGER.info("Stage Id :: {} Budget :: {} Actual:: {} ", stg.getId(), stg.getBudget(), stageRequisitionAmount);

            if(stg.getBudget().subtract(stageRequisitionAmount).compareTo(BigDecimal.valueOf(1000000)) < 0){
                stg.setActive(false);
                stageDao.save(stg); // update with Project Name
                executeProcessStageClosure(stg, stageRequisitionAmount);
            }

        }
        LOGGER.info("STAGE BUDGET STATUS CHECK SCHEDULER - END");
    }

   public void executeProcessStageClosure(Stage stage, BigDecimal actualExpense){
       LOGGER.debug("executeProcessStageClosure- START");
       String message = String.format("Greetings," +
               "\nStage : %s.%s %s ran out of budget." +
               "\nBudget was UGX :: %s but UGX :: %s has so far been spent" +
               "\nIt will be auto - closed. Please review the stage Budget for revision!", stage.getId(), stage.getName(), stage.getName(), stage.getBudget(), actualExpense);
       emailUtils.sendByGmail("BUDGET ALERT", message, Arrays.asList(defaultEmailRecipients.split(",")), new File(""));
       LOGGER.debug("executeProcessStageClosure- END");
   }
}
