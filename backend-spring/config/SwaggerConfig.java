package com.board.one_more_project.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * [Swagger 설정 클래스]
 * @Configuration: 이 클래스가 스프링의 설정 파일임을 알려줍니다.
 */
@Configuration
public class SwaggerConfig {

    /**
     * OpenAPI 객체를 스프링 빈(Bean)으로 등록합니다.
     * 이 설정이 있어야 Swagger UI에서 우리 프로젝트의 정보를 예쁘게 보여줍니다.
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("One More Project - AI 레시피 서비스 API") // 문서 제목
                        .description("사용자의 식재료 사진이나 영수증을 분석하여 AI 레시피를 제공하는 서비스의 API 문서입니다.") // 상세 설명
                        .version("1.0.0")); // API 버전
    }
}