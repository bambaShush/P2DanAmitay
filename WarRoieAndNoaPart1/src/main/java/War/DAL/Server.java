package War.DAL;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;


public class Server {
	public static void main(String[] args) throws IOException {
		final ServerSocket server = new ServerSocket(7000);
		Socket socket = null;
		ObjectInputStream inputStream;
		ObjectOutputStream outputStream;
		
		Object recievedObj=null;
		
		try {
			System.out.println(new Date() + " --> Server waits for client...");
			socket = server.accept(); // blocking
			System.out.println(new Date() + " --> Client connected from "
					+ socket.getInetAddress() + ":" + socket.getPort());
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			inputStream = new ObjectInputStream(socket.getInputStream());
			outputStream.writeObject("Welcome to War server!");
			do {
				recievedObj = inputStream.readObject();
				outputStream.writeObject("some message about the recevied request"); //set proper message
				System.out.println(new Date() + " --> Recieved from client: "
						+ recievedObj);
			} while (recievedObj != null);
		} catch (Exception e) {
			System.err.println(e);
		} finally {
			try {
				socket.close();
				server.close();
				System.out
						.println("Sever is closing after client is disconnected");
			} catch (IOException e) { }
		}
	}
}
