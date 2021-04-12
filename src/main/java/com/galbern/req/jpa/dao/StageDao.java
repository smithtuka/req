package com.galbern.req.jpa.dao;

import com.galbern.req.jpa.entities.Stage;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StageDao extends PagingAndSortingRepository<Stage, Long> {
    List<Stage> findStagesByProjectId(Long id);
}
