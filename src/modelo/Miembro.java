package modelo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

/**
 * La clase Miembro, del paquete {@link modelo} es el modelo fundamental de las clases
 * {@link Alumno}, {@link Tutor}, {@link Auxiliar}, {@link Administrador} y {@link Catedratico}.
 * Esta clase define a un miembro de la Universidad, y contiene toda la información que todos los
 * miembros de la Universidad deben brindar para pertenecer a la institución.
 */
public abstract class Miembro {
    public static final int SHA_LENGTH = 256;
    private final Logger m_logger = Logger.getLogger(getClass().getCanonicalName());
    //private final byte[] m_contraseniaHash;
    //private final int m_sqlID;
    private final int m_id;
    private final String m_nombres;
    private final String m_apellidos;
    private MessageDigest m_messageDigest = MessageDigest.getInstance("SHA-512");
    private String m_correoPersonal;
    private String m_correoUniversidad;
    // Token token;

    protected Miembro(final String nombres, final String apellidos, int id)
            throws NoSuchAlgorithmException {
        m_id=id;
        m_nombres=nombres;
        m_apellidos=apellidos;
    }
}
