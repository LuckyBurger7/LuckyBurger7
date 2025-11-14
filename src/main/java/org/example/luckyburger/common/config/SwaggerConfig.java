package org.example.luckyburger.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        //Api 호출에 적용할 요구사항
        SecurityRequirement accessTokenRequirement = new SecurityRequirement().addList("accessToken");

        return new OpenAPI()
                .info(new Info()
                        .title("LuckyBurger API 문서")
                        .description("럭키버거 백엔드 REST API 명세서입니다.")
                        .version("v2"))
                .components(
                        new Components()
                                .addSecuritySchemes("accessToken",
                                        new SecurityScheme()
                                                .name("accessToken")
                                                .type(SecurityScheme.Type.APIKEY)
                                                .in(SecurityScheme.In.HEADER)
                                                .bearerFormat("JWT")
                                )
                )
                .addSecurityItem(accessTokenRequirement);
    }
}
