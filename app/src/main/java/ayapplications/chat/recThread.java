package ayapplications.chat;

import android.os.Handler;
import android.widget.TextView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Created by andy on 9/9/15.
 */
public class recThread extends Thread {
    DatagramSocket sock;
    TextView ctv;
    Handler handler = new Handler();
    String finalTString;
    threadControl tc;
    InetAddress inet;
    int port;

    public recThread(DatagramSocket sock,InetAddress inet, int port, TextView chatTextView, threadControl tC) {
        this.sock = sock;
        this.inet = inet;
        this.port = port;
        this.ctv = chatTextView;
        this.tc = tC;

    }

    public void run() {
        DatagramPacket dp = new DatagramPacket(new byte[1024], 1024);
        String tString;

        while(true) {
            if (tc.returnTrue())
                break;
            if (sock != null) {
                try {
                    sock.receive(dp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                tString = new String(dp.getData(), 0, dp.getLength());
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
}
