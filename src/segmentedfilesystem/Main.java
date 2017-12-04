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
import java.util.Set;

public class Main {
	private static HashMap<Byte, ArrayList<byte[]>> storage = new HashMap<>();
	private static HashMap<Byte, byte[]> headerStorage = new HashMap<>();

	public static void main(String[] args) throws IOException {

		InetAddress address = InetAddress.getByName("146.57.33.55");
		DatagramSocket socket = new DatagramSocket();

		DatagramPacket sendPacket = new DatagramPacket(new byte[1], 1, address, 6014);
		socket.send(sendPacket);

		byte[] receivedBytes = new byte[1028];
		DatagramPacket receivedPacket = new DatagramPacket(receivedBytes, 1028);

		socket.receive(receivedPacket);

		Byte ID = new Byte(getFileID(receivedBytes));
		Integer[] lengths = new Integer[3];
		int counter = 0;

		while (true) {

			if (!storage.containsKey(ID)) {
				if (isHeader(receivedBytes)) {
					headerStorage.put(ID, Arrays.copyOf(receivedBytes, receivedPacket.getLength()));
				} else {
					ArrayList<byte[]> temp = new ArrayList<byte[]>();
					temp.add(Arrays.copyOf(receivedBytes, receivedPacket.getLength()));
					storage.put(ID, temp);
				}
			} else {
				if (isHeader(receivedBytes)) {
					headerStorage.put(ID, Arrays.copyOf(receivedBytes, receivedPacket.getLength()));
				} else {
					storage.get(ID).add(Arrays.copyOf(receivedBytes, receivedPacket.getLength()));
				}
			}

			if (isEnd(receivedBytes)) {
				lengths[counter] = new BigInteger(new byte[] { receivedBytes[2], receivedBytes[3] }).intValue() + 1;
				counter++;
			}

			if (checkAmount(lengths)) {
				break;
			}

			receivedBytes = new byte[1028];
			receivedPacket = new DatagramPacket(receivedBytes, 1028);
			socket.receive(receivedPacket);
			ID = new Byte(getFileID(receivedBytes));
		}

		toFile(storage.keySet());
		socket.close();
		System.out.println("Done!");
	}

	private static void toFile(Set<Byte> set) throws IOException {
		for (Byte b : set) {
			byte[] byteName = headerStorage.get(b);
			String name = new String(Arrays.copyOfRange(byteName, 2, byteName.length));
			FileOutputStream stream = new FileOutputStream("src/" + name);
			try {
				ArrayList<byte[]> fileParts = storage.get(b);

				fileParts.sort(new ByteArrComparator());
				for (byte[] a : fileParts) {
					stream.write(Arrays.copyOfRange(a, 4, a.length));
				}
			} finally {
				stream.close();
			}
		}

	}

	private static boolean checkAmount(Integer[] lengths) {
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
					break;
				}
			}
		}
		return result == 3;
	}

	private static boolean isHeader(byte[] comparePacket) {
		return comparePacket[0] % 2 == 0;
	}
	
	private static boolean isEnd(byte[] bytes){
		return bytes[0] % 4 == 3;
	}

	private static byte getFileID(byte[] packetByte) {
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
