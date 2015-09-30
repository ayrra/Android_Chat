package ayapplications.chat;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by andy on 9/8/15.
 */
public class sendThread extends Thread {
    private DatagramSocket s;
    private String n;
    InetAddress sd;
    int sp;
    TextView ctv;
    EditText sendBox;
    Button sb;
    threadControl tc;

    public sendThread(DatagramSocket sock, String name, InetAddress address, int port, TextView chatTextView, EditText sendBox, Button sendButton, threadControl tc) {
        this.s = sock;
        this.n = name;
        this.sd = address;
        this.sp = port;
        this.ctv = chatTextView;
        this.sb = sendButton;
        this.sendBox = sendBox;
        this.tc = tc;
    }

    public void run() {
        if (s != null) {
            //first send the username to the server
            byte[] connect = n.getBytes();
            DatagramPacket connectPack = new DatagramPacket(connect, connect.length, sd, sp);
            try {
                s.send(connectPack);
            } catch (IOException e) {
                ctv.append("could not send username to server");
            }
            //listen for when button is pressed, when button is pressed we send to the server
            buttonListener();
        }
    }

    public void buttonListener() {
        sb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String sendText = sendBox.getText().toString();
                byte[] bytes = sendText.getBytes();
                DatagramPacket packet = new DatagramPacket(bytes, bytes.length, sd, sp);
                ctv.append("\n" + "You: " + sendText);
                new subsend(s, packet).start(); //to prevent mainthread lock
                sendBox.setText("");
            }
        });
    }

}
