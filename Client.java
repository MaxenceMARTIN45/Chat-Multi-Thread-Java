package projet.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	
	private static Socket client;
	private static Scanner sc;
	private static DataOutputStream outs;
	private static ThreadReceptionMessageServeur receptionMessage;

	static void setClient(Socket newClient) {
		Client.client = newClient;
	}
	
	static void setSc(Scanner newSc) {
		sc = newSc;
	}
	
	static void setOuts(DataOutputStream nexOuts) {
		outs = nexOuts;
	}
	
	static void setReceptionMessage(ThreadReceptionMessageServeur newReceptionMessage) {
		receptionMessage = newReceptionMessage;
	}
	
	//@SuppressWarnings("resource")
	public static void main(String[] args) {
		try {
			
			boolean pseudoValide = false;
			while (!pseudoValide) {
				// On le met dans la boucle parce que sinon raccroche à chaque fois à la fin du tour de boucle
				Client.setClient(new Socket("localhost", 7000));
				//Socket client = new Socket("localhost", 7000);
				//System.out.print("Entrer votre pseudo : ");
				Client.setSc(new Scanner(System.in));
				//@SuppressWarnings("resource")
				//Scanner sc = new Scanner(System.in);
				Client.setOuts(new DataOutputStream(client.getOutputStream()));
				//DataOutputStream outs = new DataOutputStream(client.getOutputStream());
				DataInputStream ins = new DataInputStream(client.getInputStream());
				// Attend que client rentre un pseudo
				System.out.print("Entrer votre pseudo : ");
				String pseudo = (String) sc.nextLine();
				// Envoie le pseudo
				outs.writeUTF(pseudo);
				// Reception de la réponse serveur : OK = pseudo valide, INVALIDE = pseudo déjà pris
				String message = (String) ins.readUTF();
				if (message.equals("OK")) {
					pseudoValide = true;
					Client.setReceptionMessage(new ThreadReceptionMessageServeur(client));
					receptionMessage.start();
				}
				else {
					System.out.println("Pseudo invalide, veillez choisir un speudo qui n'est pas utilisé.");
					client.close();
				}
			}
			boolean boucle = true;
			while (boucle) {
				// System.out.println("attend ici");
				String message = (String) sc.nextLine();
				// System.out.println("n'a pas attendu");
				outs.writeUTF(message);
				if (message.equals("exit")) {
					boucle = false;
					// Fermer le thread de réception des messages du serveur
					//receptionMessage.stopThread();
				}
			}
			client.close();
			//sc.close();
		} catch (IOException e) {
			System.out.println("Le serveur s'est déconnecté de manière impromptu.");
			try {
				client.close();
			} catch (IOException e1) {
				System.out.println("Le socket client n'a pas réussi à être déconnecté.");
			}
		}
	}

}
