import java.util.logging.Level;
import java.util.logging.Logger;

import controlador.MariaDBPool;
import modelo.Miembro;

public class Principal {
    private static final Logger LOGGER = Logger.getLogger(Principal.class.getCanonicalName());

    /**
     * Este método comienza el programa. Si bien no es la {@link vista}, se usa para
     * incializarla, junto con la base de datos y otras operaciones previas.
     */
    public static void main(final String... args) {
        try {
            // Inicializa (sin instanciar) el ENUM MariaDBPool, esto ejecuta el inicializador
            // estático.
            Class.forName(MariaDBPool.class.getCanonicalName());
            // Inicializa (sin instanciar) la clase Miembro, esto ejecuta el inicializador estático.
            Class.forName(Miembro.class.getCanonicalName());
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "No se puede cargar la clase.", e);
        }

    }
}