package dev.inspector.springagent.app.controller;

import dev.inspector.springagent.app.entity.User;
import dev.inspector.springagent.app.service.TestServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest")
public class TestController {

    @Autowired
    private TestServiceImpl testServiceImpl;

    @GetMapping("/hello/{name}")
    User test(@PathVariable String name) {
        System.out.println("REST request received.");
        return testServiceImpl.findUser(name);
    }
}
