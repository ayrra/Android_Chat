package ayapplications.chat;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class chat extends AppCompatActivity {

    Button sendButton;
    EditText sendBox;
    TextView chatTextView;
    String username;
    String drop = "c139af0a9b21303eadaa942b45059e";

    InetAddress serverAddress;
    int serverPort;
    DatagramSocket socket;
    threadControl tC = new threadControl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initializeVar();
        initializeConnection();
        startThreads();
    }

    public void startThreads() {
        new sendThread(socket, username, serverAddress, serverPort, chatTextView, sendBox, sendButton, tC).start();
        new recThread(socket, serverAddress, serverPort, chatTextView, tC).start();
    }

    public void initializeConnection() {
        try {
            serverAddress = InetAddress.getByName("52.26.74.218");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        serverPort = 3000;
        try {
            socket = new DatagramSocket(serverPort);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    //overwrite backpress to close socket so we don't error out
    public void onBackPressed() {
        tC.setTrue();
        new sendString(socket, serverAddress, serverPort, drop).start();    //we use this to send a disconnect to the server
        super.onBackPressed();
    }

    public void onPause() {
        tC.setTrue();
        new sendString(socket, serverAddress, serverPort, drop).start();    //we use this to send a disconnect to the server
        super.onBackPressed();
        super.onPause();
    }

    private void initializeVar() {
        chatTextView = (TextView) findViewById(R.id.chatTextView);
        sendButton = (Button) findViewById(R.id.sendButton);
        sendBox = (EditText) findViewById(R.id.sendBox);
        setUsername();
    }

    public void setUsername() {
        Intent intent = getIntent();
        username = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        showUsername();
        sendConnectMsg();
    }

    public void showUsername() {
        chatTextView.append("Your username is: " + username);
    }

    public void sendConnectMsg() {
        chatTextView.append("\n" + "Connecting to server...");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
