package dev.inspector.spring.interceptors.repository;

import dev.inspector.agent.executor.Inspector;
import dev.inspector.agent.model.Segment;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Aspect
@Component
public class RepositoryInterceptor {

    @Autowired
    Inspector inspector;

    @Autowired
    DataSource dataSource;

    @Autowired
    HibernateInterceptor hibernateInterceptor;  // Inietta il bean HibernateInterceptor

    @Around("execution(* org.springframework.data.repository.Repository+.*(..))")
    public Object interceptQuery(ProceedingJoinPoint joinPoint) throws Throwable {
        String dbProductName = dataSource.getConnection().getMetaData().getDatabaseProductName();
        Segment segment = inspector.startSegment(dbProductName);

        try {
            Object result = joinPoint.proceed();

            // Recupera l'ultima query SQL dal HibernateInterceptor
            String executedQuery = hibernateInterceptor.getLastQuery();
            segment.setLabel(executedQuery != null ? executedQuery : "No SQL query detected");

            return result;
        } finally {
            segment.end();
        }
    }
}
