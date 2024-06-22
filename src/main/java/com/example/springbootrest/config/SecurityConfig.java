package com.example.springbootrest.config;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Configuration
public class SecurityConfig {

    @Autowired
    private RestTemplate restTemplate;

    @Bean
    public UserDetailsService userDetailsService() {
        String url = "https://dummyjson.com/users";
        ResponseEntity<JsonNode> responseEntity = restTemplate.getForEntity(url, JsonNode.class);
        JsonNode usersNode = Objects.requireNonNull(responseEntity.getBody()).get("users");

        List<UserDetails> users = new ArrayList<>();

        usersNode.forEach(userNode -> {
            String username = userNode.get("username").asText();
            String password = userNode.get("password").asText();
            String role = userNode.get("role").asText().toUpperCase();

            UserDetails user = User.withUsername(username)
                    .password(passwordEncoder().encode(password))
                    .roles(role)
                    .build();
            users.add(user);
        });

        return new InMemoryUserDetailsManager(users);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.requestMatchers("/").hasRole("USER")
                        .anyRequest().authenticated())
                .formLogin(Customizer.withDefaults())
                .logout(LogoutConfigurer::permitAll);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
