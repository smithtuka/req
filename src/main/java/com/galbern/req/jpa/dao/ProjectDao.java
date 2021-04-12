package com.galbern.req.jpa.dao;

import com.galbern.req.jpa.entities.Project;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectDao extends PagingAndSortingRepository<Project, Long> {
}
