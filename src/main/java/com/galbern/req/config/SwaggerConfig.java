package com.galbern.req.config;

import com.galbern.req.constants.RmsConstants;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI baseOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(RmsConstants.APP_NAME)
                        .description("Modernized API surface for requisitions")
                        .version("v1")
                        .license(new License().name("Apache-2.0")));
    }

    /**
     * Keep the legacy root redirect handy for quick swagger-ui access.
     */
    @RestController
    static class SwaggerRedirect {
        @GetMapping({"/", "/api"})
        public ModelAndView swaggerui() {
            return new ModelAndView("redirect:/swagger-ui/index.html");
        }
    }
}
