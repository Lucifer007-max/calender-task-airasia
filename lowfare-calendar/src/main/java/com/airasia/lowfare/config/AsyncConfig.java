package com.airasia.lowfare.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AsyncConfig {

    @Bean
    public Executor executor(){

        ThreadPoolTaskExecutor e = new ThreadPoolTaskExecutor();

        e.setCorePoolSize(50);

        e.setMaxPoolSize(200);

        e.setQueueCapacity(1000);

        e.initialize();

        return e;

    }

}