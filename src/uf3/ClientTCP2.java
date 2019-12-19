package uf3;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientTCP2 {
	static class Connexio implements Runnable {

		Socket client;
		String nom;
		PrintWriter fsortida;

		public Connexio(Socket client, String nom, PrintWriter fsortida) {
			this.client = client;
			this.nom = nom;
			this.fsortida = fsortida;
		}

		@Override
		public void run() {

			//FLUX D'ENTRADA AL SERVIDOR
			BufferedReader fentrada = null;
			try {
				fentrada = new BufferedReader(new InputStreamReader(client.getInputStream()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//FLUX PER A ENTRADA ESTÀNDARD
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

			String cadena = null, eco = "";

			System.out.println("Introdueix la cadena: ");
			//Lectura teclat
			try {
				cadena = in.readLine();

				if (cadena.equals("exit")) {
					fsortida.println(cadena);
					fsortida.close();
					fentrada.close();
					System.out.println("Finalització de l'enviament...");
					in.close();
					client.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			while (!cadena.equals("exit")) {

				//Enviament cadena al servidor
				fsortida.println(cadena);
				//Rebuda cadena del servidor
				try {
					eco = fentrada.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("  =>ECO: "+eco);
				//Lectura del teclat
				try {
					cadena = in.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

	}

	public static void main (String[] args) throws Exception {

		Scanner scan = new Scanner(System.in);

		System.out.println("Itrodueix un nom:");
		String nom = scan.next();

		String host = "localhost";
		int port = 60000;//Port remot
		Socket client = new Socket(host, port);

		//FLUX DE SORTIDA AL SERVIDOR
		PrintWriter fsortida;
		fsortida = new PrintWriter(client.getOutputStream(), true);
		fsortida.println(nom);
		Connexio connexio = new Connexio(client, nom, fsortida);
		Thread fil = new Thread(connexio, nom);
		fil.start();


	}

}