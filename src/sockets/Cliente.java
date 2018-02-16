
package sockets;

import org.apache.log4j.Logger;

/**
 *
 * @author Carlos Rodríguez Escudero.
 */
public class Cliente {
    
    private static Logger log = Logger.getLogger(Cliente.class);

//    Atributos
    private String nombreCliente;
    private int socket;

//    Constructor
    public Cliente() {
    }

    public Cliente(String nombreCliente, int socket) {
        this.nombreCliente = nombreCliente;
        this.socket = socket;
    }

//    Métodos getters y setters
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
    public String toString() {
        return getNombreCliente();
    }
}
