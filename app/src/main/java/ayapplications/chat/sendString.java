package ayapplications.chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by Andy on 9/30/2015.
 */
public class sendString extends Thread {
    DatagramSocket sock;
    InetAddress inet;
    int port;
    String mess;

    public sendString(DatagramSocket socket,InetAddress address,int port,String message) {
        this.sock = socket;
        this.inet = address;
        this.port = port;
        this.mess = message;
    }
    public void run() {
        if (sock != null) {
            DatagramPacket packet = new DatagramPacket(mess.getBytes(), mess.getBytes().length, inet, port);
            try {
                sock.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            sock.close();
        }
    }
}
