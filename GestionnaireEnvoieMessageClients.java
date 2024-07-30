package projet.chat;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;

public class GestionnaireEnvoieMessageClients {

	private Hashtable<Socket, String> clients;

	public GestionnaireEnvoieMessageClients(Hashtable<Socket, String> clients) {
		this.clients = clients;
	}

	public void envoyerMessageAuxClients(String message) {
		Enumeration<Socket> socketsClients = clients.keys();
		while (socketsClients.hasMoreElements()) {
			Socket socketClient = socketsClients.nextElement();
			try {
				DataOutputStream outs = new DataOutputStream(socketClient.getOutputStream());
				outs.writeUTF(message);
				outs.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
