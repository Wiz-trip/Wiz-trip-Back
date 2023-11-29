package com.wiztrip.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger springdoc-ui 구성 파일
 */
@Configuration
public class OpenApiConfig {

//    @Bean
//    public GroupedOpenApi group1() {
//        return GroupedOpenApi.builder()
//                .group("1.ALL")
//                .pathsToMatch("/**")
//                // .packagesToScan("com.example.swagger") // package 필터 설정
//                .build();
//    }

    @Bean
    public OpenAPI openAPI() {

        Info info = new Info()
                .version("v1.0.0")
                .title("WIZ-TRIP API Document")
                .description("TAVE 후반기 하모예 팀의 프로젝트 WIZ-TRIP의 API 문서입니다.");

        /**
         * Spring Security 구현 후 주석 해제 필요
         */
//        // SecuritySecheme명
//        String jwtSchemeName = "JWT Token";
//        // API 요청헤더에 인증정보 포함
//        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);
//        // SecuritySchemes 등록
//        Components components = new Components()
//                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
//                        .name(jwtSchemeName)
//                        .type(SecurityScheme.Type.HTTP) // HTTP 방식
//                        .scheme("bearer"));
////                        .bearerFormat("JWT")); // 토큰 형식을 지정하는 임의의 문자(Optional)

        return new OpenAPI()
                .info(info);
//                .addSecurityItem(securityRequirement)
//                .components(components);
    }
}
