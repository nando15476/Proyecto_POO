package excepciones;

/**
 * Excepción que se lanza cuando el {@link org.apache.commons.validator.routines.EmailValidator}
 * determina que el correo electrónico provisto no es válido.
 */
public class NoEsUnCorreoValidoException extends Exception {
    /**
     * Crea una nueva excepción para ser lanzada cuando la verificación falle.
     *
     * @param mail el e-mail que parce inválido.
     */
    public NoEsUnCorreoValidoException(final String mail) {
        super("El correo electrónico " + mail + " no parece ser un correo electrónico válido.");
    }
}
