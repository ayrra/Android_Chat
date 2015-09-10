package ayapplications.chat;

import android.os.Handler;
import android.widget.TextView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by andy on 9/9/15.
 */
public class recThread extends Thread {
    DatagramSocket sock;
    TextView ctv;
    Handler handler = new Handler();
    String finalTString;
    threadControl tc;

    public recThread(DatagramSocket sock, TextView chatTextView, threadControl tC) {
        this.sock = sock;
        this.ctv = chatTextView;
        this.tc = tC;

    }

    public void run() {
        DatagramPacket dp = new DatagramPacket(new byte[1024], 1024);
        String tString;

        while(!tc.returnTrue()) {
            try {
                sock.receive(dp);
            } catch (IOException e) {
                e.printStackTrace();
            }
            tString = new String(dp.getData());
            tString = tString.trim();
            finalTString = tString;
            handler.post(new Runnable() {
                public void run() {
                    if (!tc.returnTrue())
                    ctv.append("\n" + finalTString);
                }
            });
        }
    }

}
