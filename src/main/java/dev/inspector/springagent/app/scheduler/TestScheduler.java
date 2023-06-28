package dev.inspector.springagent.app.scheduler;

import dev.inspector.springagent.app.entity.User;
import dev.inspector.springagent.app.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TestScheduler {

    @Autowired
    TestRepository testRepository;

    @Scheduled(fixedRate = 10000)
    public void runTask() {
        System.out.println("Running scheduled task...");
        User user = new User(2L, "Pinotto");
        testRepository.save(user);
    }
}
