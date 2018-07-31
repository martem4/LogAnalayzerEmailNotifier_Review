package DB;

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

    private static void readDbConProperties() throws IOException {
        InputStream inputStream = new FileInputStream(JDBC_PROPERTIES_FILE);
        propertiesDb.load(inputStream);
    }

    public static ArrayList<LogSysEvent> getSysEventList(int timeOutReading) throws IOException {
        readDbConProperties();
        ArrayList<LogSysEvent> sysEventList = new ArrayList<LogSysEvent>();
        ResultSet rs;
        try {
            connection =  DriverManager.getConnection(propertiesDb.getProperty("db.url"),
                    propertiesDb.getProperty("db.login"),
                    propertiesDb.getProperty("db.password"));

            statement = connection.createStatement();
            String query = "select ID" +
                    " ,ReceivedAt" +
                    " ,DeviceReportedTime" +
                    " ,Facility" +
                    " ,Priority" +
                    " ,FromHost" +
                    " ,Message" +
                    " ,SysLogTag" +
                    " from syslog.systemevents t" +
                    " where t.ReceivedAt >= date_sub(now(), interval " +timeOutReading + " second )\n" +
                    "  and t.ReceivedAt < now();";
            rs = statement.executeQuery(query);

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
        }catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return sysEventList;
    }

}
