package dev.inspector.springagent.controller;

import dev.inspector.springagent.entity.User;
import dev.inspector.springagent.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class TestController {

    @Autowired
    private TestService testService;
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/test")
    void test(@RequestParam String name) {
        System.out.println("chiamata effettuata");
        User user = null;
        if (name != null)
            user = testService.findUser(name);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://httpbin.org/get", String.class);
        System.out.println();
    }
}
