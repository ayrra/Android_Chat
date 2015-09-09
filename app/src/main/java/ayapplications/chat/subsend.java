package ayapplications.chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by andy on 9/8/15.
 */
public class subsend extends Thread {
    DatagramSocket s;
    DatagramPacket p;

    public subsend(DatagramSocket s, DatagramPacket p) {
        this.s = s;
        this.p = p;

    }
    public void run () {
        try {
            s.send(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
