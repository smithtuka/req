package com.galbern.req.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class Roles implements GrantedAuthority {

        public static final String USER = "USER";
        public static final String ADMIN = "ADMIN";
        public static final String USER_ADMIN = "USER_ADMIN";
        public static final String SUPER = "SUPER_ADMIN";


        private String authority;

    }
