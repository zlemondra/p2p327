import java.util.*;
import java.net.*;
import java.io.*;
import java.util.concurrent.ConcurrentHashMap;
/*
 * Hello Professor Giaccolne,
 * what this class is, is the part of the peer node that does all the client side of request like pushing and pulling the files
 */
public class PNode implements Runnable{
	private ConcurrentHashMap<String, Socket> socketHashMap;
	private Socket peerSocket;
	Scanner input = new Scanner(System.in);
	File file;
	// constructor of peer node takes in the hash map used for the DHT to keep files
	PNode(ConcurrentHashMap<String, Socket> given){
		socketHashMap = given;
	}
	/*
	 * method called upon in the node constructor
	 * starts the client server by starting a thread to run the message hander to process request
	 */

	
	// this is the part of the peer node that you can do the client portion and then run for threads
	@Override
	public void run() {
		
		String answer = "";
		while(answer != "Leave") {
			System.out.println("Enter what you want to do:\n"
					+ "Push\n"
					+ "Pull\n"
					+ "Leave\n");
			answer = input.next();
			
			// compare answer
			switch(answer) {
			case "Push":
				push();
			case "Pull":
				pull();
				
			}
		}		
	}
	private void push() {
		
		
	}
	private void pull() {
		System.out.println("Enter Key");
		String key = input.next();
		
	
		
	}
	/* The entire message i.e KEY + VALUE is of 1024 bytes
	 * out of which KEY is of 24 Bytes, here we pad the remaning bytes of the key with "*" 
	 * while sending we send entire 1024 bytes*/
	public String padKey(String key)
	{
		for(int i=key.length();i<24;i++)
		{
			key+="*";
		}
		return key;
	}
	/* The entire message i.e KEY + VALUE is of 1024 bytes
	 * out of which VALUE is of 1000 Bytes, here we pad the remaning bytes of the value with "*" 
	 * while sending we send entire 1024 bytes*/
	public String padValue(String value)
	{
		for(int i=value.length();i<1000;i++)
		{
			value+="*";
		}
		return value;
	}
	public Socket getServerSocket(String Key)
	{
		String hashValue = "server"+Math.abs((Key.hashCode())%8);
		Socket socket = socketHashMap.get(hashValue);			
		return socket;
	}
	
}