package dev.inspector.springagent.repository;

import dev.inspector.springagent.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<User, Long> {
    User findByName(String name);
}
