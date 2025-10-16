package org.example.luckyburger.common.config;

import org.example.luckyburger.common.security.properties.JwtSecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JwtSecurityProperties.class)
public class PropertiesConfig {
}
