package com.jayway.serviceregistry.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.config.EnableHypermediaSupport;

import static org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType.HAL;

@Configuration
@ComponentScan("com.jayway.serviceregistry")
@EnableHypermediaSupport(type = HAL)
@EnableAutoConfiguration
@Import({MongoConfiguration.class, RabbitMQConfiguration.class, SecurityConfig.class})
public class ServiceRegistryStart {

    public static void main(String[] args) {
        SpringApplication.run(ServiceRegistryStart.class, args);
    }
}