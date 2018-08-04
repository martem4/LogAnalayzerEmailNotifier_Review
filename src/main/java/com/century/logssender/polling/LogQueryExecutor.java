package com.century.logssender.polling;

import com.century.logssender.properties.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;

@Service
class LogQueryExecutor {

    private final ApplicationProperties applicationProperties;

    @Autowired
    public LogQueryExecutor(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public ResultSet queryResults(Long interval) {
        ResultSet rs = null;
        try (Connection connection = getConnection()){
            try (PreparedStatement statement = connection.prepareStatement(getLogsPollingQuery())) {
                statement.setInt(1, applicationProperties.getPollingInterval());
                rs = statement.executeQuery();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs;
    }

    private String getLogsPollingQuery() {
        return "select ID" +
                " ,ReceivedAt" +
                " ,DeviceReportedTime" +
                " ,Facility" +
                " ,Priority" +
                " ,FromHost" +
                " ,Message" +
                " ,SysLogTag" +
                " from syslog.systemevents t" +
                " where t.ReceivedAt >= date_sub(now(), interval ? second )\n" +
                "  and t.ReceivedAt < now();";
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(applicationProperties.getUrl(),
                applicationProperties.getUser(),
                applicationProperties.getPassword()
        );
    }

}
