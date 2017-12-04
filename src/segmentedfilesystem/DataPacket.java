package segmentedfilesystem;

import java.math.BigInteger;

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

}
