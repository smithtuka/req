package com.galbern.req.jpa.dao;

import com.galbern.req.jpa.entities.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
public interface UserDao extends PagingAndSortingRepository<User,Long> {
    User findByEmail(String email);

    Optional<User> findUserByLastName(String userName);
    Optional<User> findUserByUserName(String userName);

}
