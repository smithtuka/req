package com.galbern.req.security;

import com.galbern.req.jpa.dao.UserCredentialsDao;
import com.galbern.req.jpa.entities.UserCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    public static Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private UserCredentialsDao userRepository;


    public UserDetailsServiceImpl(UserCredentialsDao userRepository) {

        this.userRepository = userRepository;

    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOGGER.info("user sign in :: {}",username);

            try {
                UserCredentials user = userRepository.findById(username).get();
                LOGGER.info("fetched :: {}", user.toString());
                return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), Collections.emptyList());
            } catch (Exception ex) {
                LOGGER.error("USER NOT FOUND :: {}", username, ex);
                throw new UsernameNotFoundException("User not found with username: " + username);
            }

    }



//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        LOGGER.info("SIGN IN ATTEMPT :: {}",username);
//        Optional<UserCredentials> user = userRepository.findByUsername(username);
//        user.orElseThrow(() -> {
//            LOGGER.error("SIGN IN FAILURE :: {}", username);
//           return new UsernameNotFoundException("User not found " + username);});
//        return user.map(MyUserDetails::new).get();
//    }

}