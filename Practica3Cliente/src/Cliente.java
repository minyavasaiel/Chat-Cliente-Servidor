import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Cliente {
    private final int NUMPUERTO = 8899;
    private static String dirDestino = null; //args0
    private ServerReceived serverReceived;
    private ServerSender serverSender;
    private static Socket socket;
    private static String nick; //args1

    public static String getNick() {
        return nick;
    }

    public static void setNick(String nick) {
        Cliente.nick = nick;
    }
//el cliente se ejecuta "java Cliente<dir_server> <nick>
    //después de enviar #salir hay que poner a dormir aquí al cliente para que le de tiempo a recibir los mensajes de server que si no da excepción


    public Cliente (String nick) throws IOException {
        this.nick = nick;
        serverReceived = new ServerReceived(new DataInputStream(socket.getInputStream()));
        serverSender =  new ServerSender(new DataOutputStream(socket.getOutputStream()));

        Thread hiloSR = new Thread(serverReceived);
        hiloSR.start();
    }

    public void runing() throws IOException, InterruptedException {
        Thread hiloSS = new Thread(this.serverSender);
        hiloSS.start();
        hiloSS.join();
        //¿Tras esto para la entrada(received)
       // hiloSR.stop();
    }

    public void shutdown() throws IOException {
       // ServerReceived.stopCliente();
       // ServerSender.stopCliente();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        dirDestino = args[0];
        nick = args[1];
        socket =  new Socket(dirDestino, 8899);
        Cliente cliente = new Cliente(nick);
        cliente.runing();
       }

    }



