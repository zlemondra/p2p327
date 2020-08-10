import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.HashMap;

import java.util.*;
import java.net.InetAddress;

public class ServerNode {
	
	public static String name;						//variable for the name of the server
	public static int port;							//variable for the port number of the server
	public static String ipAddress;				//variable for the IP address
	
	public static boolean isServer = false;					//determines if node is acting as the server
	public static boolean isClient = false;					//determines if node is acting as a client
	
	//keeps track of the sockets connected to each server name
	public static HashMap<String, Socket> socketHashMap = new HashMap<String, Socket>();
	
	public static ServerSocket serverSock;		//the server socket
	public static Socket clientSock;				//the client socket
	
	
	public static String serverArgument;			//takes in what's written on the command line
	
	public static void main(String[] args) throws IOException {
		serverArgument = args[0];
		InetAddress local = InetAddress.getLocalHost();			//stores address of local host in local
		ipAddress = local.getHostAddress();						//.getHostAddress() returns the address in the form of a string
		name = local.getHostName();								//stores name of local host in variable name
		port = 0;						//setting port to 0 will find the next available port when creating a server socket

		//if the name of the server using the command line matches the server name, then that server becomes the server and creates a server socket
		if(name.equalsIgnoreCase(serverArgument)) {
			isServer = true;
			
			if(isServer) {
				try {
					serverSock = new ServerSocket(port);
					
					//creates and starts the thread for the client
					Thread client = new Thread(new PNode(socketHashMap));
					client.start();
				}
				//handles the thrown exception if the socket cannot be created
				catch(IOException i){
					i.printStackTrace();
				}
			}
			
		}
		//if the name of the server does not match the name of the server using the command line, then that server is the client
		else {
			isClient = true;
			
			//a thread to connect the peer nodes is created and started
			if(isClient) {
				Thread connectPeers = new Thread(new FindPeers(name, ipAddress, port, socketHashMap));
				connectPeers.start();
			}
		}
		
		while(!serverSock.isClosed()) {
			//the accept method makes the server wait until the client signs on and requests a connection
			clientSock = serverSock.accept();
			
			//creates and starts thread to run the server implementation once a connection has been made between client and server
			Thread servSide = new Thread(new PNode_ServSide(clientSock));
			servSide.start();
		}
		
		//runs the menu in the PNode class
		Thread menu = new Thread(new PNode(socketHashMap));
		menu.start();
		 
		
		
	}
}
