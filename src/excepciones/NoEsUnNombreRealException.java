package excepciones;

/**
 * Excepción que se lanza cuando el {@link java.util.regex.Matcher} no puede encontrar un nombre
 * válido.
 */
public class NoEsUnNombreRealException extends Exception {

    /**
     * Crea una nueva excepción para ser lanzada cuando la verificación falle.
     *
     * @param nombre el nombre que parce inválido.
     */
    public NoEsUnNombreRealException(final String nombre) {
        super("El nombre " + nombre + " no parece ser un nombre válido.");
    }

}
