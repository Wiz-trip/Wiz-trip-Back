package com.wiztrip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing //Auditing 사용
public class WizTripBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(WizTripBackendApplication.class, args);
    }

}
