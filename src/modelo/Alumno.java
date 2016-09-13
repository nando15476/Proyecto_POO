package modelo;

import java.security.NoSuchAlgorithmException;

/**
 * Created by notengobattery on 12/09/16.
 */
public class Alumno extends Miembro {

    protected Alumno(final String nombres, final String apellidos, final int id)
            throws NoSuchAlgorithmException {
        super(nombres, apellidos, id);
    }
}
