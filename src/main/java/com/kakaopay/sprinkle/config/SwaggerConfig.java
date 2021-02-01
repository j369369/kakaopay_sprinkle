package com.kakaopay.sprinkle.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket swaggerApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(swaggerInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("com.kakaopay.sprinkle.controller"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                ;
    }

    private ApiInfo swaggerInfo() {
        return new ApiInfoBuilder().title("Kakao pay Sprinkle API Documentation")
                .description("뿌리기 API에 대한 문서입니다. (생성 / 받기 / 조회를 테스트 해보실수 있습니다.)")
                .build();
    }
    @Bean
    public UiConfiguration uiConfig() {
        return new UiConfiguration(
                null, "full", "alpha", "schema", UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS, false, true, null
        );
    }

}