package segmentedfilesystem;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;

public class Main {
	int port;
	InetAddress address;
	DatagramSocket socket = null;
	DatagramPacket packet;
	byte[] sendBuf = new byte[256];
	public static HashMap<Byte, ArrayList<byte[]>> storage = new HashMap<>();
	public static HashMap<Byte, byte[]> headerStorage = new HashMap<>();

	public static void main(String[] args) throws IOException {

		InetAddress address = InetAddress.getByName("146.57.33.55");
		DatagramSocket socket = new DatagramSocket();

		DatagramPacket sendPacket = new DatagramPacket(new byte[1], 1, address, 6014);
		socket.send(sendPacket);

		DatagramPacket receivedPacket = new DatagramPacket(new byte[1028], 1028);
		socket.receive(receivedPacket);
		
		byte[] received = Arrays.copyOf(receivedPacket.getData(), receivedPacket.getLength());
		Byte ID = new Byte(getFileID(received));
		Integer[] lengths = new Integer[3];
		Byte[] fileIDs = new Byte[3];
		int counter = 0;

		while (true) {

			if (!storage.containsKey(ID)) {
				if (isHeader(received)) {
					headerStorage.put(ID, received);
				} else {
					ArrayList<byte[]> temp = new ArrayList<byte[]>();
					temp.add(received);
					storage.put(ID, temp);
				}
			} else {
				if (isHeader(received)) {
					headerStorage.put(ID, received);
				} else {
					storage.get(ID).add(received);
				}
			}

			if (received[0] % 4 == 3) {
				int value = new BigInteger(new byte[] { received[2], received[3] }).intValue();
				lengths[counter] = value + 1;
				fileIDs[counter] = ID;
				counter++;
			}
			
			if (checkAmount(lengths)) {
				break;
			}

			receivedPacket = new DatagramPacket(new byte[1028], 1028);
			socket.receive(receivedPacket);
			received = Arrays.copyOf(receivedPacket.getData(), receivedPacket.getLength());
			ID = new Byte(getFileID(received));
		}

		toFile(fileIDs);
		socket.close();
		System.out.println("Done!");
	}

	public static void toFile(Byte[] IDs) throws IOException {
		for (int i = 0; i < IDs.length; i++) {
			byte[] byteName = headerStorage.get(IDs[i]);
			String name = new String(Arrays.copyOfRange(byteName, 2, byteName.length));
			FileOutputStream stream = new FileOutputStream("/home/blask017/lab-9/" + name);
			try {
				ArrayList<byte[]> fileParts = storage.get(IDs[i]);

				fileParts.sort(new ByteArrComparator());
				for (byte[] a : fileParts) {
					stream.write(Arrays.copyOfRange(a, 4, a.length));
				}
			} finally {
				stream.close();
			}
		}

	}

	public static boolean checkAmount(Integer[] lengths) {
		int result = 0;
		for (int i = 0; i < lengths.length; i++) {
			if (lengths[i] == null) {
				return false;
			}
		}

		Collection<ArrayList<byte[]>> files = storage.values();
		Integer[] temp = Arrays.copyOf(lengths, lengths.length);
		for (int i = 0; i < lengths.length; i++) {
			for (ArrayList<byte[]> a : files) {
				if (temp[i] != null && a.size() == temp[i]) {
					result++;
					temp[i] = null;
				}
			}
		}
		return result == 3;
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

	private static class ByteArrComparator implements Comparator<byte[]> {

		@Override
		public int compare(byte[] o1, byte[] o2) {
			if (getPacketNumber(o1) < getPacketNumber(o2)) {
				return -1;
			}
			if (getPacketNumber(o1) > getPacketNumber(o2)) {
				return 1;
			}

			return 0;
		}

		private int getPacketNumber(byte[] arr) {
			return new BigInteger(new byte[] { arr[2], arr[3] }).intValue();
		}
	}

}
