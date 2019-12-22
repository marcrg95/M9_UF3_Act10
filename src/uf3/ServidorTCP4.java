package uf3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class ServidorTCP4 {

	static class Connexio implements Runnable {
		private Socket clientConnectat;
		private BufferedReader fentrada;
		private String cadena;
		private ServerSocket servidor;
		private ArrayList<PrintWriter> fsortida;

		public Connexio(ServerSocket servidor, Socket clientConnectat, BufferedReader fentrada, ArrayList<PrintWriter> fsortida) {
			this.servidor = servidor;
			this.clientConnectat = clientConnectat;
			this.fentrada = fentrada;
			this.fsortida = fsortida;

		}

		@Override
		public void run() {

			try {
				while ((cadena = fentrada.readLine()) != "") {

					for (int i = 0; i < fsortida.size(); i++) {
						fsortida.get(i).println(Thread.currentThread().getName()+ ": " + cadena);;
					}
					System.out.println("Rebent: " + cadena + " de " + Thread.currentThread().getName());
					if (cadena.equals("exit")) break;

				}

				fentrada.close();
				//fsortida.close();
				clientConnectat.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}


	public static void main (String[] args) throws Exception {
		ArrayList<PrintWriter> fsortida = new ArrayList<PrintWriter>();
		int numPort = 60000;
		ServerSocket servidor = new ServerSocket(numPort);
		for (int i = 0; i < Integer.parseInt(args[0]); i++) {

			String cadena = "";
			System.out.println("Esperant connexió... ");
			Socket clientConnectat = servidor.accept();
			System.out.println("Client connectat... ");

			fsortida.add(new PrintWriter(clientConnectat.getOutputStream(), true));

			BufferedReader fentrada = new BufferedReader(new InputStreamReader(clientConnectat.getInputStream()));
			Connexio connexio = new Connexio(servidor, clientConnectat, fentrada, fsortida);
			String filNom = "";
			filNom = fentrada.readLine();
			Thread fil = new Thread(connexio, filNom);
			fil.start();

		}

	}

}