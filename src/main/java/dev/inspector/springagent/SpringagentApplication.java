package dev.inspector.springagent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@SpringBootApplication
@EnableScheduling
//@EnableJms
public class SpringagentApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(SpringagentApplication.class, args);
    }


}
