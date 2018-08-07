package com.century.logssender.polling;

import com.century.logssender.model.LogEvent;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;

@Component
class LogEventMapper {

    @SneakyThrows
    LogEvent map(ResultSet resultSet) {
        return new LogEvent(resultSet.getInt("ID"),
                resultSet.getDate("ReceivedAt"),
                resultSet.getDate("DeviceReportedTime"),
                resultSet.getInt("Facility"),
                resultSet.getInt("Priority"),
                resultSet.getString("FromHost"),
                resultSet.getString("Message"),
                resultSet.getString("SysLogTag"));
    }
}
