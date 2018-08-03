import listener.SysEventListener;

public class Main {

    public static void main(String[] args) {
        SysEventListener listener = new SysEventListener();
        listener.listenNewEvent();
    }
}
