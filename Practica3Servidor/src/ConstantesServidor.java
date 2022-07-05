public interface ConstantesServidor {
    public static final int PUERTO_ESCUCHA= 8899;
    public final String CMD_HELP = "Lista de comandos: \n #ayuda: muestra la ayuda. \n #listar: lista de usuarios conectados. \n #charlar <nick>: empiezas conversacion con esa persona. \n #salir: cierras conexion.";
    public final String CLIENT_DISC_ERROR = "[ERROR] Tu interlocutor se ha desconectado.";
    public final String CLIENT_DISC_ERROR2 = "[ERROR] No tienes ninguna conversacion iniciada. Usa el comando #charlar <nickname> para iniciar una conversacion.";
    public final String CLIENT_NO_CONVERR = "[ERROR] No se ha podido enviar el mensaje.";
    public final String CMD_CHARLA_ERR = "[ERROR] Error de comando. Usa #charlar <nickname>\n donde nickname es...";
    public final String CMD_CHARLA_OK = "Conversacion iniciada con ";
    public final String CMD_ERR= "[ERROR] Comando no reconocido. Escriba #ayuda para ver la lista de comandos";
    public final String BYE_MSG= "El servidor te dice: Hasta pronto!";

}
