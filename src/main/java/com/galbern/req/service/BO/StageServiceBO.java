package com.galbern.req.service.BO;

import com.galbern.req.jpa.dao.StageDao;
import com.galbern.req.jpa.entities.Stage;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.TransactionSystemException;

import java.util.List;

@Service
public class StageServiceBO {

    private static Logger LOGGER = LoggerFactory.getLogger(StageServiceBO.class);

    @Autowired
    private StageDao stageDao;

    @Retryable(value = {DataAccessResourceFailureException.class, TransactionSystemException.class, CannotCreateTransactionException.class},
            maxAttempts = 2, backoff = @Backoff(delay = 500))
    public Stage createStage(Stage newSage) {
        try {
            return stageDao.save(newSage);
        } catch (Exception e) {
            LOGGER.error("[STAGE-CREATION-FAILURE] for {}", new Gson().toJson(newSage).replaceAll("[\r\n]+", ""));
            throw e;
        }
    }

    @Retryable(value = {DataAccessResourceFailureException.class, TransactionSystemException.class, CannotCreateTransactionException.class},
            maxAttempts = 2, backoff = @Backoff(delay = 500))
    public List<Stage> findStagesByProjectId(Long id) {
        try {
            return stageDao.findStagesByProjectId(id);
        } catch (Exception e) {
            LOGGER.error("[FETCH-STAGES-FAILURE] - error getting all stages by projectId - {}", id);
            throw e;
        }
    }

    @Retryable(value = {DataAccessResourceFailureException.class, TransactionSystemException.class, CannotCreateTransactionException.class},
            maxAttempts = 2, backoff = @Backoff(delay = 500))
    public Stage findById(Long id) {
        try {
            return stageDao.findById(id).get();
        } catch (Exception e) {
            LOGGER.error("[FETCH-STAGE-FAILURE] - error getting all stages by stageId - {}", id);
            throw e;
        }
    }

    @Retryable(value = {DataAccessResourceFailureException.class, TransactionSystemException.class, CannotCreateTransactionException.class},
            maxAttempts = 2, backoff = @Backoff(delay = 500))
    public Stage updateStage(Stage stage) {
        try {
            Stage s = stageDao.findById(stage.getId()).get();
            s.setBudget(stage.getBudget());
            s.setRequisitions(stage.getRequisitions());
            s.setActive(stage.getActive());
            s.setPlannedEndDate(stage.getPlannedEndDate());
            return stageDao.save(s);
        } catch (Exception e) {
            LOGGER.error("[UPDATE-STAGE-FAILURE] - error updating  stage - {} for {}", stage.getId(), stage.getProject().getName());
            throw e;
        }
    }

    @Retryable(value = {DataAccessResourceFailureException.class, TransactionSystemException.class, CannotCreateTransactionException.class},
            maxAttempts = 2, backoff = @Backoff(delay = 500))
    public void deleteStage(Long id) {
        try {
            stageDao.deleteById(id);
        } catch (Exception e) {
            LOGGER.error("[DELETE-STAGE-FAILURE] - error deleting  stage - {}", id);
            throw e;
        }
    }

}
