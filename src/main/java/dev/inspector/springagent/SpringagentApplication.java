package dev.inspector.springagent;

import dev.inspector.springagent.entity.User;
import dev.inspector.springagent.repository.TestRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableScheduling
public class SpringagentApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(SpringagentApplication.class, args);
    }

    /*@Bean
    CommandLineRunner commandLineRunner(TestRepository testRepository) {
        return args -> {
            User user = new User(1L, "Gianni");
            testRepository.save(user);
        };
    }*/

}
