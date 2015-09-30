package ayapplications.chat;

/**
 * Created by andy on 9/9/15.
 * we use this simple thread control class to close the receving thread before we close the socket,
 * otherwise it will reprint the same message
 * we make an instance of it in chat
 */
public class threadControl {
    boolean torF;

    public void setTrue() {
        torF = true;
    }
    public boolean returnTrue() {
        return torF;
    }

}
