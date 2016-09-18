package excepciones;

/**
 * Un error que se lanza cuando un error irrecuperable ocurre y el programa debe terminar.
 */
public class ErrorIrrecuperable extends Error {
    /**
     * Se lanza error que no se debe capturar (catch) e indica que el programa no puede continuar
     * con su correcta ejecución y debe terminar.
     *
     * @param message mensaje de error.
     */
    public ErrorIrrecuperable(final String message) {
        super(message);
    }

    /**
     * Se lanza error que no se debe capturar (catch) e indica que el programa no puede continuar
     * con su correcta ejecución y debe terminar.
     *
     * @param e excepción que causó el error.
     */
    public ErrorIrrecuperable(final Exception e) {
        super(e);
    }
}
