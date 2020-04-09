package com.adamsimon.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.adamsimon.core")
@EntityScan(basePackages = {"com.adamsimon.core"})
@EnableJpaRepositories(basePackages = {"com.adamsimon.core"})
public class TestConfigurationCoreBoot {

    public static void main(String[] args) {
        SpringApplication.run(TestConfigurationCoreBoot.class, args);
    }
}
