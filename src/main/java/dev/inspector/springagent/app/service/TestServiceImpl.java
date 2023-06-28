package dev.inspector.springagent.app.service;

import dev.inspector.springagent.app.entity.User;
import dev.inspector.springagent.app.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private TestRepository testRepository;
    @Autowired
    private RestTemplate restTemplate;
    private final JmsTemplate jmsTemplate;

    public TestServiceImpl(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendMessage(String message) {
        jmsTemplate.send("anotherQueue", session -> session.createTextMessage(message));
    }

    public User findUser(String name) {
        sendMessage("Test message");
        User user = null;
        if (name != null)
            user = testRepository.findByName(name);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://httpbin.org/get", String.class);
        return user;
    }

}
