package be.anouchka.exercise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WordgamesApplication {

    public static void main(String[] args) {
        SpringApplication.run(WordgamesApplication.class, args);
    }
}
