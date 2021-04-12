package com.galbern.req.security;

import com.galbern.req.jpa.dao.UserCredentialsDao;
import com.galbern.req.jpa.entities.UserCredentials;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserCredentialsDao userRepository;


    public UserDetailsServiceImpl(UserCredentialsDao userRepository) {

        this.userRepository = userRepository;

    }


    @Override

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if ("user".equals(username)) {
            return new User("user",
                    "$2a$10$xdruRph9m4o7upnWq7Mu8OXH/3jw8edKG/uc1vOCM.tZ06jGjwKpG",
                    new ArrayList<>());
        } else {

            try {
                UserCredentials user = userRepository.findById(username).get();
                return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), Collections.emptyList());
            } catch (Exception ex) {

                throw new UsernameNotFoundException("User not found with username: " + username);
            }

        }

    }

}