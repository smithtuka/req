package com.galbern.req.service.BO;

import com.galbern.req.jpa.entities.Project;
import com.galbern.req.service.ProjectService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ProjectServiceBO implements ProjectService {
    @Override
    public Project addProject(Project p) {
        return null;
    }

    @Override
    public Project findProject(Long id) {
        return null;
    }

    @Override
    public Boolean deleteProject(Project p) {
        return null;
    }

    @Override
    public Set<Project> viewProjects() {
        return null;
    }
}
