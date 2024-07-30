package projet.chat;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ThreadReceptionMessageServeur extends Thread {

	private Socket client;
	private boolean execution;

	public ThreadReceptionMessageServeur(Socket client) {
		this.client = client;
		this.execution = true;
	}

	//public void stopThread() {
	//	this.stopThread();
	//}
	
	public void run() {
		try {
			// Réception des messages du serveur
			DataInputStream ins = new DataInputStream(client.getInputStream());
			while (execution) {
				String message = (String) ins.readUTF();
				System.out.println(message);
			}
		} catch (IOException e) {
			System.out.println("Le serveur s'est déconnecté de manière impromptu.");
			execution = false;
		}
	}

}
