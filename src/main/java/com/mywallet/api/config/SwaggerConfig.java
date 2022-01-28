package com.mywallet.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    //private static final String BASIC_AUTH = "basicAuth";
    private static final String BEARER_AUTH = "Bearer";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.mywallet.api"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .securitySchemes(securitySchemes())
                .securityContexts(Arrays.asList(securityContext()));
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "My Wallet REST API",
                "Product API to perform CRUD operations",
                "2.0.7",
                null,//"Terms of service",
                new Contact(
                        "Mu'ti C Putro",
                        "https://github.com/MCPutro",
                        "em.chepe@gmail.com"),
                null,//"License of API",
                null,//"API license URL",
                Collections.emptyList()
        );
    }

    private List<SecurityScheme> securitySchemes() {
        return Arrays.asList(
                //new BasicAuth(BASIC_AUTH),
                new ApiKey(BEARER_AUTH, "Authorization", "header")
        );
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(
                Arrays.asList(
//                        basicAuthReference(),
                        bearerAuthReference()
                ))
                .forPaths(
                        PathSelectors.regex("^(?!.\\/api\\/user\\/)(?!.*(createAndLogin|signup|signin|refresh\\-token)).+\\/.*")//ant("/api/**")
                ).build();
    }

//    private SecurityReference basicAuthReference() {
//        return new SecurityReference(
//                BASIC_AUTH,
//                new AuthorizationScope[0]
//        );
//    }

    private SecurityReference bearerAuthReference() {
        return new SecurityReference(BEARER_AUTH, new AuthorizationScope[0]);
    }


}
