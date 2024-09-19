package dev.inspector.spring.interceptors.db;

import com.p6spy.engine.common.StatementInformation;
import com.p6spy.engine.event.SimpleJdbcEventListener;
import dev.inspector.agent.executor.Inspector;
import dev.inspector.agent.model.Segment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

@Service
public class JdbcInterceptor extends SimpleJdbcEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcInterceptor.class);


    private Inspector inspector;

    private final ThreadLocal<Segment> currentSegment = new ThreadLocal<>();
    public  JdbcInterceptor(@Autowired Inspector inspector) {
        this.inspector = inspector;
    }

    @Override
    public void onBeforeAnyExecute(StatementInformation statementInformation) {
        DatabaseInfo databaseInfo = DatabaseInfo.buildFrom(statementInformation);

        if (databaseInfo == null || !inspector.hasTransaction()) {
            return;
        }

        LOGGER.debug(
                "Thread {}: JDBC Interceptor. Starting monitoring segment for query {}",
                Thread.currentThread().getName(),
                statementInformation.getSql()
        );
        Segment currentMonitoringSegment = inspector.startSegment(databaseInfo.getDatabaseProductName());
        currentSegment.set(currentMonitoringSegment);
    }

    @Override
    public void onAfterAnyExecute(StatementInformation statementInformation, long timeElapsedNanos, SQLException e) {
        Segment currentMonitoringSegment = currentSegment.get();

        if (currentMonitoringSegment != null) {
            LOGGER.debug(
                    "Thread {}: JDBC Interceptor. Ending monitoring segment for query {}",
                    Thread.currentThread().getName(),
                    statementInformation.getSql()
            );
            currentMonitoringSegment.setLabel(statementInformation.getSql());
            currentMonitoringSegment.end(BigDecimal.valueOf(TimeUnit.NANOSECONDS.toMillis(timeElapsedNanos)));
        }

        currentSegment.remove();
    }

}
