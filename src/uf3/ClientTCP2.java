package uf3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientTCP2 {

	static class Connexio implements Runnable {

		BufferedReader fentrada;

		public Connexio(BufferedReader fentrada) {
			this.fentrada = fentrada;
		}

		@Override
		public void run() {

			boolean exit = false;
			while(!exit) {
				try {
					System.out.println(fentrada.readLine());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					exit = true;
				}
			}

		}

	}

	public static void main (String[] args) throws Exception {

		Scanner scan = new Scanner(System.in);

		System.out.println("Introdueix un nom:");
		String nom = scan.next();

		String host = "localhost";
		int port = 60000;//Port remot
		Socket client = new Socket(host, port);

		//FLUX DE SORTIDA AL SERVIDOR
		PrintWriter fsortida;
		fsortida = new PrintWriter(client.getOutputStream(), true);
		fsortida.println(nom);
		//FLUX D'ENTRADA AL SERVIDOR
		BufferedReader fentrada = null;
		fentrada = new BufferedReader(new InputStreamReader(client.getInputStream()));

		Connexio connexio = new Connexio(fentrada);
		Thread fil = new Thread(connexio, nom);
		fil.start();

		//FLUX PER A ENTRADA ESTÀNDARD
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		String cadena = null, eco = "";

		System.out.println("Introdueix la cadena: ");
		//Lectura teclat
		cadena = in.readLine();

		while (!cadena.equals("exit")) {

			//Enviament cadena al servidor
			fsortida.println(cadena);
			//Rebuda cadena del servidor
			eco = fentrada.readLine();
			System.out.println(eco);
			//Lectura del teclat
			cadena = in.readLine();

		}
		
		fsortida.println(cadena);
		fsortida.close();
		fentrada.close();
		System.out.println("Finalització de l'enviament...");
		in.close();
		client.close();

	}

}