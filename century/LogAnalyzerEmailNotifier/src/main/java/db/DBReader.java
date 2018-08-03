package db;

import model.LogSysEvent;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class DBReader {
    private static final String JDBC_PROPERTIES_FILE = "app.properties";
    private static Properties propertiesDb = new Properties();
    private static Connection connection;
    private static Statement statement;

    private void readDbConProperties() throws IOException {
        InputStream inputStream = new FileInputStream(JDBC_PROPERTIES_FILE);
        propertiesDb.load(inputStream);
    }

    private ResultSet getSysEventFromDb(int timeOutReading) {

        ResultSet rs = null;
        try {
            try {
                connection = DriverManager.getConnection(propertiesDb.getProperty("db.url"),
                        propertiesDb.getProperty("db.login"),
                        propertiesDb.getProperty("db.password"));
            } catch (SQLException e) {
                e.printStackTrace();
            }

            statement = connection.createStatement();
            String query = String.format("select ID" +
                    " ,ReceivedAt" +
                    " ,DeviceReportedTime" +
                    " ,Facility" +
                    " ,Priority" +
                    " ,FromHost" +
                    " ,Message" +
                    " ,SysLogTag" +
                    " from syslog.systemevents t" +
                    " where t.ReceivedAt >= date_sub(now(), interval %d second )\n" +
                    "  and t.ReceivedAt < now();", timeOutReading);
            try {
                rs = statement.executeQuery(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public ArrayList<LogSysEvent> getSysEventList(int timeOutReading) throws IOException, SQLException {
        readDbConProperties();
        ArrayList<LogSysEvent> sysEventList = new ArrayList<LogSysEvent>();
        ResultSet rs = getSysEventFromDb(timeOutReading);

        if (rs != null) {
            while (rs.next()) {
                sysEventList.add(new LogSysEvent(rs.getInt("ID"),
                        rs.getDate("ReceivedAt"),
                        rs.getDate("DeviceReportedTime"),
                        rs.getInt("Facility"),
                        rs.getInt("Priority"),
                        rs.getString("FromHost"),
                        rs.getString("Message"),
                        rs.getString("SysLogTag")));
            }
        }
        return sysEventList;
    }
}
