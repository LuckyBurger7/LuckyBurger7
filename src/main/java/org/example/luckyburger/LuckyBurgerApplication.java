package org.example.luckyburger;

import org.redisson.spring.starter.RedissonAutoConfigurationV2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {RedissonAutoConfigurationV2.class})
public class LuckyBurgerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LuckyBurgerApplication.class, args);
    }

}
