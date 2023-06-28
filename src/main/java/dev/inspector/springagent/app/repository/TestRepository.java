package dev.inspector.springagent.app.repository;

import dev.inspector.springagent.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<User, Long> {
    User findByName(String name);
}
