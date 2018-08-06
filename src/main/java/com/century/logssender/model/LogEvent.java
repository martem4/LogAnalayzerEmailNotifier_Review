package com.century.logssender.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;

@Data
@AllArgsConstructor
public class LogEvent {

    private int id;
    private Date receivedAt;
    private Date deviceReportedTime;
    private int facility;
    private int priority;
    private String fromHost;
    private String message;
    private String sysLogTag;

}
