package com.galbern.req.service.BO;

import com.galbern.req.dao.ProjectDao;
import com.galbern.req.jpa.entities.Item;
import com.galbern.req.jpa.entities.Project;
import com.galbern.req.service.ProjectService;
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
import java.util.Set;

@Service
public class ProjectServiceBO {
    private static Logger LOGGER = LoggerFactory.getLogger(ProjectServiceBO.class);
    @Autowired
    private ProjectDao projectDao;

    @Retryable(value = {DataAccessResourceFailureException.class, TransactionSystemException.class, CannotCreateTransactionException.class},
            maxAttempts = 2, backoff = @Backoff(delay = 500))
    public Project createProject(Project project) {
        try {
            return projectDao.save(project);
        } catch (Exception e) {
            LOGGER.error("[CREATE-PROJECT-FAILURE]- failed to save an project : {}", new Gson().toJson(project).replaceAll("[\n\r]+", ""));
            throw e;
        }
    }


    @Retryable(value = {DataAccessResourceFailureException.class, TransactionSystemException.class, CannotCreateTransactionException.class},
            maxAttempts = 2, backoff = @Backoff(delay = 500))
    public Project findById(Long id) {
        try {
            return projectDao.findById(id).get();
        } catch (Exception e) {
            LOGGER.error("[FIND-PROJECT-FAILURE]- could not  FIND project: {}", id);
            throw e;
        }
    }


    @Retryable(value = {DataAccessResourceFailureException.class, TransactionSystemException.class, CannotCreateTransactionException.class},
            maxAttempts = 2, backoff = @Backoff(delay = 500))

    public Project updateProject(Project project) {
        try {
            Project proj = projectDao.findById(project.getId()).get();
            proj.setApprovers(project.getApprovers());
            proj.setCustomer(project.getCustomer());
            proj.setActive(project.getActive());
            proj.setName(project.getName());
            return projectDao.save(proj);
        } catch (Exception e) {
            LOGGER.error("[PROJECT-UPDATE-FAILURE] - failed to updated {}", project.getName());
            throw e;
        }
    }


    @Retryable(value = {DataAccessResourceFailureException.class, TransactionSystemException.class, CannotCreateTransactionException.class},
            maxAttempts = 2, backoff = @Backoff(delay = 500))
    public List<Project> findAllProjects() {
        List<Project> projects;
        try {
            projects = (List<Project>) projectDao.findAll();
        } catch (Exception e) {
            LOGGER.error("[FIND-PROJECTS-FAILURE]- could not find projects");
            throw e;
        }
        return projects;

    }

    @Retryable(value = {DataAccessResourceFailureException.class, TransactionSystemException.class, CannotCreateTransactionException.class},
            maxAttempts = 2, backoff = @Backoff(delay = 500))
    public void deleteProject(Long id) {
        try {
            projectDao.deleteById(id);
        } catch (Exception e) {
            LOGGER.error("[DELETE-PROJECT-FAILURE]- could not  DELETE project: {}", id);
            throw e;
        }
    }


}
