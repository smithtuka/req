package com.galbern.req.sandbox;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

//@Repository
public interface SupplierDao extends PagingAndSortingRepository<Supplier, Long> {
}