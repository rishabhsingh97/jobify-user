package com.rsuniverse.jobify_user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaRepositories
@EnableCaching
public class JobifyUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(JobifyUserApplication.class, args);
    }

}