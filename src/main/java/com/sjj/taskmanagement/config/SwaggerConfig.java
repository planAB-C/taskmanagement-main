package com.sjj.taskmanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;


@Configuration
@EnableSwagger2
public class SwaggerConfig {
    private String apiName = "JWT";
    private String headerKey = "authorization";
    private HashSet<String> protocolSet = new HashSet<>();

    @Bean
    public Docket api() {
        protocolSet.add("https");
        protocolSet.add("http");
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sjj.taskmanagement.controller"))
                .paths(PathSelectors.any())
                .build()
                .protocols(protocolSet)
                .useDefaultResponseMessages(false)
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts())
                ;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("任务分配系统接口文档")
                .description("接口文档")
                .version("1.0")
                .build();
    }

    private List<SecurityScheme> securitySchemes() {
        ApiKey apiKey = new ApiKey(apiName, headerKey, "header");
        return Collections.singletonList(apiKey);
    }

    private List<SecurityContext> securityContexts() {
        List<SecurityContext> securityContexts = new ArrayList<>();
        securityContexts.add(
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .forPaths(PathSelectors.any())
                        .build());
        return securityContexts;
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> securityReferences = new ArrayList<>();
        securityReferences.add(new SecurityReference(apiName, authorizationScopes));
        return securityReferences;
    }
}
