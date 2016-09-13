import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Principal {
    private static final Logger LOGGER = Logger.getLogger(Principal.class.getCanonicalName());

    /**
     * Este método comienza el programa. Si bien no es la {@link vista}, se usa para
     * incializarla, junto con la base de datos y otras operaciones previas.
     */
    public static void main(final String... args) {
        if (MariaDBPool.getConexion() != null) {
            LOGGER.log(Level.INFO, "Conexión de la base de datos creada correctamente.");
            // Verifica y genera el esqueleto de la base de datos
            try {
                MariaDBPool.makeBaseDeDatos();
            } catch (final SQLException e) {
                LOGGER.log(Level.SEVERE,"No se pudo verificar la base de datos.",e);
            }
        }
    }
}