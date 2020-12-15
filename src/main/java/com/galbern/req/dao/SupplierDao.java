package com.galbern.req.dao;

import com.galbern.req.domain.Supplier;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierDao extends PagingAndSortingRepository<Supplier, Long> {
}
