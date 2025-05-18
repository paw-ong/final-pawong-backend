package kr.co.pawong.pwbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PawongBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(PawongBackendApplication.class, args);
    }

}

