package projet.chat;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Hashtable;

public class ThreadRecepteurDeMessageClient extends Thread {

	private Hashtable<Socket, String> clients;
	private Socket client;
	private boolean execution;

	public ThreadRecepteurDeMessageClient(Hashtable<Socket, String> clients, Socket client) {
		this.clients = clients;
		this.client = client;
		this.execution = true;
	}

	public void run() {
		try {
			// Réception des messages du client client
			DataInputStream ins = new DataInputStream(client.getInputStream());
			while (execution) {
				// Réception des messages du client client
				String message = (String) ins.readUTF();
				String pseudo = (String) clients.get(client);
				String messageDiffuse;
				if (message.equals("exit")) {
					messageDiffuse = "L'utilisateur " + pseudo + " a quitté la conversation.";
					System.out.println(messageDiffuse);
					execution = false;
					clients.remove(client);
				} else {
					messageDiffuse = pseudo + " a dit : " + message;
				}
				// Renvoyer le message reçu à tout les clients
				GestionnaireEnvoieMessageClients gestionnaireEnvoie = new GestionnaireEnvoieMessageClients(clients);
				gestionnaireEnvoie.envoyerMessageAuxClients(messageDiffuse);
				//
			}
		} catch (IOException e) {
			String pseudo = (String) clients.get(client);
			String messageDiffuse = "L'utilisateur " + pseudo + " a été éjecté de la conversation à cause d'un défaut de connexion.";
			System.out.println(messageDiffuse);
			clients.remove(client);
			try {
				client.close();
			} catch (IOException e1) {
				System.out.println("Le socket client n'a pas réussi à être déconnecté.");
			}
			GestionnaireEnvoieMessageClients gestionnaireEnvoie = new GestionnaireEnvoieMessageClients(clients);
			gestionnaireEnvoie.envoyerMessageAuxClients(messageDiffuse);
		}
	}
}
