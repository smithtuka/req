package com.galbern.req.jpa.dao;

import com.galbern.req.jpa.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemDao  extends JpaRepository<Item, Long> {

    List<Item> findItemsByRequisitionId(Long requisitionId);

    List<Item> findItemsByRequisitionRequesterId(Long requesterId);

    List<Item> findItemsByRequisitionStageProjectId(Long projectId);

    List<Item> findItemsByRequisitionStageId(Long stageId);
}
