package excepciones;

import javax.naming.OperationNotSupportedException;

/**
 * Excepción que se lanza cuando se intenta realizar un cambio que no está permitido ya sea por
 * falta de privilegios o porque el campo se ha bloqueado.
 */
public class CambioDenegadoException extends OperationNotSupportedException {
    /**
     * Se lanza cuando se intenta realizar un cambio que no está permitido ya sea por
     * falta de privilegios o porque el campo se ha bloqueado.
     *
     * @param message mensaje para complementar la excepción.
     */
    public CambioDenegadoException(final String message) {
        super("Cambio denegado: " + message);
    }
}
