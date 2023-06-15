package dev.inspector.springagent.service;

import dev.inspector.springagent.entity.User;

public interface TestService {
    User findUser(String name);
}
