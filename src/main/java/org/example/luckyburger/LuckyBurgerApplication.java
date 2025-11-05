package org.example.luckyburger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class LuckyBurgerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LuckyBurgerApplication.class, args);
    }

}
