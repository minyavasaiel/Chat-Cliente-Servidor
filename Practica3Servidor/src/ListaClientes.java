import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

public class ListaClientes {
    private static List<ClienteConectado> conectados;

    public static void initList(){
        conectados = new ArrayList<ClienteConectado>();
    }

    public static void addCliente(ClienteConectado clienteConectado){
        Long id = null;
        if (conectados.size() == 0) {
           id= Long.valueOf(0);
        } else {
            for (int i=0; i<conectados.size(); i++){
                id=conectados.get(i).getId()+1;
            }
        }
        clienteConectado.setId(id);
        conectados.add(clienteConectado);
    }

    public static int getNumConectados(){
        int numero = conectados.size();
        return numero;
    }

    public static void deleteClientById(Long id){
        System.out.println("El cliente con id "+id+" se quiere desconectar.");
        for (int i=0; i<conectados.size();i++){
            if (id==(conectados.get(i).getId())){
                //borar de lista
                //System.out.println("Tamaño de array antes de borrar: "+conectados.size());
                conectados.remove(conectados.get(i));
               // System.out.println("Tamaño de array después de borrar: "+conectados.size());
                //¿Algo más? Recorrer la lista de todos los clientes buscando si estaba en algun idConversante
                // y si estaba ponerlo a null y ademas enviar un mensaje de aviso en ese cliente que ya no conversa con nadie
            }
        }
        for (int j=0; j<conectados.size(); j++){
            if (conectados.get(j).getIdConversante()==id){
               // System.out.println("Id conversante de "+conectados.get(j).getId()+" antes: "+conectados.get(j).getIdConversante());
                conectados.get(j).setIdConversante(null);
                //System.out.println("Id conversante de "+conectados.get(j).getId()+" después: "+conectados.get(j).getIdConversante());
                conectados.get(j).enviarRespuesta(ConstantesServidor.CLIENT_DISC_ERROR);
            }
        }
    }

    //no entiendo como va esto
    public static String getNames(){
        String cadenaNombres="";
        for (int i=0; i<conectados.size(); i++){
             cadenaNombres= cadenaNombres+" \n"+conectados.get(i).getNick();
        }
        return cadenaNombres;
    }

    public static boolean enviarMensaje(Long id, String mensaje){
        // Recorrer la lista de clientes buscando el que tiene ese id y mandarle el mensaje usando enviarRespuesta
        for (int i =0; i<conectados.size(); i++){
            if (conectados.get(i).getId() ==id ){
                int posicion= ListaClientes.getPositionById(id);
                conectados.get(posicion).enviarRespuesta(mensaje);
                return true;
            }
        }
        return false;
        //envía el mensaje al cliente con el id que le llega como parámetro
    }

    public static int getPositionById(Long id){
        int posicion=-1;
        for (int i=0; i<conectados.size();i++){
            if (id==conectados.get(i).getId()){
                posicion= i;
                return posicion;
            }
        }
        return posicion;
    }

    public static Long getIdByNick(String nick){
        Long id=null;
        for (int i=0; i<conectados.size();i++){
            if ((conectados.get(i).getNick()).equals(nick)){
               id=conectados.get(i).getId();
            }
        }
        return id;
    }

    public static String getNickById(Long id){
        String name = "";
        for (int i=0; i<conectados.size();i++){
            if ((conectados.get(i).getId())==id){
                name=conectados.get(i).getNick();
            }
        }
        return name;
    }

    public static boolean comprobarNick(String nick){
        for (int i=0; i<conectados.size();i++){
            if ((conectados.get(i).getNick()).equals(nick)){
                return false;
            }
        }
        return true;
    }
}
