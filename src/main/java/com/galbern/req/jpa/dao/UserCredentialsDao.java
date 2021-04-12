package com.galbern.req.jpa.dao;

import com.galbern.req.jpa.entities.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCredentialsDao extends JpaRepository<UserCredentials,String> {
}
