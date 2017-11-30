package segmentedfilesystem;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
	int port;
	InetAddress address;
	DatagramSocket socket = null;
	DatagramPacket packet;
	byte[] sendBuf = new byte[256];
	public static boolean f1Done;
	public static boolean f2Done;
	public static boolean f3Done;
	public static HashMap<Byte, ArrayList<byte[]>> storage = new HashMap<>();

	public static void main(String[] args) throws IOException {
		f1Done = false;
		f2Done = false;
		f3Done = false;

		InetAddress address = InetAddress.getByName("146.57.33.55");
		DatagramSocket socket = new DatagramSocket();

		DatagramPacket sendPacket = new DatagramPacket(new byte[1], 1, address, 6014);
		socket.send(sendPacket);

		DatagramPacket receivedPacket = new DatagramPacket(new byte[1000], 1000);
		System.out.println("this is the first length " + receivedPacket.getLength());
		System.out.println("datapacket sent?");
		socket.receive(receivedPacket);
		byte[] received = receivedPacket.getData();
		Byte ID = new Byte(getFileID(received));
		
		while (receivedPacket.getLength() != 0) {
			if(received[0] % 4 == 1){
				System.out.println("this is the last packet?");
				int coolio = received[2] & received[3];
				System.out.println(coolio);
			}

			if (!storage.containsKey(ID)) {
				ArrayList<byte[]> temp = new ArrayList<byte[]>();
				temp.add(received);
				storage.put(ID, temp);
			} else {
				storage.get(ID).add(received);
			}
			
			receivedPacket = new DatagramPacket(new byte[1000], 1000);
			socket.receive(receivedPacket);
			received = receivedPacket.getData();
			ID = new Byte(getFileID(received));
//			System.out.println(ID.toString());
//			System.out.println(storage.size());
		}

		System.out.write(received, 0, 1000);
		socket.close();
	}

	public static boolean isID(byte[] headerPacket, byte[] comparePacket) {
		if (getFileID(comparePacket) == getFileID(headerPacket)) {
			return true;
		} else {
			return false;
		}

	}

	public static boolean isHeader(byte[] comparePacket) {
		if (comparePacket[0] % 2 == 0) {
			return true;
		} else {
			return false;
		}
	}

	public static byte getFileID(byte[] packetByte) {
		return packetByte[1];
	}

}
