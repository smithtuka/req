package com.galbern.req.security;

import com.galbern.req.constants.RmsConstants;
import com.galbern.req.jpa.dao.UserCredentialsDao;
import com.galbern.req.jpa.entities.Item;
import com.galbern.req.jpa.entities.UserCredentials;
import com.galbern.req.utilities.ExcelUtil;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import static java.lang.String.format;

@RestController
@RequestMapping("/api/public")
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
    public ResponseEntity<List<UserCredentials>> fetchCredentials() {
        return new ResponseEntity<>(userCredentialsDao.findAll(), HttpStatus.OK);
    }

    @ApiOperation(value = "createUserCredentials", nickname = "createCredentials", notes = "used to create creds")
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS", response = Item.class),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR", response = UserCredentials.class)
    })

    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public UserCredentials adduser(@RequestBody UserCredentials userCredentials) {
        Roles roles = new Roles();
        try {
            if (RmsConstants.signUpFeatureFlag) {
                userCredentials.setPassword(bCryptPasswordEncoder.encode(userCredentials.getPassword()));
                UserCredentials user = userCredentialsDao.save(userCredentials); // change password just
                roles.setAuthority(user.getRole());
                userCredentials.setPassword(bCryptPasswordEncoder.encode(userCredentials.getPassword()));

            }
        } catch (Exception exception) {
            LOGGER.error("Error Signing up:: ", exception);
        }
        return userCredentialsDao.save(userCredentials);
    }


    @PutMapping("/v1/reset")
    public ResponseEntity<String> createCredentials
            (@RequestParam("username") String userName,
             @RequestParam("password") String password,
             @RequestParam("active") boolean active,
             @RequestParam("role") String role)    throws Exception {

        LOGGER.info("RESET :: for {}", userName);

        Roles roles = new Roles();
        UserCredentials response = new UserCredentials();
        try {
            UserCredentials user = userCredentialsDao.findById(userName).orElseThrow(() -> new UsernameNotFoundException("userName not known"));
            user.setPassword(bCryptPasswordEncoder.encode(password));
            user.setActive(active);
            user.setRole(role);
            user = userCredentialsDao.save(user);
//            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),
//                    user.getPassword(), List.of(roles)));
            LOGGER.info("NEW CREDENTIALS :: {} :: DETAILS :: {}", user.toString()); // auth.getDetails().toString()
            response = user;
        } catch (
                DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (
                BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
        return ResponseEntity.ok(response.toString());
    }

    @ApiOperation(value = "get token", nickname = "token", notes = "used to create token")
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS", response = String.class),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR", response = UserCredentials.class)
    })
    @PostMapping("/v1/login")
    public ResponseEntity<String> getToken(@RequestBody UserCredentials authCredentials) {
        String userName = authCredentials.getUsername();
        try {
            final UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
            final String jwtToken = tokenManager.generateToken(userDetails.getUsername());
            return ResponseEntity.ok(jwtToken);
        } catch (Exception ex) {
            LOGGER.error(" failed for user attempt :: {}", userName, ex);
            throw ex;
        }

    }


}
