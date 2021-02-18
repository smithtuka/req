package com.galbern.req.config;

import com.galbern.req.constants.RmsConstants;
import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.ArrayList;

@Configuration
@RestController
@EnableSwagger2
public class SwaggerConfig {


    @Bean
    public Docket api() {

        //Adding Header
        ParameterBuilder aParameterBuilder = new ParameterBuilder();
        aParameterBuilder.name("USER")
                .defaultValue("{\"firstName\":\"Swagger\", \"lastName\":\"Testing\", \"ldap\":\"123456\"}")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .build();
        java.util.List<Parameter> aParameters = new ArrayList<>();
        aParameters.add(aParameterBuilder.build());

        Contact contact = new Contact(RmsConstants.APP_NAME, RmsConstants.URL, "info@galbern.com>");

        return new Docket(DocumentationType.SWAGGER_2)
                .directModelSubstitute(XMLGregorianCalendar.class, String.class)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))//RequestHandlerSelectors.any()
                .paths(PathSelectors.ant("/**")) // PathSelectors.any()
                .build()
                .apiInfo(
                        new ApiInfo(RmsConstants.APP_NAME, RmsConstants.APP_NAME, "v1", RmsConstants.URL, contact, "", "", new ArrayList<>()))
                .globalOperationParameters(aParameters);
    }

    @RequestMapping(value = {"/", "/api"}, method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView swaggerui() {
        return new ModelAndView("redirect:swagger-ui.html");
    }

}