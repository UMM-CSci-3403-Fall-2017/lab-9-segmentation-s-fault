package segmentedfilesystem;

import static org.junit.Assert.*;

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

}
