package com.galbern.req.dao;

import com.galbern.req.jpa.entities.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends PagingAndSortingRepository<User,Long> {
}
