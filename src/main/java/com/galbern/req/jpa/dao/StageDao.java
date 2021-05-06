package com.galbern.req.jpa.dao;

import com.galbern.req.jpa.entities.Stage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StageDao extends JpaRepository<Stage, Long> {
    List<Stage> findStagesByProjectId(Long id);

    List<Stage> findStagesByIsActive(Boolean isActive);
}
