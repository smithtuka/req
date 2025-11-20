package com.galbern.req.security;

import com.galbern.req.jpa.dao.UserCredentialsDao;
import com.galbern.req.jpa.entities.Role;
import com.galbern.req.jpa.entities.UserCredentials;
import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.List.of;
import static java.util.Optional.ofNullable;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    public static Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private UserCredentialsDao userRepository;
    @Autowired
    private SpringRdsSupport springRdsSupport;


    public UserDetailsServiceImpl(UserCredentialsDao userRepository) {

        this.userRepository = userRepository;

    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOGGER.info("user sign in :: {}", username);

        try {
            UserCredentials user;
            // jpa
//            user = userRepository.findById(username).get();
            // jdbc
            user = new UserCredentials();
            Connection conn = springRdsSupport.getRemoteConnection();
            PreparedStatement stmt = conn.prepareStatement("select username, password, role, active from public.usercredentials where username=?", ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, username);
            LOGGER.info("QUERY MetaData:: {}", stmt.getMetaData());
            ResultSet resultSet = stmt.executeQuery();
            conn.close();
            while (resultSet.next()) {
                String userNameRetrieved = resultSet.getString("username");
                String pass = resultSet.getString("password");
//                Object role = resultSet.getObject("role");
                LOGGER.info("RESULTS: USERNAME :{} PASSWORD {} ", userNameRetrieved, pass);
                user.setUsername(userNameRetrieved);
                user.setPassword(pass);
//            user.setRole((Role)role);
            }
            LOGGER.info("CREDENTIALS-FETCHED  :: {}", user.toString());
//            Roles roles = new Roles();
//            roles.setAuthority(ofNullable(user.getRole().toString()).orElse(""));
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),Collections.emptyList()); //List.of(roles)
        } catch (Exception ex) {
            LOGGER.error("USER NOT FOUND :: {}", username, ex);
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

    }

}