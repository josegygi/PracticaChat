package Chat;

import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class ClienteChat extends JFrame implements ActionListener, Runnable {
	private Socket socket;
	private DataInputStream fentrada;
	private DataOutputStream fsalida;
	private String nombre;
	private static JTextField mensaje = new JTextField();
	private static JTextArea textarea;
	private JButton enviar = new JButton("Enviar");
	private JButton salir = new JButton("Salir");
	private boolean repetir = true;

	private ClienteChat(Socket s, String nombre) {
		super(" CONEXION DEL CLIENTE CHAT: " + nombre);
		setLayout(null);

		mensaje.setBounds(10, 10, 400, 30);
		add(mensaje);

		textarea = new JTextArea();
		JScrollPane scrollpane1 = new JScrollPane(textarea);
		scrollpane1.setBounds(10, 50, 400, 300);
		add(scrollpane1);

		enviar.setBounds(420, 10, 100, 30);
		add(enviar);
		salir.setBounds(420, 50, 100, 30);
		add(salir);

		textarea.setEditable(false);
		enviar.addActionListener(this);
		salir.addActionListener(this);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		socket = s;
		this.nombre = nombre;
		try {
			fentrada = new DataInputStream(socket.getInputStream());
			fsalida = new DataOutputStream(socket.getOutputStream());
			String texto = "Entra en el Chat ... " + nombre;
			fsalida.writeUTF(texto);
		} catch (IOException e) {
			System.out.println("ERROR DE E/S");
			e.printStackTrace();
			System.exit(0);
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == enviar) {

			if (mensaje.getText().trim().length() == 0)
				return;
			String texto = nombre + ": " + mensaje.getText();

			try {
				mensaje.setText("");
				fsalida.writeUTF(texto);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == salir) {
			String texto = "Abandona el Chat ... " + nombre;
			try {
				fsalida.writeUTF(texto);
				fsalida.writeUTF("*");
				repetir = false;
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void run() {
		String texto;
		while (repetir) {
			try {
				texto = fentrada.readUTF();
				textarea.setText(texto);

			} catch (IOException e) {

				JOptionPane.showMessageDialog(null, "IMPOSIBLE CONECTAR CON EL SERVIDOR\n" + e.getMessage(),
						"<<MENSAJE DE ERROR:2>>", JOptionPane.ERROR_MESSAGE);
				repetir = false;
			}
		}

		try {
			socket.close();
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		int puerto = 44444;
		Socket s;

		String nombre = JOptionPane.showInputDialog("Introduce tu nombre o nick:");



		if (nombre.trim().length() == 0) {
			System.err.println("El nombre esta vacio....");
			return;
		}else if(!nombre.matches("[A-ZÑÁÉÍÓÚ][a-zñáéíóúü]*")) {
            System.err.println("El nombre tiene que comenzar por mayúscula (no se admiten segundos nombres)");
            return;
        }
		try {
			s = new Socket("localhost", puerto);
			ClienteChat cliente = new ClienteChat(s, nombre);
			cliente.setBounds(0, 0, 540, 400);
			cliente.setVisible(true);
			new Thread(cliente).start();
			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "IMPOSIBLE CONECTAR CON EL SERVIDOR\n" + e.getMessage(),
					"<<MENSAJE DE ERROR:1>>", JOptionPane.ERROR_MESSAGE);
		}
		
	}
}
