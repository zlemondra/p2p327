import java.util.ArrayList;
import java.net.*;
import java.io.*;

public class PNode {
	int port;//port number
	String location;//Directory Location
	
	PNode(int inputPort) throws IOException{
		port = inputPort;
		location = port + "";
		//start the client server
		
		/*Hey Lauren, if you're wondering what this is, its the thread that starts he client server
		 * I had to make it a run method in the new thread object so the thread wouldn't run in an endless
		 * loop. try and catch was a n automated 	
		 * hello
		*/
		new Thread(new Runnable() {
			public void run() {
				try {
					startServer(port);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		).start();				
	}
	/*
	 * method called upon in the node constructor
	 * starts the client server by starting a thread to run the message hander to process request
	 */
	void startServer(int temp) throws IOException { // takes in the port number as a parameter	
		ServerSocket serv = new ServerSocket(0); //server socket
		System.out.println("Server started at " + InetAddress.getLocalHost() + ":" + port);
		while(true) {
			//listening for a connection request
			Socket connect = serv.accept();//accepting the request
			//message handler object that takes the client request
			MessageHandler request = new MessageHandler(connect); //takes the connection socket as a parameter to process that request 						
			// thread object for request handler
			Thread thread = new Thread((Runnable) request);
			//now start the thread
			thread.start();
			
		}
	}
	void send(String file, String host, int port) throws UnknownHostException, IOException {
		Socket sock = new Socket(host, port);//creating the socket with the machine name and port number from parameter
		PrintWriter output = new PrintWriter(sock.getOutputStream(), true);
		/*
		 * socket getOutputSream method:
		 * Returns an output stream for this socket. If this socket has an associated channel then 
		 * the resulting outputstream delegates all of its operations to the channel. If the channel is in 
		 * non-blocking mode then the output stream's write
		 * operations will throw an IllegalBlockingModeException. 
		 * Closing the returned OutputStreamwill close the associated socket
		 */
		output.println(file); //printing the file being sent
		//gotta close up the connections
		output.close(); sock.close();
		
	}
	

	
}