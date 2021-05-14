package com.galbern.req.jpa.dao;

import com.galbern.req.jpa.entities.Project;
import com.galbern.req.jpa.entities.Stage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StageDao extends JpaRepository<Stage, Long> {
    List<Stage> findStagesByProjectId(Long id);

    List<Stage> findStagesByIsActive(Boolean isActive);

    @Query("SELECT s.project from Stage s where s.id = :id")
    Project getProject(@Param("id") Long id);
}
