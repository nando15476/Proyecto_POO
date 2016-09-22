import java.util.logging.Level;
import java.util.logging.Logger;

import controlador.IMiembro;
import controlador.MariaDBPool;
import modelo.Administrador;
import modelo.Alumno;

public class Principal {
    private static final Logger LOGGER = Logger.getLogger(Principal.class.getCanonicalName());

    /**
     * Este método comienza el programa. Si bien no es la {@link vista}, se usa para
     * incializarla, junto con la base de datos y otras operaciones previas.
     *
     * @param args argumentos del sistema operativo
     */
    public static void main(final String... args) {
        assert false;
        try {
            // Inicializa (sin instanciar) el ENUM MariaDBPool, esto ejecuta el inicializador
            // estático.
            Class.forName(MariaDBPool.class.getCanonicalName());
        } catch (final ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "No se puede cargar la clase.", e);
        }
        try (IMiembro x = Alumno.getAlumnoById(14287);
             final IMiembro y = Alumno.getAlumnoById(15322)) {
            x.obtenerDesdeBaseDeDatos();
            y.setNombres("Jojo");
            y.setPrimerApellido("Kate");
            y.setSegundoApellido("Kilo");
            y.setCorreo("hotmail.com", "mimimi21312");
            y.makeCorreoU();
            y.guardarEnBaseDeDatos(new Administrador());

        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}