package dev.inspector.spring.config;

import dev.inspector.spring.interceptors.rest.OutgoingRestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OutgoingRestInterceptorConfig {

    @Autowired
    OutgoingRestInterceptor outgoingRestInterceptor;

    @Bean
    public RestTemplate restTemplate() {
        BufferingClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.getInterceptors().add(outgoingRestInterceptor);
        return restTemplate;
    }

}
