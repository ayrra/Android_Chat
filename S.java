
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;

public class S {

	public static void main(String[] args) throws IOException {
		
		ArrayList<Integer> port = new ArrayList<Integer>();
		ArrayList<InetAddress> IP = new ArrayList<InetAddress>();
		ArrayList<String> name = new ArrayList<String>();
		int userCount = 0;
		boolean newUserBit = true; //we use this so the names are not repeated on first connect
		String trimmed = "";
	
		if (args.length != 1)
			throw new IllegalArgumentException("Parameter: <Port>");
		int servPort = Integer.parseInt(args[0]);
		
		DatagramSocket socket = new DatagramSocket(servPort);
		byte[] rec = new byte[256];
		byte[] send = new byte[256];
				
		while(true) {
			DatagramPacket recPack = new DatagramPacket(rec, rec.length);
			socket.receive(recPack);
			trimmed = new String(recPack.getData(), 0, recPack.getLength()).trim();
			System.out.println("received " + trimmed);
				if (!port.contains(recPack.getPort())) {
					port.add(recPack.getPort());
					IP.add(recPack.getAddress());
					name.add(trimmed);
					userCount++;
					System.out.println("Joined: IP:   " + recPack.getAddress());
					System.out.println("Joined: Port: " + recPack.getPort());
					System.out.println("Joined: Name: " + trimmed);
					System.out.println("Joined: Usr#: " + userCount);
					newUserBit = true;
				}
				else {newUserBit = false;}
				
				if (newUserBit) {
					String addMsg = "SERVER: " + new String(name.get(port.indexOf(recPack.getPort())) + " joined the chatroom!");
					send = new byte[send.length]; //anything we send will be trimmed by the client(doesn't matter here)
					send = addMsg.getBytes();
					for (int i = 0; i < userCount; i++) {
						//broadcast to everyone when a new user joins
							DatagramPacket sendJoin = new DatagramPacket(send, send.length, IP.get(i), port.get(i));
							socket.send(sendJoin);	
					}
					System.out.println(new String(addMsg.getBytes()));
				}
				if(!newUserBit && !trimmed.equals("c139af0a9b21303eadaa942b45059e")) {
					String completeString = new String(name.get(port.indexOf(recPack.getPort())) + " sent: "+ trimmed);
					send = new byte[send.length]; 
					send = completeString.getBytes();
					for (int i = 0; i < userCount; i++) {
						//broadcast to everyone except itself
						if (!port.get(i).equals(recPack.getPort())) {
							DatagramPacket sendPack = new DatagramPacket(send, send.length, IP.get(i), port.get(i));
							socket.send(sendPack);
						}
					}
				} 
				//this has to go last otherwise we try to send a message to a user that doesn't exist
				if (trimmed.equals("c139af0a9b21303eadaa942b45059e") && !newUserBit) {
					String dropMsg = "SERVER: " + new String(name.get(port.indexOf(recPack.getPort())) + " closed chat!");
					send = new byte[send.length]; //anything we send will be trimmed by the client(doesn't matter here)
					send = dropMsg.getBytes();
					for (int i = 0; i < userCount; i++) {
						//broadcast to everyone except itself
						if (!port.get(i).equals(recPack.getPort())) {
							DatagramPacket sendDropMsg = new DatagramPacket(send, send.length, IP.get(i), port.get(i));
							socket.send(sendDropMsg);
						}
					}
				int index = port.indexOf(recPack.getPort());
				name.remove(index);
				IP.remove(index);
				port.remove(index);
				userCount--;
				System.out.println(new String(dropMsg.getBytes()));
				}
		}	
	}
		//Never reach this
}
