package com.adamsimon.ticket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.adamsimon.ticket")
@EntityScan(basePackages = {"com.adamsimon.ticket"})
@EnableJpaRepositories(basePackages = {"com.adamsimon.ticket"})
public class TestConfigurationTicketBoot {

    public static void main(String[] args) {
        SpringApplication.run(TestConfigurationTicketBoot.class, args);
    }
}
