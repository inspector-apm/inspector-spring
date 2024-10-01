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

    @Value("${inspector.url}")
    private String url;

    @Value(("${inspector.enabled}"))
    private Boolean enabled;

    @Value(("${inspector.version}"))
    private String version;

    @Value(("${inspector.max-entries}"))
    private Integer maxEntries;

    @Bean
    public Config inspectorConfig() {
        Config config = new Config(ingestionKey);
        config.setEnabled(enabled);
        config.setUrl(url);
        config.setMaxEntries(maxEntries);
        config.setVersion(version);
        return config;
    }

}
