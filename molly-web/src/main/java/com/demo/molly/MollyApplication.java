package com.demo.molly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MollyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MollyApplication.class, args);
    }
}
