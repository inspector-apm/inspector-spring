package dev.inspector.spring.config;

import dev.inspector.agent.executor.Inspector;
import dev.inspector.agent.model.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InspectorBeanConfig {

    @Value("${inspector.ingestion-key}")
    private String ingestionKey;

    @Value("${inspector.time-to-flush}")
    private String timeToFlush;

    @Bean
    public Config inspectorConfig() {
        Config config = new Config(ingestionKey, timeToFlush);
        return config;
    }

    @Bean
    public Inspector inspector(Config config) {
        return new Inspector(config);
    }
}
