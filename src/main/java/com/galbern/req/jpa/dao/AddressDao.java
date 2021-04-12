package com.galbern.req.jpa.dao;

import com.galbern.req.jpa.entities.Address;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressDao extends PagingAndSortingRepository<Address, Long> {
}
