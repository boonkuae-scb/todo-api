package com.bk.todos.template;

import org.springframework.context.annotation.Bean;

class RestTemplate{

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}