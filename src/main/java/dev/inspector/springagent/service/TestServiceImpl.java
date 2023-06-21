package dev.inspector.springagent.service;

import dev.inspector.springagent.entity.User;
import dev.inspector.springagent.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private TestRepository testRepository;
    @Autowired
    private RestTemplate restTemplate;

    public User findUser(String name) {
        User user = null;
        if (name != null)
            user = testRepository.findByName(name);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://httpbin.org/get", String.class);
        return user;
    }

}
