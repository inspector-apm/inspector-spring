package dev.inspector.springagent.app.service;

import dev.inspector.springagent.app.entity.User;
import dev.inspector.springagent.app.repository.TestRepository;
//import dev.inspector.springagent.lib.interceptors.queue.MessagePushInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
//import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private TestRepository testRepository;
    @Autowired
    private RestTemplate restTemplate;
//    @Autowired
//    private JmsTemplate jmsTemplate;
//    @Autowired
//    private MessagePushInterceptor messagePushInterceptor;

    public User findUser(String name) {
        executeOutgoingRESTRequest();
//        testMessagePush("Test message");
        User user = testRepository.findByName(name);
        return user;
    }

    private void executeOutgoingRESTRequest() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://httpbin.org/get", String.class);
    }

//    public void testMessagePush(String message) {
//        jmsTemplate.convertAndSend("anotherQueue", message, messagePushInterceptor);
//    }

}
