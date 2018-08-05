package com.century.logssender.polling;

import com.century.logssender.model.LogEvent;
import com.century.logssender.properties.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class LogQueryExecutor {

    private final ApplicationProperties applicationProperties;
    private final JdbcTemplate jdbcTemplate;
    private final LogEventMapper logEventMapper;

    @Autowired
    public LogQueryExecutor(ApplicationProperties applicationProperties,
                            JdbcTemplate jdbcTemplate,
                            LogEventMapper logEventMapper) {
        this.applicationProperties = applicationProperties;
        this.jdbcTemplate = jdbcTemplate;
        this.logEventMapper = logEventMapper;
    }

    List<LogEvent> queryResults(Long interval) {
        return jdbcTemplate.query(getLogsPollingQuery(), getQueryArguments(), getRowMapper());
    }

    private String getLogsPollingQuery() {
        return "select ID ,ReceivedAt ,DeviceReportedTime ,Facility ,Priority ,FromHost ,Message ,SysLogTag" +
                " from syslog.systemevents t" +
                " where t.ReceivedAt >= date_sub(now(), interval ? second )" +
                " and t.ReceivedAt < now();";
    }

    private Object[] getQueryArguments() {
        return new Object[]{applicationProperties.getPollingInterval()};
    }

    private RowMapper<LogEvent> getRowMapper() {
        return (resultSet, i) -> logEventMapper.map(resultSet);
    }
}
