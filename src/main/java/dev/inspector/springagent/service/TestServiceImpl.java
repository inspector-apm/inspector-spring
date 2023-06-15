package dev.inspector.springagent.service;

import dev.inspector.springagent.entity.User;
import dev.inspector.springagent.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {
    @Autowired
    TestRepository testRepository;

    public User findUser(String name) {
        return testRepository.findByName(name);
    }
}
