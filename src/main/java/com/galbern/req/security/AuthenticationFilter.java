//package com.galbern.req.security;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.galbern.req.jpa.dao.UserCredentialsDao;
//import com.galbern.req.jpa.dao.UserDao;
//import com.galbern.req.jpa.entities.UserCredentials;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.FilterChain;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Date;
//
//import static java.lang.String.format;
//
//
//@Component
//public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//
//    @Autowired
//    private UserCredentialsDao userCredentialsDao;
//
//        @Autowired
//        @Qualifier("authenticationManager")
//    private AuthenticationManager authenticationManager;
//    private final String jwtSecret = "zdtlD3JK56m6wTTgsNFhqzjqP";
//    private final String jwtIssuer = "com.galbern";
//
//    public AuthenticationFilter(AuthenticationManager authenticationManager) {
//        this.authenticationManager = authenticationManager;
//        setFilterProcessesUrl("/user/login");
//    }
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest req,
//                                                HttpServletResponse res) throws AuthenticationException {
//        try {
//            UserCredentials creds = new ObjectMapper()
//                    .readValue(req.getInputStream(), UserCredentials.class);
//
//            return authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            creds.getUsername(),
//                            creds.getPassword(),
//                            new ArrayList<>())
//            );
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest req,
//                                            HttpServletResponse res,
//                                            FilterChain chain,
//                                            Authentication auth) throws IOException {
//
//        UserCredentials user = (UserCredentials) auth.getPrincipal(); // see Principal
//
//        String token = Jwts.builder()
//                .setSubject(format("%s", user.getUsername())) // add Id
//                .setIssuer(jwtIssuer)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)) // 1 week
//                .signWith(SignatureAlgorithm.HS512, jwtSecret)
//                .compact();
//
//        String body = ((User) auth.getPrincipal()).getUsername() + " " + token;
//
//        res.getWriter().write(body);
//        res.getWriter().flush();
//    }
//
//
//    public  String generateJwtToken(UserDetails userDetails) {
//        String userName = null!=userDetails ? userDetails.getUsername() : "breash"; // hack
//        UserCredentials user = userCredentialsDao.findById(userName).get();
//        String token = Jwts.builder()
//                .setSubject(format("%s", user.getUsername())) // add Id
//                .setIssuer(jwtIssuer)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)) // 1 week
//                .signWith(SignatureAlgorithm.HS512, jwtSecret)
//                .compact();
//        return token;
//    }
//
//}