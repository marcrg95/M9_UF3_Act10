package uf3;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class ServidorTCP4 {

	private static int i;

	static class Connexio implements Runnable {
		private Socket clientConnectat;
		private BufferedReader fentrada;
		private PrintWriter fsortida;
		private String cadena;
		private ServerSocket servidor;

		public Connexio(ServerSocket servidor, Socket clientConnectat, BufferedReader fentrada) {
			this.servidor = servidor;
			this.clientConnectat = clientConnectat;
			this.fentrada = fentrada;
		}

		@Override
		public void run() {
			//FLUX DE SORTIDA AL CLIENT
			try {
				fsortida = new PrintWriter(clientConnectat.getOutputStream(), true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				while ((cadena = fentrada.readLine()) != "") {

					fsortida.println(cadena);
					System.out.println("Rebent: " + cadena + " de " + Thread.currentThread().getName());
					if (cadena.equals("exit")) break;

				}
				
				fentrada.close();
				fsortida.close();
				clientConnectat.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}


	public static void main (String[] args) throws Exception {

		int numPort = 60000;
		ServerSocket servidor = new ServerSocket(numPort);
		while (true) {

			String cadena = "";
			i++;
			System.out.println("Esperant connexió... ");
			Socket clientConnectat = servidor.accept();
			System.out.println("Client connectat... ");
			BufferedReader fentrada = new BufferedReader(new InputStreamReader(clientConnectat.getInputStream()));
			Connexio connexio = new Connexio(servidor, clientConnectat, fentrada);
			String filNom = "";
			filNom = fentrada.readLine();
			Thread fil = new Thread(connexio, filNom);
			fil.start();

		}

	}

}