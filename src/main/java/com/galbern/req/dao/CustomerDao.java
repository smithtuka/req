package com.galbern.req.dao;

import com.galbern.req.domain.Customer;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerDao extends PagingAndSortingRepository<Customer, Long> {
}
