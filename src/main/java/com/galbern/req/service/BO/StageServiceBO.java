package com.galbern.req.service.BO;

import com.galbern.req.dao.StageDao;
import com.galbern.req.jpa.entities.Stage;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StageServiceBO {

    private static Logger LOGGER = LoggerFactory.getLogger(StageServiceBO.class);

    @Autowired
    private StageDao stageDao;

    public Stage createStage(Stage newSage){
        try{
            return stageDao.save(newSage);
        }catch (Exception e){
            LOGGER.error("[STAGE-CREATION-FAILURE] for {}", new Gson().toJson(newSage).replaceAll("[\r\n]+", ""));
            throw e;
        }
    }

    public List<Stage> findAStagesByProject(Long id){
        try{
            return (List<Stage>)stageDao.findStagesByProjectId(id);
        } catch (Exception e){
            LOGGER.error("[FETCH-STAGES-FAILURE] - error getting all stages by projectId - {}", id);
            throw e;
        }
    }

    public Stage findStageById(Long id){
        try{
            return stageDao.findById(id).get();
        } catch (Exception e){
            LOGGER.error("[FETCH-STAGE-FAILURE] - error getting all stages by stageId - {}", id);
            throw e;
        }
    }

    public Stage updateSTage(Stage stage){
        try{
            Stage s = stageDao.findById(stage.getId()).get();
            s.setBudget(stage.getBudget());
            s.setRequisitions(stage.getRequisitions());
            s.setActive(stage.getActive());
            s.setPlannedEndDate(stage.getPlannedEndDate());
            return stageDao.save(s);
        } catch (Exception e){
            LOGGER.error("[UPDATE-STAGE-FAILURE] - error updating  stage - {} for {}", stage.getId(), stage.getProject().getName());
            throw e;
        }
    }

    public void deleteStage(Long id) {
        try {
            stageDao.deleteById(id);
        } catch (Exception e) {
            LOGGER.error("[DELETE-STAGE-FAILURE] - error deleting  stage - {}", id);
            throw e;
        }
    }

}
