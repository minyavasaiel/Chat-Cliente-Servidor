import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ServerReceived extends Thread{
    private DataInputStream in;
    private boolean active;


    public ServerReceived( DataInputStream in){
    //inicializa el stream
       this.in=in;
        active= true;
    }

    //run() en bucle infinito
    @Override
    public void run(){
        String mensaje="";
        while(active) {
            try {
                mensaje = in.readUTF();
            } catch (IOException e) {
                //e.printStackTrace();
                System.err.println(e);
            }
            if (mensaje.equals("El servidor te dice: Hasta pronto!")){
                System.out.println(mensaje);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        //e.printStackTrace();
                        System.err.println(e);
                    } finally {
                        stopCliente();
                        try {
                            in.close();
                        } catch (IOException e) {
                            //e.printStackTrace();
                            System.err.println(e);
                        }
                    }
                } else {
                    System.out.println(mensaje);
                }
        }

    }

    public void stopCliente(){
        this.active= false;
    }


}
