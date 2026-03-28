package ru.CarDealership;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CarDealershipApplication {
    public static void main(String[] args) {
        SpringApplication.run(CarDealershipApplication.class, args);
    }
}