package com.galbern.req.service;

import com.galbern.req.domain.Project;

import java.util.Set;

public interface ProjectService {
    public Project addProject(Project p);
    public Project findProject(Long id);
    public Boolean deleteProject(Project p);
    public Set<Project> viewProjects();
}
