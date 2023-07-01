package dev.inspector.springagent.app.controller;

import dev.inspector.springagent.app.service.TestServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private TestServiceImpl testServiceImpl;

    @GetMapping("/test")
    void test(@RequestParam String name) {
        System.out.println("REST request received.");
        testServiceImpl.findUser(name);
    }
}
