package com.example.smarterbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(SmarterConfigurationProperties.class)
public class SmarterBackendApplication {
  public static void main(String[] args) {
    SpringApplication.run(SmarterBackendApplication.class, args);
  }
}
