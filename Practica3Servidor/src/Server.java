import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    //estoy repitiendo esto
    //private ServerSocket serverSocket = new ServerSocket(8899);

      public static void server(ServerSocket serverSocket) throws IOException {
          ListaClientes.initList();
          while(true){
              //aceptar conexiones exteriores pendiente de conexiones de nuevos clientes
              Socket misocket = serverSocket.accept();
              //constructor clienteConector
              ClienteConectado clienteConectado = new ClienteConectado(misocket);
              Thread hiloCliente = new Thread(clienteConectado);
              hiloCliente.start();
          }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket =  new ServerSocket(ConstantesServidor.PUERTO_ESCUCHA);
        System.out.println("Iniciando el servidor\n");
        server(serverSocket);

    }

}
