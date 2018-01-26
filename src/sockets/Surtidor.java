package sockets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import sockets.Caja.ExtendsLlenadoDeposito;

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

//    Atributo.
    private int socket;
    
    private Socket socketSurtidor;
    private ServerSocket servidorSurtidor;
    private static final int PUERTO_SINCRONO = 5555;
    private static final int PUERTO_SINCRONO_1 = 5555;
    
//    ExtendsLlenadoDeposito ce;

    public Surtidor() {
    }

    public Surtidor(int socket) {
        this.socket = socket;
        
    }

    public int getSocket() {
        Cliente cliente = new Cliente();
        if (cliente.getSocket() == 5555){
            return PUERTO_SINCRONO;
        }else{
            return PUERTO_SINCRONO_1;
        }
//        return socket;
    }

    public void setSocket(int socket) {
        this.socket = socket;
    }
    
    @Override
    public String toString(){
        return String.valueOf(getSocket());
    }

    public void surtidor() {

        
            //        try {
            try {
            servidorSurtidor = new ServerSocket(socket);
        } catch (IOException ex) {
            Logger.getLogger(Surtidor.class.getName()).log(Level.SEVERE, null, ex);
        }
//            Thread hilo = new Thread(new Runnable() {
////                @Override
//                public void run() {
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
                        
                           new ExtendsLlenadoDeposito(socket).start();

                    }
                }
////            });
////            hilo.start();
////        } catch (IOException ex) {
////            Logger.getLogger(Surtidor.class.getName()).log(Level.SEVERE, null, ex);
////        }

    }

    public static void main(String[] args) {

        Surtidor surtidor = new Surtidor();
        surtidor.surtidor();
    }
}
