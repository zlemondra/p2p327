import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

/*
 * Hello Professor Giacalone,
 * what this class is, is the part of the peer node that does all the server side of request like pushing and pulling the files like utlizing the DHT to
 * complete the client's request.
 * 
 * Lauren hates the way I comment my code :(
 */
public class PNode_ServSide implements Runnable {// always gotta implement runnable if you're gonna use this class in a thread for JAVA
	private Socket clientSocket;// this the socket that is going to represent the client's requests
	public static HashMap<String, String> DHT = new HashMap<String, String>();//distributed Hash Table in the form of a hash map
	private boolean loop = true; //bool for while loop in the run() method

	public PNode_ServSide(Socket client) {
		// takes in the client socket to do server tasks based on the client's request
		clientSocket = client;
	}

	public void run(){ // this is what is going to be running in the serverNode class as the client threads
		try{
			//for communication over sockets between Client and server
			DataInputStream in = new DataInputStream(clientSocket.getInputStream());//input
			DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());//output

			while(loop){
				try{/*lauren if you're wondering why I have all theses try and catches, it's because of its auto-generated because I'm rereading data from 
					the communication node and you need a try-catch in case there's no data
				*/
					//read the choice according to client input
					String answer = in.readUTF();

					switch(answer){
						case "Push":	
							//read the key value pair from client
							String readKey = in.readUTF();
							
							//split the value from Key and file by ","
							String[] hashKey = readKey.split(",");
							String fileAndContent = hashKey[1]+ hashKey[2];//String of the file
							boolean pushResult = push(hashKey[0],fileAndContent);
							out.writeUTF(String.valueOf(pushResult));//writing if pushing the file was successful
							break;
	
						case "Pull":	

							String key = in.readUTF();
							String value = pull(key);							
							//Check if value is null return NULL to client, else return the actual VALUE for the KEY
							if(value == null)
								out.writeUTF("null");
							else
								out.writeUTF(value);
							break;
	
						case "Leave":					
							System.out.println("Client Disconnected");
							loop = false;	//exit from the while(true) loop
							//System.exit(0);
							break;
					}

				}catch(IOException e){
					e.printStackTrace();
				}
			}//end of while
		}catch(IOException e){
			e.printStackTrace();
		}
	}


	//Put function to put the KEY VALUE pair into the HashTable
	public static boolean push(String key, String file){
		//if put into hashtable is successful then true, and display over which server the hashtable was pushed
		if((DHT.put(key, file))!="null"){
			System.out.println("Distributed Hash Table:\n"+DHT);
			return true;
		}
		else{
				return false;
		}
	}
	
	//Get function to get the VALUE from the Specified KEY, if value is null it will return NULL
	public static String pull(String key)
	{		
		String value = DHT.get(key);
		return value;
	}
	

}
