package segmentedfilesystem;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Main {
	int port;
	InetAddress address;
	DatagramSocket socket = null;
	DatagramPacket packet;
	byte[] sendBuf = new byte[256];

	public static void main(String[] args) throws IOException {

		InetAddress address = InetAddress.getByName("146.57.33.55");
		DatagramSocket socket = new DatagramSocket();
		
		DatagramPacket sendPacket = new DatagramPacket(new byte[1], 1, address, 6014);
		socket.send(sendPacket);
		
		DatagramPacket receivedPacket = new DatagramPacket(new byte[1000], 1000);
		System.out.println("datapacket sent?");
		socket.receive(receivedPacket);
		byte[] received = receivedPacket.getData();
		
		System.out.println("received datapacket?");
		System.out.println("this is the least significant bit? " + received[0] % 2);
		System.out.write(received, 0, 1000);
		socket.close();
	}

}
