package listener;

import db.DBReader;
import mailsender.MailSender;
import model.LogSysEvent;
import model.MailTemplate;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SysEventListener implements EventListener {

    private static boolean READ = true;
    private static final int TIMEOUT_READING_SECONDS = 60;

    private ArrayList<LogSysEvent> sysEventList;

    public void listenNewEvent() {
        MailSender mailSender = new MailSender();
        DBReader dbReader = new DBReader();
        List<MailTemplate> mailTemplates = mailSender.readMailTemplate();
        while(READ) {
            try {
                try {
                    sysEventList = dbReader.getSysEventList(TIMEOUT_READING_SECONDS);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (sysEventList.size() != 0) {
                    for (LogSysEvent logSysEvent : sysEventList) {
                        mailSender.sendMailToRecipient(mailTemplates, logSysEvent);
                    }
                }
                Thread.sleep(TIMEOUT_READING_SECONDS*1000);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        mailSender = null;
        dbReader = null;
    }

    public void stopListen() {
        READ = false;
    }
}
