package com.example.springbootrest.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
    // malo nepotrebno, mogli smo definirati samo restTemplate = new RestTemplate()
    // iako ovako je sve centralizirano u slučaju da dodajemo još Beanova

    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        return  objectMapper;
    }

    // ista stvar, moglo se direktno specificirati u kontroleru
}
