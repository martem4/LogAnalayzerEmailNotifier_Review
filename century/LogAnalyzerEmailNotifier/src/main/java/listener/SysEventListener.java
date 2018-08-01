package listener;

import DB.DBReader;
import MailSender.MailSender;
import Model.LogSysEvent;
import Model.MailTemplate;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SysEventListener implements EventListener {

    private static boolean READ = true;
    private static final int TIMEOUT_READING_SECONDS = 60;

    private ArrayList<LogSysEvent> sysEventList;

    public void listenNewEvent() {
        List<MailTemplate> mailTemplates = MailSender.readMailTemplate();
        while(READ) {
            try {
                sysEventList = DBReader.getSysEventList(TIMEOUT_READING_SECONDS);
                if (sysEventList.size() != 0) {
                    for (LogSysEvent logSysEvent : sysEventList) {
                        MailSender.sendMailToRecipient(mailTemplates, logSysEvent);
                    }
                }
                Thread.sleep(TIMEOUT_READING_SECONDS*1000);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopListen() {
        READ = false;
    }
}
