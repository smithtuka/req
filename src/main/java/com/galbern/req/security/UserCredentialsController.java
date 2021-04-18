package com.galbern.req.security;

import com.galbern.req.jpa.dao.UserCredentialsDao;
import com.galbern.req.jpa.entities.Item;
import com.galbern.req.jpa.entities.UserCredentials;
import com.sun.mail.iap.Response;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import static java.lang.String.format;

@RestController
@RequestMapping("/credentials")
@Api("user credentials")
public class UserCredentialsController {

    private static Logger LOGGER = LoggerFactory.getLogger(UserCredentialsController.class);
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    @Qualifier("authenticationManager")
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTUtil tokenManager;

    @Autowired
    private UserCredentialsDao userCredentialsDao;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping
    public ResponseEntity<List<UserCredentials>> fetchCredentials(){
        return new ResponseEntity<>(userCredentialsDao.findAll(), HttpStatus.OK);
    }

    @ApiOperation(value = "createUserCredentials", nickname = "createCredentials", notes = "used to create creds")
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS", response = Item.class),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR", response = UserCredentials.class)
    })

    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public UserCredentials adduser(@RequestBody UserCredentials userCredentials){
        userCredentials.setPassword(bCryptPasswordEncoder.encode(userCredentials.getPassword()));
        return userCredentialsDao.save(userCredentials);
    }


    @PostMapping("/v1/signup")
    public ResponseEntity<String> createToken(@RequestBody UserCredentials
                                                request) throws Exception {
        try {
            authenticationManager.authenticate(
                    new
                            UsernamePasswordAuthenticationToken(request.getUsername(),
                            request.getPassword())
            );
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

        return ResponseEntity.ok(("Temporary"));
    }
    @ApiOperation(value = "get token", nickname = "token", notes = "used to create token")
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS", response = String.class),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR", response = UserCredentials.class)
    })
    @GetMapping("/v1/login")
    public ResponseEntity<String> getToken(@RequestBody UserCredentials authCredentials){
        String userName = authCredentials.getUsername();
        try {
            final UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                    authCredentials.getPassword()));
            final String jwtToken = tokenManager.generateToken(userName);
            return ResponseEntity.ok(jwtToken);
        } catch (Exception ex){
            LOGGER.error(" failed for user attempt :: {}", userName, ex );
            throw ex;
        }

    }


}
