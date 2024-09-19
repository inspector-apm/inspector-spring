package dev.inspector.spring.interceptors.db;

import com.p6spy.engine.common.ConnectionInformation;
import com.p6spy.engine.common.StatementInformation;
import org.springframework.util.StringUtils;

public class DatabaseInfo {

    private String databaseProductName;

    public DatabaseInfo(String databaseProductName) {
        this.databaseProductName = databaseProductName;
    }

    public String getDatabaseProductName() {
        return databaseProductName;
    }

    public void setDatabaseProductName(String databaseProductName) {
        this.databaseProductName = databaseProductName;
    }

    public static DatabaseInfo buildFrom(StatementInformation statementInformation) {
        if (statementInformation == null) {
            return null;
        }

        ConnectionInformation connectionInfo = statementInformation.getConnectionInformation();

        if (connectionInfo == null) {
            return null;
        }

        String rawDbUrl = connectionInfo.getUrl();

        if (!StringUtils.hasText(rawDbUrl)) {
            return null;
        }

        String dbUrl = dev.inspector.spring.utils.StringUtils.removePrefix(dev.inspector.spring.utils.StringUtils.removePrefix(rawDbUrl, "jdbc:"), "p6spy:");
        String[] dbUrlParts = dbUrl.split(":");

        String dbSystem = dbUrlParts[0];

        return new DatabaseInfo(dbSystem);
    }
}
