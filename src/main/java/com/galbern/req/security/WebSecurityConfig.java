package com.galbern.req.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
//@Builder
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    private static final String[] AUTH_WHITELIST = {

            "/v1/api-docs",

            "/swagger-resources",

            "/swagger-resources/**",

            "/configuration/ui",

            "/configuration/security",

            "/swagger-ui.html",

            "/webjars/**",

            "/v1/users/",
            "/v1/users/user"

    };


//    public WebSecurityConfig(UserDetailsService userDetailsService, PasswordEncoder bCryptPasswordEncoder) {
//
//        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
//
//        this.userDetailsService = userDetailsService;
//
//    }


    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.cors().and().csrf().disable().authorizeRequests()

                .antMatchers(AUTH_WHITELIST).permitAll()
                .antMatchers("/v1/users/**").permitAll()
                .antMatchers(HttpMethod.GET, "/login").permitAll()
                .antMatchers(HttpMethod.GET, "/v1/requisitions/**").permitAll()
//                .anyRequest().authenticated()

                .and().addFilter(new AuthenticationFilter(authenticationManager()))

                .addFilter(new AuthorizationFilter(authenticationManager()))

                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }


    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {

        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);

    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());

        return source;

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}



