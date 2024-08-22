package dev.inspector.spring.interceptors.repository;

import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class HibernateInterceptor implements StatementInspector {
    private static final Logger logger = LoggerFactory.getLogger(HibernateInterceptor.class);
    private static final ThreadLocal<String> lastQuery = new ThreadLocal<>();

    @Override
    public String inspect(String sql) {
        logger.info("SQL Query: " + sql);
        lastQuery.set(sql);  // Salva la query SQL nel contesto del thread corrente
        return sql;
    }

    public String getLastQuery() {
        return lastQuery.get();  // Recupera la query SQL dal contesto del thread corrente
    }

    public void clearLastQuery() {
        lastQuery.remove();
    }
}
