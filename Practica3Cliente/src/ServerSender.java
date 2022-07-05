import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class ServerSender extends Thread{
    private DataOutputStream out;
    private boolean active;
    private BufferedReader keyboard; //para leer el teclado
    Scanner sc = new Scanner(System.in, "ISO-8859-1");

    public ServerSender (DataOutputStream out){
        //inicializa el stream
        this.out=out;
        try {
            out.flush();
        } catch (IOException e) {
            //e.printStackTrace();
            System.err.println(e);
        }
        active=true;
    }

    @Override
    public void run() {
        try {
            out.writeUTF(Cliente.getNick());
        } catch (IOException e) {
            //e.printStackTrace();
            System.err.println(e);
        }
        while (active){
           //leer buffer teclado
           InputStreamReader isr = new InputStreamReader(System.in);
           BufferedReader br = new BufferedReader (isr);
           String cadena = sc.nextLine();
           //lo escribe en el stream, pero comprobar si no es salir
            if(cadena!="#salir"){
                try {
                    out.writeUTF(cadena);
                    out.flush();
                } catch (IOException e) {
                   // e.printStackTrace();
                    System.err.println(e);
                }
            } else {
                try {
                    out.writeUTF(cadena);
                    out.flush();
                } catch (IOException e) {
                    //e.printStackTrace();
                    System.err.println(e);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                    System.err.println(e);
                } finally {
                    stopCliente();
                    try {
                        out.close();
                    } catch (IOException e) {
                        //e.printStackTrace();
                        System.err.println(e);
                    }
                }

            }

        }

    }

    public void stopCliente(){
        this.active= false;
    }





}
