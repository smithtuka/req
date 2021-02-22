package com.galbern.req.dao;

import com.galbern.req.jpa.entities.Item;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemDao  extends PagingAndSortingRepository<Item, Long> {

    List<Item> findItemsByRequisitionId(Long requisitionId);

    List<Item> findItemsByRequisitionRequesterId(Long requesterId);

    List<Item> findItemsByRequisitionStageProjectId(Long projectId);

    List<Item> findItemsByRequisitionStageId(Long stageId);
}
