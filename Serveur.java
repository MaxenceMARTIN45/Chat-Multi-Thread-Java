package projet.chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

public class Serveur {

	@SuppressWarnings("unused")
	private static Hashtable<Socket, String> clients;
	@SuppressWarnings("unused")
	private static ServerSocket socketServeur;

	public static void main(String[] args) {
		try {
			//
			ServerSocket socketServeur = new ServerSocket(7000);
			System.out.println("Le serveur vient de démarrer");
			System.out.println("Le serveur attend des connexions");
			//
			Hashtable<Socket, String> clients = new Hashtable<Socket, String>();
			//
			ThreadGestionnaireConnexion premiereConnexion = new ThreadGestionnaireConnexion(clients, socketServeur,
					"executerUneFois");
			premiereConnexion.start();
			premiereConnexion.join();
			//
			ThreadGestionnaireConnexion gestionnaireConnexion = new ThreadGestionnaireConnexion(clients, socketServeur,
					"executerBoucleInfinie");
			gestionnaireConnexion.start();
			//
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

}
