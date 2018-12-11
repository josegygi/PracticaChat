package Chat;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

class ComunHilos {
	 private int conexiones;
	 private int actuales;
     private Socket tabla[];
	 private String mensajes;
	
	ComunHilos(int actuales, int conexiones, Socket[] tabla) {
	    this.actuales = actuales;
		this.conexiones = conexiones;
		this.tabla = tabla;  
		mensajes="";        
	}

    int getConexiones() { return conexiones;	}
	synchronized void setConexiones(int conexiones) {
        this.conexiones = conexiones;
	}

	String getMensajes() { return mensajes; }
	synchronized void setMensajes(String mensajes) {
		this.mensajes = mensajes;
	}
	
	int getActuales() { return actuales; }
	synchronized void setActuales(int actuales) {
        this.actuales = actuales;
	}

	synchronized void addTabla(Socket s, int i) {
		tabla[i] = s;
	}	
	Socket getElementoTabla(int i) { return tabla[i]; }

	public void guardarMensajes(){

        System.out.println(getMensajes());

        String chat = getMensajes().replaceAll("\n","  ");

        ProcessBuilder pb = new ProcessBuilder("cmd", "/C","powershell echo '"+ chat +"'> C:\\\\java_code\\\\PracticaPSP2\\\\historialCompleto.txt\"");
        try
        {
            Process process = pb.start();
            TimeUnit.SECONDS.sleep(10);
            if (process.exitValue() != 0)
            {
                System.err.println("Error al guardar el historial");
            }
        }
        catch (IOException | InterruptedException e)
        {
            System.err.println(e.getMessage());
        }

    }
		
}
