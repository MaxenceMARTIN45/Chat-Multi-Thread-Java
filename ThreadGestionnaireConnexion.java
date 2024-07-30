package projet.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;

public class ThreadGestionnaireConnexion extends Thread {

	private Hashtable<Socket, String> clients;
	private ServerSocket socketServeur;
	private String typeExecution;
	private boolean execution;

	public ThreadGestionnaireConnexion(Hashtable<Socket, String> clients, ServerSocket socketServeur,
			String typeExecution) {
		this.clients = clients;
		this.socketServeur = socketServeur;
		this.typeExecution = typeExecution;
		this.execution = true;
	}

	public void run() {
		while (execution) {
			try {
				// Attend une nouvelle connexion
				Socket client = socketServeur.accept();
				// Recevoir le pseudo du nouveau client
				DataInputStream ins = new DataInputStream(client.getInputStream());
				String pseudo = (String) ins.readUTF();
				// Vérifier si le pseudo est valide
				boolean pseudoValide = true;
				Enumeration<Socket> socketsClients = clients.keys();
				while (socketsClients.hasMoreElements()) {
					Socket socketClient = socketsClients.nextElement();
					if(clients.get(socketClient).equals(pseudo)) {
						pseudoValide = false;
					}
				}
				if(pseudoValide) {
					// On envoie au client que le pseudo est correcte
					String message = "OK";
					DataOutputStream outs = new DataOutputStream(client.getOutputStream());
					outs.writeUTF(message);
					outs.flush();
					// Ajouter le client à la liste des clients
					clients.put(client, pseudo);
					// Afficher dans le serveur la nouvelle connexion
					System.out.println("Nouvelle connexion de : " + pseudo);
					// Afficher sur tout les clients la nouvelle connexion
					message = pseudo + " a rejoint la conversation";
					GestionnaireEnvoieMessageClients gestionnaireEnvoie = new GestionnaireEnvoieMessageClients(clients);
					gestionnaireEnvoie.envoyerMessageAuxClients(message);
					// Démarrer le thread de reception des messages de ce client
					ThreadRecepteurDeMessageClient recepteurMessageDeCeClient = new ThreadRecepteurDeMessageClient(clients,
							client);
					recepteurMessageDeCeClient.start();
					if (typeExecution == "executerUneFois") {
						execution = false;
					}
				}
				else {
					// On envoie au client que le pseudo est incorrecte
					String message = "INVALIDE";
					DataOutputStream outs = new DataOutputStream(client.getOutputStream());
					outs.writeUTF(message);
					outs.flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
