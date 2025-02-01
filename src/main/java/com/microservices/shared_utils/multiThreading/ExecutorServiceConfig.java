package com.microservices.shared_utils.multiThreading;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ExecutorServiceConfig {

    @Value("${threads.allocated:2}")
    private Integer threadCount;

    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(threadCount);
    }

}

