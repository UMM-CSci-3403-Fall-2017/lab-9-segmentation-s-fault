package segmentedfilesystem;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;

public class DataPacket {
	
	public boolean isHeader(byte[] comparePacket) {
		if (comparePacket[0] % 2 == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isEnd(byte[] bytes){
		return bytes[0] % 4 == 3;
	}
	
	public int getPacketNumber(byte[] bytes){
		
		return new BigInteger(new byte[]{bytes[2], bytes[3]}).intValue();
	} 
	public void sortPackets(ArrayList<byte[]> packets){
		packets.sort(new ByteArrComparator());
	}
	private class ByteArrComparator implements Comparator<byte[]> {

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
	}
	

}
