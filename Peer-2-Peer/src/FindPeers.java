import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;


public class FindPeers implements Runnable {
	public HashMap<String, Socket> socketHashMap;					//maps all sockets for the server according to server name and the socket
	public String nameOfServer;								//variable for the name of the server
	public String addressOfServer;							//variable for the host address of the server
	public int portOfServer;									//variable for the port of the server
	public Socket sock;										//variable for the socket
	
	/**
	 * constructor for FindPeers class
	 * @param nameOfServer
	 * @param addressOfServer
	 * @param portOfServer
	 * @param sockets
	 */
	public FindPeers(String nameOfServer, String addressOfServer, int portOfServer, HashMap<String, Socket> socketHashMap) {
		this.nameOfServer = nameOfServer;
		this.addressOfServer = addressOfServer;
		this.portOfServer = portOfServer;
		this.socketHashMap = socketHashMap;
	}
	
	/**
	 * tries to create a new socket with the server information and then puts it in the hash map if successful
	 */
	public void run() {
		try {
			sock = new Socket(addressOfServer, portOfServer);		//tries to create a new socket with the server address and port
		}
		catch(IOException i){
			i.printStackTrace();									//if a problem occurs when creating the socket, an exception will 
																//be caught and handled to print the stack
		}
		
		socketHashMap.put(nameOfServer, sock);							//socket will be placed in the hash map with the server name 
																//as the key if created successfully
	}
}
