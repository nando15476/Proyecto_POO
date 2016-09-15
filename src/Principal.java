import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import controlador.MariaDBPool;

public class Principal {
    private static final Logger LOGGER = Logger.getLogger(Principal.class.getCanonicalName());

    /**
     * Este método comienza el programa. Si bien no es la {@link vista}, se usa para
     * incializarla, junto con la base de datos y otras operaciones previas.
     */
    public static void main(final String... args) {
        try {
            byte [] lol = MessageDigest.getInstance("SHA-512").digest("LOL".getBytes());
            System.out.println(lol);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            Class.forName(MariaDBPool.class.getCanonicalName());
            MariaDBPool.makeBaseDeDatos();
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "No se puede cargar la clase.", e);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "No se puede establecer una conexión con SQL.", e);
        }

    }
}