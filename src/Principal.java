import java.util.logging.Level;
import java.util.logging.Logger;

import controlador.IMiembro;
import controlador.MariaDBPool;
import modelo.Administrador;
import modelo.Alumno;

class Principal {
    private static final Logger LOGGER = Logger.getLogger(Principal.class.getCanonicalName());

    /**
     * Este método comienza el programa. Si bien no es la {@link vista}, se usa para
     * incializarla, junto con la Base de Datos y otras operaciones previas.
     *
     * @param args argumentos del sistema operativo
     */
    public static void main(final String... args) {
        try {
            // Inicializa (sin instanciar) el ENUM MariaDBPool, esto ejecuta el inicializador
            // estático.
            Class.forName(MariaDBPool.class.getCanonicalName());
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "No se puede cargar la clase.", e);
        }
        try (IMiembro x = Alumno.getAlumnoById(554);
             final IMiembro y = Alumno.getAlumnoById(3232)) {
            x.invalidar(new Administrador());
            x.setIdentificador(3344);
            System.out.println(x.toString());
            x.guardarEnBaseDeDatos(new Administrador());
            System.out.println(x.toString());
        } catch (final Exception e) {
            System.out.println("¡EXCEPCIÓN!");
            e.printStackTrace(System.out);
        }
    }
}
