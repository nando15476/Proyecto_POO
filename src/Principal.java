import java.util.logging.Logger;

public class Principal {
    private static final Logger LOGGER = Logger.getLogger(Principal.class.getCanonicalName());

    /**
     * Este método comienza el programa. Si bien no es la {@link vista}, se usa para
     * incializarla, junto con la base de datos y otras operaciones previas.
     */
    public static void main(String[] args) {
        if(MariaDBPool.getConexion() != null){
            System.out.println("Conexión de la base de datos creada correctamente.");
        }
    }
}