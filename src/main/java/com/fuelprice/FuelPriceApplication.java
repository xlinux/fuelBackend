package com.fuelprice;

import com.fuelprice.config.MimitProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(MimitProperties.class)
public class FuelPriceApplication {
    public static void main(String[] args) {
        SpringApplication.run(FuelPriceApplication.class, args);
    }
}
