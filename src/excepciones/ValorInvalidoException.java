package excepciones;

/**
 * Se debe lanzar cuando ocurre una excepción debida a un valor de parámetro inválido.
 */
public class ValorInvalidoException extends Exception {
    /**
     * Crea una nueva excepción que se lanza cuando ocurre una excepción debida a un valor de
     * parámetro inválido.
     *
     * @param s el mensaje de error que explica la fuente y la causa del valor inválido
     * @param e la excepción que confirma la invalidez del valor
     */
    public ValorInvalidoException(final String s, final Exception e) {
        super(s, e);
    }
}
