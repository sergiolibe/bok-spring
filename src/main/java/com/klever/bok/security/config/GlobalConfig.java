package com.klever.bok.security.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GlobalConfig {

    @Bean
    ModelMapper globalModelMapper(){
        return new ModelMapper();
    }
}
