package com.example.springSecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class DemoApplication {
    // https://github.com/jwtk/jjwt
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
