package sockets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author Carlos Rodríguez Escudero
 */
public class Caja {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Caja.class);

    private Socket socketCajero;
    private static int puerto = 5555;
    private static final String ip = "localhost";

//    Creamos objetos de la clase Cliente.
    Cliente cliente_1 = new Cliente("Cliente 1", puerto);
    Cliente cliente_2 = new Cliente("Cliente 2", puerto);

    public void clienteGasolinera() {

        Scanner sc = new Scanner(System.in);

        LlenarDeposito llenar = new LlenarDeposito();
        clienteGasolineraHilo cliente2 = new clienteGasolineraHilo(cliente_1.toString(), llenar);

        System.out.println("\033[34mLlegán 2 clientes.\n¿A qué cliente quieres atender primero?");

        String cliente = sc.next();
        log.debug("Dato almacenado en la variable cliente:" + cliente);
        boolean clienteCorrecto = false;
        while (!clienteCorrecto) {
            switch (cliente) {
                case "cliente_1":
                    llenar.cliente(cliente_1.toString());
                    System.out.println(cliente_1.toString());
                    clienteCorrecto = true;
                    cliente2.start();
                     {
                        try {
                            cliente2.join();
                        } catch (InterruptedException ex) {
                            log.error("Hilo 1 interrumpido.", ex);
                        }
                    }
                    log.info(cliente_2.toString());
                    break;

                case "cliente_2":
                    cliente2.start();
                     {
                        try {
                            cliente2.join();
                        } catch (InterruptedException ex) {
                            log.error("Hilo 2 interrumpido.", ex);
                        }
                    }
                    clienteCorrecto = true;
//                        Comienza cliente 1
                    llenar.cliente(cliente_1.toString());
                    log.info(cliente_1.toString());
                    break;

                default:
                    log.info("\033[34mPor favor, debe seleccionar un cliente correcto.");
                    log.info("\033[34mSeleccione cliente:");
                    cliente = sc.next();
                    break;
            }
        }
    }

    class LlenarDeposito {

        public synchronized void cliente(String nombreCliente) {
            Scanner sc = new Scanner(System.in);
            int importeAPagar = 0;
            String mensajeEntrante = null;
            String mensajeSaliente = null;

            try {
                socketCajero = new Socket(ip, puerto);

//            Creamos los flujos de entrada y salida de datos.
                DataOutputStream enviar = new DataOutputStream(socketCajero.getOutputStream());
                DataInputStream recibir = new DataInputStream(socketCajero.getInputStream());

                mensajeSaliente = "\033[34mEstableciendo conexión con el sutidor.";
                enviar.writeUTF(mensajeSaliente);

//                Conexión establecida con el surtidor
                log.info(recibir.readUTF());

                do {
                    log.info("\033[34m¿Cuanto desea pagar?");
                    importeAPagar = sc.nextInt();

                    if (importeAPagar <= 0) {
                        log.info("\033[34mPorfavor realice un pago para llenar el deposito.");
                    } else {
                        enviar.writeInt(importeAPagar);
                    }
                } while (importeAPagar <= 0);

//               Manguera descolgada  
                mensajeEntrante = recibir.readUTF();
                log.info(mensajeEntrante);

//              Gatillo presionado  
                mensajeEntrante = recibir.readUTF();
                log.info(mensajeEntrante);

//                Llenando deposito...  
                mensajeEntrante = recibir.readUTF();
                log.info(mensajeEntrante);

//                Gatillo liberado  
                mensajeEntrante = recibir.readUTF();
                log.info(mensajeEntrante);

//                Manguera colgada.  
                mensajeEntrante = recibir.readUTF();
                log.info(mensajeEntrante);

                mensajeSaliente = "\033[34m¿Terminado.?";
                enviar.writeUTF(mensajeSaliente);

//                Terminado. OK  
                mensajeEntrante = recibir.readUTF();
                log.info(mensajeEntrante);

                log.info("\033[34mSurtidor cerrado.");

//                Cerrando los flujos.
                recibir.close();
                enviar.close();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    class clienteGasolineraHilo extends Thread {

        private String nombreCliente;
        LlenarDeposito llenadoDeposito;
        protected int socket;

        public clienteGasolineraHilo(String nombreCliente, LlenarDeposito llenadoDeposito) {
            this.nombreCliente = nombreCliente;
            this.llenadoDeposito = llenadoDeposito;
        }

        @Override
        public void run() {
            LlenarDeposito llenar = new LlenarDeposito();
            llenar.cliente(cliente_1.toString());
        }
    }

    public static void main(String[] args) {

        PropertyConfigurator.configure("log4j.properties");
        
        Caja caja = new Caja();
        caja.clienteGasolinera();
    }
}
