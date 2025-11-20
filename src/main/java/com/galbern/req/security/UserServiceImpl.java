package com.galbern.req.security;

import com.galbern.req.exception.InvalidUserNameException;
import com.galbern.req.exception.UsersAlreadyExistsException;
import com.galbern.req.jpa.dao.UserCredentialsDao;
import com.galbern.req.jpa.entities.Role;
import com.galbern.req.jpa.entities.UserCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserCredentialsDao userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public String signUp(UserCredentials user) throws UsersAlreadyExistsException {
        if (!userRepository.findByUsername(user.getUsername()).isPresent()) {
            user.setActive(true);
            user.setRole(user.getRole());
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return jwtUtil.generateToken(user.getUsername());
        } else {
            throw new UsersAlreadyExistsException("Invalid username/password supplied");
        }
    }

    public List<UserCredentials> getAllUser() {
        return new ArrayList<>(userRepository.findAll());
    }

    @Override
    public String signIn(UserCredentials authCredentials) throws InvalidUserNameException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authCredentials.getUsername(),
                    authCredentials.getPassword()));
            return jwtUtil.generateToken(authCredentials.getUsername());
        } catch (BadCredentialsException e) {
            throw new InvalidUserNameException("Invalid user name or password");
        }

    }
}

