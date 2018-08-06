package com.century.logssender.polling;

import com.century.logssender.model.LogEvent;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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

    @SneakyThrows
    List<LogEvent> parseLogs(ResultSet resultSet) {
        final List<LogEvent> result = new ArrayList<>();

        while (resultSet.next()) {
            result.add(map(resultSet));
        }

        return result;
    }


}
