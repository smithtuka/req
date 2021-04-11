package com.galbern.req.security;

//import com.galbern.req.jpa.dao.UserCredentialsDao;
import com.galbern.req.jpa.entities.User;
import com.galbern.req.jpa.entities.UserCredentials;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component

public class UserDetailsServiceImpl implements UserDetailsService {

    private UserCredentialsDao userRepository;


    public UserDetailsServiceImpl(UserCredentialsDao userRepository) {

        this.userRepository = userRepository;

    }


    @Override

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

//        User user = userRepository.findByUserUserName(username);
        UserCredentials user = userRepository.findById(username);

        if (user == null) {

            throw new UsernameNotFoundException(username);

        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), Collections.emptyList());

    }

}