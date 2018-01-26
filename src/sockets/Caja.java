package sockets;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author FOC
 */
public class Caja {

    private Socket socketCajero;
    private static final int PUERTO_SINCRONO = 5555;
    private static final String ip = "localhost";

//    Creamos objetos de la clase Cliente.
    Cliente cliente_1 = new Cliente("Cliente 1");
    Cliente cliente_2 = new Cliente("Cliente 2");
    
    public void llenando(){
        
        try {
            LlenarDeposito llenar = new LlenarDeposito();
            ExtendsLlenadoDeposito cliente2 = new ExtendsLlenadoDeposito(cliente_1.toString(), llenar);

            cliente2.start();

            llenar.cliente_1(cliente_1.toString());
            
            System.out.println("Llenando deposito del " + cliente_1.toString() + ".");
            
            System.out.println("Deposito lleno " + cliente_1.toString());

            
            cliente2.join();
            System.out.println("Llenando deposito del " + cliente_2.toString() + ".");
            System.out.println("Deposito lleno " + cliente_2.toString());
        } catch (InterruptedException ex) {
            Logger.getLogger(Caja.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    class LlenarDeposito {

        public synchronized void cliente_1(String nombreCliente) {

            try {
                socketCajero = new Socket(ip, PUERTO_SINCRONO);

//            Creamos los flujos de entrada y salida de datos.
                DataOutputStream enviarOrden = new DataOutputStream(socketCajero.getOutputStream());
                DataInputStream recibirConfirmacion = new DataInputStream(socketCajero.getInputStream());

                enviarOrden.writeUTF("Cliente estableciendo conexión.");

                System.out.println("\033[32m" + recibirConfirmacion.readUTF());

            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }

    }

    class ExtendsLlenadoDeposito extends Thread {

        private String nombreCliente;
        LlenarDeposito llenadoDeposito;

        public ExtendsLlenadoDeposito(String nombreCliente, LlenarDeposito llenadoDeposito) {
            this.nombreCliente = nombreCliente;
            this.llenadoDeposito = llenadoDeposito;
        }

        @Override
        public void run() {
            
            llenadoDeposito.cliente_1(nombreCliente);
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Caja.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public static void main(String[] args) {

        Caja caja = new Caja();
        
        caja.llenando();
    }

}
