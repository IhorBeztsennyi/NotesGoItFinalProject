package ua.goit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:application.properties")
public class NotesFinalProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotesFinalProjectApplication.class, args);
    }

}
