package org.example.luckyburger.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        Server server = new Server();
        server.setUrl("https://luckyburger.shop");

        return new OpenAPI()
                .info(new Info()
                        .title("LuckyBurger API 문서")
                        .description("럭키버거 백엔드 REST API 명세서입니다.")
                        .version("v2"))
                .components(
                        new Components()
                                .addSecuritySchemes("accessToken",
                                        new SecurityScheme()
                                                .name("Authorization")
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                )
                .addSecurityItem(new SecurityRequirement().addList("accessToken"))
                .servers(List.of(server));
    }
}
