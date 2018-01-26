/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sockets;

import java.net.Socket;

/**
 *
 * @author FOC
 */
public class Cliente {
    
    private String nombreCliente;
    private int socket;

    public Cliente() {
    }

    public Cliente(String nombreCliente, int socket) {
        this.nombreCliente = nombreCliente;
        this.socket = socket;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public int getSocket() {
        
        return socket;
    }

    public void setSocket(int socket) {
        this.socket = socket;
    }

    @Override
    public String toString(){ 
        return getNombreCliente();
    }
}
