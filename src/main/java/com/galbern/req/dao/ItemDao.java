package com.galbern.req.dao;

import com.galbern.req.domain.Item;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemDao  extends PagingAndSortingRepository<Item, Long> {

}
