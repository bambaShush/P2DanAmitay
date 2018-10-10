package War.BusinessLogic;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Date;

import War.WarObserver.WarObservable;



public class Client extends WarObservable implements Runnable {

	private String serverIp;
	private int serverPort;
	
	private Socket socket = null;
	private DataInputStream inputStream;
	private DataInputStream userInput;
	private ObjectOutputStream objOutputStream;	
	private boolean isConnected;
	
	public Client(Object obj) {
		try {
		socket = new Socket(this.serverIp, this.serverPort);
		System.out.println("I'm at " + socket.getLocalAddress() + ":" + socket.getLocalPort());
		objOutputStream = new ObjectOutputStream(socket.getOutputStream());
		inputStream = new DataInputStream(socket.getInputStream()); 
		
		userInput = new DataInputStream(System.in);
		
		isConnected = true;
		new Thread(this).start();
		} catch (Exception e) {
			System.err.println(e);
		}
		finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void updateMessageOutide(String message) {
		publish(subscriber -> subscriber.setServerMessage(message));
	}
	
	@Override
	public void run() {
		while (isConnected) {
			try {
				String theMessage = inputStream.readLine();
				updateMessageOutide(theMessage);
			} catch (IOException e) {
				System.out.println(e.getMessage());
			} 
		}
		
	}

}
