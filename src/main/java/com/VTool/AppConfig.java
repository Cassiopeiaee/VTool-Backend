package com.VTool;

import org.springframework.web.client.RestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restRestTemplate() {
        return new RestTemplate();
    }
}
