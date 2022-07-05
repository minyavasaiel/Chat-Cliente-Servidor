import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

public class ClienteConectado extends Thread{
    private String nick;
    private Long id; //manejar clientes con mismo nick
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private Long idConversante; //con el que se está hablando

    public ClienteConectado (Socket socket) throws IOException {
        this.socket = socket;
        this.in= new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
    }

    public void run() {
        try {
            String nickLlega = "";
            boolean nickOk = false;
            do {
                nickLlega = this.in.readUTF();
                if (!ListaClientes.comprobarNick(nickLlega)) {
                    //System.out.println("El boolean en if es: "+ListaClientes.comprobarNick(nickLlega));
                    this.out.writeUTF("El nick " + nickLlega + " ya esta escogido. Por favor, escoja otro.");
                    nickOk = false;
                } else {
                    //System.out.println("El boolean en else es: "+ListaClientes.comprobarNick(nickLlega));
                    nickOk = true;
                }
            } while (!nickOk);
            this.setNick(nickLlega);
            //El id lo meto en la siguiente función
            ListaClientes.addCliente(this);
            System.out.println("Agregado cliente " + this.getNick() + " con direccion " + this.socket.getInetAddress());
            this.out.writeUTF("Hola, " + nick);
            this.out.writeUTF("Conectados: " + ListaClientes.getNumConectados());
        } catch (SocketException e) {
            System.err.println("[Error] Fallo en el socket. " + e);
        } catch (IOException e) {
            //e.printStackTrace();
            System.err.println(e);
        }
        try {
            out.writeUTF("Escribe tus mensajes.");
        } catch (SocketException e) {
            System.err.println("[Error] Fallo en el socket. " + e);
        } catch (IOException e) {
            //e.printStackTrace();
            System.err.println(e);
        }
        String mensaje = "";
        boolean salir = false;
        do {
            try {
                mensaje = this.in.readUTF();
            } catch (SocketException e) {
                System.err.println("[Error] Fallo en el socket. " + e);
            } catch (IOException e) {
                //e.printStackTrace();
                System.err.println(e);
            }
            if (mensaje.charAt(0) != '#') {
                if (idConversante != null) {
                    mensaje = ">" + this.getNick() + ": " + mensaje;
                    if (ListaClientes.enviarMensaje(idConversante, mensaje)) {
                    } else {
                        try {
                            out.writeUTF(ConstantesServidor.CLIENT_DISC_ERROR);
                        } catch (SocketException e) {
                            System.err.println("[Error] Fallo en el socket. " + e);
                        } catch (IOException e) {
                            // e.printStackTrace();
                            System.err.println(e);
                        }
                    }
                } else {
                    try {
                        out.writeUTF(ConstantesServidor.CLIENT_DISC_ERROR2);
                    } catch (SocketException e) {
                        System.err.println("[Error] Fallo en el socket. " + e);
                    } catch (IOException e) {
                        //e.printStackTrace();
                        System.err.println(e);
                    }
                }
            }else if (mensaje==null){
                System.err.println("[Error] El cliente se ha desconectado." );
                salir= true;
            } else {
                procesarComando(mensaje);
            }
            if (mensaje.equals("#salir")) {
                salir = true;
            }
        } while (!salir);
    }

    public void enviarRespuesta(String ans){
        //respuesta hacia el clienteA desde el B
        try {
            out.writeUTF(ans);
        } catch (IOException e) {
           // e.printStackTrace();
            System.err.println(e);
        }
    }

    public void procesarComando(String cmd){
        //cadenas qe comienzan por # se procesa y se responde
        String[] parts = cmd.split(" ");
        switch(parts[0]) {
            case "#ayuda":
                String mensajeAyuda="";
                if (this.getIdConversante()==null){
                    mensajeAyuda="No estas conversando con nadie.\n";
                } else {
                    mensajeAyuda = "Estas conversando con " + ListaClientes.getNickById(this.idConversante )+". Usando el nick de "+this.nick+" \n";
                }
                //devuelve una lista con todos los comandos disponibles
                try {
                    this.out.writeUTF( mensajeAyuda+ConstantesServidor.CMD_HELP);
                } catch (IOException e) {
                    //e.printStackTrace();
                    System.err.println(e);
                }
                break;
            case "#listar":
                //devuelve una lista con todos los nicknames
                try {
                    this.out.writeUTF( "En este momento hay conectados "+ListaClientes.getNumConectados()+": "+ ListaClientes.getNames());
                } catch (IOException e) {
                    //e.printStackTrace();
                    System.err.println(e);
                }
                break;
            case "#charlar":
                //#charlar <nickname>: Establece una conversación con el usuario indicado en <nickname>
                this.idConversante = ListaClientes.getIdByNick(parts[1]);
                if (this.idConversante==null){
                    try {
                        out.writeUTF("[ERROR] El usuario "+parts[1]+" no se encuentra conectado. Usa #listar para ver quien esta conectado.");
                    } catch (IOException e) {
                        System.err.println(e);
                    }
                } else if (this.idConversante==this.id) {
                    try {
                        out.writeUTF("[ERROR] No hable consigo mismo. Usa #listar para ver quien esta conectado.");
                    } catch (IOException e) {
                        System.err.println(e);
                    }
                }else {
                    try {
                        out.writeUTF(ConstantesServidor.CMD_CHARLA_OK+ ListaClientes.getNickById(idConversante)+". Escribe para hablarle.");
                    } catch (IOException e) {
                        System.err.println(e);
                    }
                    System.out.println("El cliente " + this.getId() + " " + this.getNick() + " ha abierto conversacion con " + idConversante);
                }
                break;
            case "#salir":
                try {
                    out.writeUTF(ConstantesServidor.BYE_MSG);
                } catch (IOException e) {
                   // e.printStackTrace();
                    System.err.println(e);
                }
                ListaClientes.deleteClientById(this.id);
                System.out.println("Desconectado cliente "+this.nick+" con direccion "+this.socket.getInetAddress());
                //cierra conexión con servidor
                break;
            default:
                //devolver que no se ha entendido el comando
                try {
                    this.out.writeUTF( ConstantesServidor.CMD_ERR);
                } catch (IOException e) {
                    //e.printStackTrace();
                    System.err.println(e);
                }
                break;
        }
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Socket getSocket() {
        return socket;
    }

    public Long getIdConversante(){
        return idConversante;
    }

    public void setIdConversante(Long idConversante) {
        this.idConversante = idConversante;
    }

    @Override
    public String toString() {
        return "ClienteConectado{" +
                "nick='" + nick + '\'' +
                ", id=" + id +
                ", socket=" + socket +
                ", in=" + in +
                ", out=" + out +
                ", idConversante=" + idConversante +
                '}';
    }
}
