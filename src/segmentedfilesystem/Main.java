package segmentedfilesystem;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Main {
	int port;
	InetAddress address;
	DatagramSocket socket = null;
	DatagramPacket packet;
	byte[] sendBuf = new byte[256];
    
    public static void main(String[] args) throws IOException {
    	
    	
    	if(args.length != 1){
    		System.out.println("Using segmented client server");
    		return;
    	}
    	DatagramSocket socket = new DatagramSocket();
    	
        byte[] buf = new byte[256];
        InetAddress address = InetAddress.getByName(args[0]);
        DatagramPacket packet = new DatagramPacket(buf,buf.length,address,4445);
        socket.send(packet);
    }

}
