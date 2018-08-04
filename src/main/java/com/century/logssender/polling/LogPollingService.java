package com.century.logssender.polling;

import com.century.logssender.model.LogEvent;
import com.century.logssender.properties.ApplicationProperties;
import io.reactivex.Observable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class LogPollingService {

    private final LogQueryExecutor queryExecutor;
    private final LogEventMapper logEventMapper;
    private final ApplicationProperties applicationProperties;

    @Autowired
    public LogPollingService(LogQueryExecutor queryExecutor,
                             LogEventMapper logEventMapper,
                             ApplicationProperties applicationProperties) {
        this.queryExecutor = queryExecutor;
        this.logEventMapper = logEventMapper;
        this.applicationProperties = applicationProperties;
    }

    public Observable<LogEvent> poll() {
        return this.poll(applicationProperties.getPollingInterval());
    }

    public Observable<LogEvent> poll(int interval) {
        return Observable.interval(interval, TimeUnit.SECONDS)
                .map(queryExecutor::queryResults)
                .map(logEventMapper::parseLogs)
                .flatMapIterable(l -> l);
    }



}
