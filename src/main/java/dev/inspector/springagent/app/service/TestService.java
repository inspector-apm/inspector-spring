package dev.inspector.springagent.app.service;

import dev.inspector.springagent.app.entity.User;

public interface TestService {
    User findUser(String name);
}
