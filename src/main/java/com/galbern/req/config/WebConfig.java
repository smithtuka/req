package com.galbern.req.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module; //
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import java.util.List;
//// HAOM -- HIBERNATE4MODULE // actually 5 jar used here
//
@Configuration
//@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
//
//    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
//        for (HttpMessageConverter converter : converters) {
//            if (converter instanceof org.springframework.http.converter.json.MappingJackson2HttpMessageConverter) {
//                ObjectMapper mapper = ((MappingJackson2HttpMessageConverter) converter).getObjectMapper();
//                mapper.registerModule(new Hibernate6Module());
//                // replace Hibernate4Module() with the proper class for your hibernate version.
//            }
//        }
//    }

        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**");
        }
}


// configured with @Bean Hibernate6Module in Application class
