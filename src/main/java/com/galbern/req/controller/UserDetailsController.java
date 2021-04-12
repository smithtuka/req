package com.galbern.req.controller;

import com.galbern.req.jpa.dao.UserCredentialsDao;
import com.galbern.req.jpa.entities.User;
import com.galbern.req.jpa.entities.UserCredentials;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

  
@Api(value = "controller for RMS service version v1", tags={"RMSV1UserController"})
@RestController
@RequestMapping("/v1/users")
public class UserDetailsController {

    @Autowired
    private UserCredentialsDao userCredentialsDao;

    public static Logger  LOGGER = LoggerFactory.getLogger(com.galbern.req.controller.UserDetailsController.class);


    @ApiOperation(value = "ping", nickname = "ping", notes = "to ping")
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS", response = String.class),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR", response = String.class)
    })
    @GetMapping("/ping")
    public String ping(@RequestParam(value = "echo", required = false) String echo) {
        LOGGER.info("GET /v1/users in {}", Thread.currentThread().getStackTrace()[1].getMethodName());
        return "ping pong!\n\t" + echo;
    }


    @ApiOperation(value = "createUser", nickname = "createAUser", notes = "use to create a User")
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS", response = User.class),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR", response = User.class)
    })
    @PostMapping
    public @ResponseBody
    ResponseEntity<UserCredentials> createUser(@RequestBody UserCredentials user) {

        try {
            LOGGER.info("POST /v1/items in {}", Thread.currentThread().getStackTrace()[1].getMethodName());
            var userDetails = userCredentialsDao.save(user);
            return new ResponseEntity<>(userDetails, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("error - failed to create an User", e);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ApiOperation(value = "findUsers", nickname = "GetUser", notes = "use to find all Users")
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS", response = User.class),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR", response = User.class)
    })
    @GetMapping
    public @ResponseBody
    ResponseEntity<List<UserCredentials>> findUser() {

        try {
            LOGGER.info("Get /v1/user in {}", Thread.currentThread().getStackTrace()[1].getMethodName());
            return new ResponseEntity<>(userCredentialsDao.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("error - failed to find User details", e);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

    }



    @ApiOperation(value = "findUserByUsername", nickname = "findUserByUsername", notes = "use to find a User")
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS", response = User.class),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR", response = User.class)
    })
    @GetMapping("/{username}")
    public @ResponseBody
    ResponseEntity<UserCredentials> findUserByUsername(@PathVariable("username") String userName) {
        try {
            LOGGER.info("Get /v1/user by username in {}", Thread.currentThread().getStackTrace()[1].getMethodName());
            return new ResponseEntity<>(userCredentialsDao.findById(userName).get(), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("error - failed to find a single UserDetails", e);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
