package excepciones;

import controlador.IAdministrador;
import controlador.IMiembro;
import modelo.Administrador;
import modelo.Miembro;

/**
 * Se lanza cuando se deniega un cambio por falta de autoridad o por falta de autenticidad. A
 * diferencia de {@link CambioDenegadoException}, donde se lanza porque el cambio en sí fue
 * denegado, este se lanza cuando el responsable falla en autenticarse.
 */
public class PermisoDenegadoException extends Exception {
    /**
     * Se lanza cuando un {@link Administrador} no está autenticado e intenta realizar un cambio u
     * operación que no puede hacer sin autorización.
     *
     * @param admin el {@link Administrador} responsable que falló la prueba de autenticidad
     */
    public PermisoDenegadoException(final IAdministrador admin) {
        super("Permiso denegado: El Administrador {" + admin +
                "} no ha sido autorizado para tareas administrativas.");
    }

    /**
     * Se lanza cuando un {@link Miembro} no está autenticado e intenta realizar una acción que
     * requiere autenticación.
     *
     * @param miembro el {@link Miembro} responsable que falló la prueba de autenticidad
     */
    public PermisoDenegadoException(final IMiembro miembro) {
        super("Permiso denegado: El Miembro {" + miembro +
                "} no cuenta con suficientes privilegios.");
    }
}
