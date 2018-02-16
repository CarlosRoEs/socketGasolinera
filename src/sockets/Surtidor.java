package sockets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author Carlos Rodríguez Escudero
 */
public class Surtidor {
    
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Surtidor.class);

    private ServerSocket servidorSurtidor = null;
    private static int puerto = 5555;
    private static int idSocket = 1;

    /**
     * Realiza la conexión del servidor.
     */
    public void surtidor() {
        
        log.info("El surtidor usa el puerto 5555.");
        log.info("Iniciando servidor...");
        try {
            servidorSurtidor = new ServerSocket(puerto);
            
        } catch (IOException ex) {
            log.error("El surtidor no ha podido realizar la conexión.", ex);
        }
        
        while (true) {
            
            log.info("Esperando cliente...");
            Socket socketSurtidor;
            
            try {
                socketSurtidor = servidorSurtidor.accept();
                
//                Creamos los hilos para la entrada de los clientes.
                SurtidorHilo nuevoCliente = new SurtidorHilo(socketSurtidor);
                nuevoCliente.start();
                
                log.info("Nueva conexión entrante id: " + idSocket + "(" + puerto + ")");
                
                try {
                    nuevoCliente.join();
                } catch (InterruptedException ex) {
                    log.error("Hilo interrumpido.", ex);
                }
                
            } catch (IOException ex) {
                log.error("Error en el puerto de entrada.", ex);
            }
        }
    }
    
    static class SurtidorHilo extends Thread {
        
        private Socket socketSurtidor;
        
        public SurtidorHilo(Socket socketSurtidor) {
            this.socketSurtidor = socketSurtidor;
        }
        
        @Override
        public void run() {
            
            int importePagado = 0;
            String mensajeEntrante = null;
            String mensajeSaliente = null;
            
            try {
                
                DataOutputStream enviar;
                DataInputStream recibir;
                enviar = new DataOutputStream(socketSurtidor.getOutputStream());
                recibir = new DataInputStream(socketSurtidor.getInputStream());
//                Estableciendo conexión con el sutidor.
                mensajeEntrante = recibir.readUTF();
                log.info("\033[34m" + mensajeEntrante);
                
                mensajeSaliente = "Conexión establecida con el surtidor.\nSurtidor Abierto.";
                enviar.writeUTF(mensajeSaliente);
                
                importePagado = recibir.readInt();
                log.debug("El importe de la variable es " + importePagado);
                log.info("\033[34m" + importePagado);
                
                mensajeSaliente = "Manguera descolgada";
                enviar.writeUTF(mensajeSaliente);
                
                mensajeSaliente = "Gatillo presionado";
                enviar.writeUTF(mensajeSaliente);
                
                if (importePagado > 0) {
                    for (int i = 1; i <= importePagado; i++) {
                        log.info("Llenando deposito " + i);
                    }                    
                } else {
                    log.info("Importe introducido no valido.");
                    
                }
                
                mensajeSaliente = "Llenando deposito...";
                enviar.writeUTF(mensajeSaliente);
                
                mensajeSaliente = "Gatillo liberado";
                enviar.writeUTF(mensajeSaliente);
                
                mensajeSaliente = "Manguera colgada.";
                enviar.writeUTF(mensajeSaliente);

//                ¿Terminado.?
                mensajeEntrante = recibir.readUTF();
                log.info("\033[34m" + mensajeEntrante);
                
                mensajeSaliente = "Terminado. OK";
                enviar.writeUTF(mensajeSaliente);
                
                log.info("Cerrando surtidor...");

//                Cerrando los flujos.
                recibir.close();
                enviar.close();
                
            } catch (IOException ex) {
                log.error("Error en el flujo de datos.", ex);
            }
            
        }
        
    }
    
    public static void main(String[] args) {
        
        PropertyConfigurator.configure("log4j.properties");
        
        Surtidor surtidor = new Surtidor();
        surtidor.surtidor();
    }
}
