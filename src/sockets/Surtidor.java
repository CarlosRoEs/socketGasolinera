
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import sockets.Cliente;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author FOC
 */
public class Surtidor {

    private Socket socketSurtidor;
    private ServerSocket servidorSurtidor;
    private static final int PUERTO_SINCRONO = 5555;
    

    public void surtidor() {

        try {
            servidorSurtidor = new ServerSocket(PUERTO_SINCRONO);
            Thread hilo = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
//                            Pintamos de verde la salida por consola del surtidor.
                            System.out.println("\033[32mEsperando cliente...");
                            socketSurtidor = servidorSurtidor.accept();

//            Creamos los flujos de entrada y salida de datos.
                            DataOutputStream enviarOrden = new DataOutputStream(socketSurtidor.getOutputStream());
                            DataInputStream recibirConfirmacion = new DataInputStream(socketSurtidor.getInputStream());

                            String orden = recibirConfirmacion.readUTF();
                            System.out.println("\033[34m" +  orden);

                            enviarOrden.writeUTF("Conexión establecida con el surtidor.");
                        } catch (IOException ex) {
                            Logger.getLogger(Surtidor.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                }
            });
            hilo.start();
        } catch (IOException ex) {
            Logger.getLogger(Surtidor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void main(String[] args) {

        Surtidor surtidor = new Surtidor();
        surtidor.surtidor();
    }
}
