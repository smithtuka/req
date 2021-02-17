package com.galbern.req.dao;

import com.galbern.req.jpa.entities.Stage;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StageDao extends PagingAndSortingRepository<Stage, Long> {
}
