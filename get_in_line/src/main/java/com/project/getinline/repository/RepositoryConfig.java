package com.project.getinline.repository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {

    @Bean
    public EventRepository eventRepository(){
        return new EventRepository() {
        };
    }
}