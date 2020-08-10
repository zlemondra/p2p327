import java.util.*;
import java.net.*;
import java.io.*;

/*
 * 
 * Hello Professor Giacalone,
 * what this class is, is the part of the peer node that does all the client side of request like pushing and pulling the files
 */
public class PNode implements Runnable{
	private HashMap<String, Socket> socketHashMap;
	private Socket peerSocket;
	Scanner input = new Scanner(System.in);
	File file;
	// constructor of peer node takes in the hash map used for the DHT to keep files
	PNode(HashMap<String, Socket> given){
		socketHashMap = given;
	}
	/*
	 * method called upon in the node constructor
	 * starts the client server by starting a thread to run the message hander to process request
	 */

	
	// this is the part of the peer node that you can do the client portion and then run for threads
	@Override
	public void run() {
		// taking in client/user input 
		String answer = "";
		while(answer != "Leave") {
			System.out.println("Enter what you want to do:\n"
					+ "Push\n"//sending a file to the server
					+ "Pull\n"// requesting server
					+ "Leave\n");
			answer = input.next();
			
			// compare answer
			switch(answer) {
			case "Push":
				try {//needs exception because of use of files
					push();//invoke pushing method
				} catch (IOException e) {					
					e.printStackTrace();
				}
				break;
			case "Pull":
				pull();//invoke pull method
				break;				
			}
		}		
	}
	private void push() throws IOException {
		//ask for key pair to create new hash map 
		System.out.println("Enter a new key: ");
		String key = input.next();
		
		//creating file and writing contents of said new file
		System.out.println("Enter your name for new file: ");
		String fileName = input.nextLine();
		file = new File(fileName +".txt");
		file.createNewFile();
		System.out.println("Enter Contents of file");
		String contents = input.nextLine();
		FileWriter myWriter = new FileWriter(file);
	    myWriter.write(contents);
	    myWriter.close();
		//find the hashValue, where to put this KEY,VALUE
		peerSocket = getServerSocket(key);
		String fileAndContent = fileName + contents;
		//put the value if local server, put into local hash table
		//else, put into other server, by connecting to other server using sockCommunicateStream
		if(peerSocket == null)
		{
			boolean result = PNode_ServSide.push(key,fileAndContent);
			if(result)
					System.out.println("Success");
				else
					System.out.println("Failure");
		}
		else{			
			socketCommunicate(peerSocket,"Push",key, file);
		}
		
		
	}
	private void pull() {
		System.out.println("Enter Key");
		String key = input.next();
		//get the Hashvalue where to get the value from
		peerSocket = getServerSocket(key);
		
		if(peerSocket == null)//search in local hash map,
			System.out.println("The value is: "+PNode_ServSide.pull(key));
		
		else//else, find from other server
			socketCommunicate(peerSocket,"Pull",key, file);
					
		
	}
	// method to get socket from hash map
	public Socket getServerSocket(String key)
	{
		Socket socket = socketHashMap.get(key);			
		return socket;
	}

	/*This method is used to connect between sockets i.e. Servers, and send and receive
	 * message and Communicate for key/value pair to push or pull */
	public void socketCommunicate(Socket sock, String userInput, String key, File file){
		try{
			//make send and receive for sockets to communicate
			DataInputStream in = new DataInputStream(sock.getInputStream());
			DataOutputStream out = new DataOutputStream(sock.getOutputStream());
			//reading file to push
			String data= "";//contents of file
			try {
			      Scanner reader = new Scanner(file);
			      data = reader.nextLine();			   
			      reader.close();
			    } catch (FileNotFoundException e) {
			      System.out.println("An error occurred.");
			      e.printStackTrace();
			}
			String keyPair =key +","+ file.getName()+"," + data;
			//send the server the choice and key/value
			out.writeUTF(userInput);
			out.writeUTF(keyPair);
			//this reads the file when pulling it
			if(userInput.equals("Pull")){
				String[] contents = in.readUTF().split(",");
				System.out.println("File: "+contents[1]+
						"\nFile Contents:\n"+ contents[2]);
			}
			//tells you if you successfully pull it
			if(userInput.equals("Push")){	
				String result = in.readUTF();
				if(result.equals("true"))
					System.out.println("Success");			
				else
					System.out.println("Failure");
			}
			
		} catch(IOException e)
		{
			e.printStackTrace();
		}
	}


}