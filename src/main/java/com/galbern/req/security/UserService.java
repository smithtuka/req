package com.galbern.req.security;

import com.galbern.req.exception.InvalidUserNameException;
import com.galbern.req.jpa.entities.UserCredentials;

public interface UserService {

    String signIn(UserCredentials authCredentials) throws InvalidUserNameException;
}
