package segmentedfilesystem;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

/**
 * This is just a stub test file. You should rename it to
 * something meaningful in your context and populate it with
 * useful tests.
 */
public class SegmentedFileSystemTests {

    @Test
    public void isHeaderTest() {
        byte[] testBytes = {0, 23, 34, 78, 23, 90, 78};
        DataPacket test = new DataPacket();
        
        assertTrue(test.isHeader(testBytes));
    }
    
    @Test
    public void isEndTest(){
    	byte[] testBytes = {3, 23, 45, 56, 123, 127, -29};
    	DataPacket test = new DataPacket();
    	
    	assertTrue(test.isEnd(testBytes));
    }
    
    @Test
    public void packetNumberTest(){
    	byte[] testBytes = {3, 34, 3, 3, 23, 89, 34, 78};
    	DataPacket test = new DataPacket();
    	
    	assertEquals(test.getPacketNumber(testBytes), 771);
   
    }
    @Test
    public void packetSortTest(){
    	byte[] firstPacket = {3,28,0,0,10,15,20};
    	byte[] secondPacket = {3,28,0,1,25,30,35};
    	byte[] thirdPacket = {3,28,0,2,40,45,50};
    	byte[] endPacket = {3,28,0,3,55,60,65};
    	ArrayList<byte[]> packets = new ArrayList<>();
    	packets.add(secondPacket);
    	packets.add(endPacket);
    	packets.add(thirdPacket);
    	packets.add(firstPacket);
    	DataPacket test = new DataPacket();
    	
    	test.sortPackets(packets);
    	assertTrue(test.isEnd(packets.get(3)));
    	assertEquals(test.getPacketNumber(packets.get(0)), 0);
    	assertEquals(test.getPacketNumber(packets.get(1)), 1);
    	assertEquals(test.getPacketNumber(packets.get(2)), 2);
    }

}
