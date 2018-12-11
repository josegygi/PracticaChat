package Chat;

import java.io.*;
import java.net.*;

public class ServidorChat  {
	private static final int MAXIMO = 5;
	
	public static void main(String args[]) throws IOException {

        int PUERTO = 44444;

        ServerSocket servidor;

        servidor = new ServerSocket(PUERTO);

        System.out.println("Servidor iniciado...");

        Socket tabla[] = new Socket[MAXIMO];
        ComunHilos comun = new ComunHilos(0, 0, tabla);

            try {

            while (comun.getConexiones() < MAXIMO) {
                Socket socket;

                    socket = servidor.accept();
                    comun.addTabla(socket, comun.getConexiones());
                    comun.setActuales(comun.getActuales() + 1);
                    comun.setConexiones(comun.getConexiones() + 1);

                    HiloServidorChat hilo = new HiloServidorChat(socket, comun);
                    hilo.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        comun.guardarMensajes();
        servidor.close();

	}
}

