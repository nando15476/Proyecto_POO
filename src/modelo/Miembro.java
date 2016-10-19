package modelo;

import org.apache.commons.validator.routines.EmailValidator;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.regex.Pattern;

import controlador.IAdministrador;
import controlador.IMiembro;
import excepciones.CambioDenegadoException;
import excepciones.ErrorIrrecuperable;
import excepciones.NoEsUnCorreoValidoException;
import excepciones.ValorInvalidoException;

/**
 * La clase Miembro, del paquete {@link modelo} es el modelo fundamental de las clases
 * {@link Alumno}, {@link Tutor}, {@link Auxiliar} y {@link Catedratico}.
 * Esta clase define a un Miembro de la Universidad, y contiene toda la información que todos los
 * Miembros de la Universidad deben brindar para pertenecer a la institución.
 * <p>
 * Se define como "Miembro Inválido" a cualquier Miembro que no figure en la Base de Datos. Esto
 * quiere decir que un objeto Miembro y algún heredero pueden no ser representaciones de objetos
 * en la Base de Datos. Esto es así porque al generar un objeto inválido y luego agregarlo a la
 * Base de Datos, el objeto se valida automáticamente.
 * </p>
 * <p>Los objetos inválidos no tienen acceso a solicitar un {@link Token}, y por lo tanto no se
 * puede iniciar sesión con ellos. Los objetos inválidos son mutables (es decir, se pueden
 * modificar), pero media vez se valide el objeto en la Base de Datos, el objeto será inmutable.</p>
 */
public abstract class Miembro implements IMiembro {
    /**
     * Clave vacía usada para invalidar la clave anterior.
     */
    static final byte[] CLAVE_NOOP = new byte[0];
    /**
     * El número estándar de dígitos para generar el nombre de correo de la Universidad.
     */
    static final int CORREO_UVG_DIGITOS = 5;
    /**
     * Con este REGEX se verifican la validez de los nombres de las personas. Un nombre de persona
     * puede contener cualquier caracter en cualquier idioma. Sin embargo, la primera letra debe ser
     * mayúscula, no puede contener números ni caracteres especiales, no puede superar el tamaño
     * máximo de la Base de Datos y sólo puede contener un punto al final (para nombres como J.
     * Ulises).
     */
    static final Pattern NOMBRE_REGEX = Pattern.compile("(\\p{Lu}\\p{L}*\\.?)$");
    /** Encuentra todos los aciertos de caracteres UNICODE. */
    static final Pattern UNICODE_MATCHER = Pattern.compile("\\p{M}");
    /**
     * El mensaje de la excepción cuando el cambio se niega porque el Miembro está validado.
     */
    static final String YA_VALIDADO_MSG =
            "No se puede cambiar ningún valor de un Miembro validado. Primero invalide el Miembro.";
    private final EmailValidator m_emailValidator;
    private final MessageDigest m_messageDigest;
    /** Un {@code array} de 64 posiciones que representa el SHA-512 de la contraseña de usuario. */
    transient byte[] m_clave;
    /**
     * Nombre del usuario del correo de la Universidad. En este caso, el host es constante y el
     * mismo para todos los Miembros (uvg.edu.gt).
     */
    transient String m_correoUniversidad;
    /** Identificador único del Miembro en la Universidad. */
    int m_identificador = -1;
    /** El Segundo Apellido del Miembro. */
    transient String m_segundoApellido;
    /** Nombres del Miembro. En este se incluyen todos los nombres del Miembro. */
    transient String m_nombres;
    /** El Primer Apellido del Miembro. */
    transient String m_primerApellido;
    /** Identificador único del Miembro en la Base de Datos. */
    int m_sqlID = -1;
    /** Nombre de usuario del correo electrónico personal (lo que va antes de la arroba). */
    @Nullable
    transient String m_correoUsuario;
    /** Host del correo electrónico personal (por ejemplo, gmail.com, yahoo.com, etc.). */
    @Nullable
    transient String m_correoHost;
    /** El nombre de host de la Universidad (al cual pertenecen todos los correos de la U). */
    @NonNls
    String UNIVERSIDAD_HOST = "uvg.edu.gt";

    /** Genera un nuevo Miembro desde las clases herederas. */
    Miembro() {
        m_emailValidator = EmailValidator.getInstance();
        try {
            m_messageDigest = MessageDigest.getInstance("SHA-512");
        } catch (final NoSuchAlgorithmException e) {
            throw new ErrorIrrecuperable(e);
        }
    }

    @Override
    public int getSqlID() {
        return m_sqlID;
    }

    @Override
    public int getIdentificador() {
        return m_identificador;
    }

    @Override
    public void setIdentificador(final int identificador) throws CambioDenegadoException {
        if (isValido()) {
            throw new CambioDenegadoException(YA_VALIDADO_MSG);
        } else {
            if (identificador > 0) {
                m_identificador = identificador;
            } else {
                throw new CambioDenegadoException("No se permite un identificador negativo o 0.");
            }
        }
    }

    @Override
    @Nullable
    public String getNombres() {
        return m_nombres;
    }

    @Override
    @Nullable
    public String getPrimerApellido() {
        return m_primerApellido;
    }

    @Override
    @Nullable
    public String getSegundoApellido() {
        return m_segundoApellido;
    }

    @Override
    @Nullable
    public String getCorreoHost() {
        return m_correoHost;
    }

    @Override
    public void setCorreo(@NonNls @Nullable final String correoHost,
            @NonNls @Nullable final String correoUsuario)
            throws NoEsUnCorreoValidoException, CambioDenegadoException {
        if ((correoHost == null) || (correoUsuario == null) || correoHost.isEmpty() ||
                correoUsuario.isEmpty()) {
            return;
        }
        if (isValido()) {
            throw new CambioDenegadoException(YA_VALIDADO_MSG);
        } else {
            //noinspection MagicCharacter
            final String mail = correoUsuario + '@' + correoHost;
            if (m_emailValidator.isValid(mail)) {
                m_correoHost = correoHost;
                m_correoUsuario = correoUsuario;
            } else {
                throw new NoEsUnCorreoValidoException(mail);
            }
        }
    }

    @Override
    @Nullable
    public String getCorreoUsuario() {
        return m_correoUsuario;
    }

    @Override
    @Nullable
    public String getCorreoUniversidad() {
        return m_correoUniversidad;
    }

    @Override
    public void invalidar(final IAdministrador admin) {
        if (admin.isAutorizado()) {
            invalidar();
        }
    }

    /**
     * Invalida cualquier heredero de Miembro. Uso exclusivo para transacciones internas.
     */
    protected abstract void invalidar();

    /**
     * Guardar en Basse de Datos para uso interno.
     *
     * @throws SQLException            cuando la petición SQL falla
     * @throws ValorInvalidoException  cuando cualquier valor del Miembro a guardar es inválido
     * @throws CambioDenegadoException cuando la Base de Datos rechaza el cambio realizado
     */
    abstract void guardarEnBaseDeDatos()
            throws SQLException, ValorInvalidoException, CambioDenegadoException;

    /**
     * Valida cualquier heredero de Miembro. Uso exclusivo para transacciones internas.
     *
     * @param sqlID el nuevo ID del Miembro
     */
    protected abstract void validar(final int sqlID);

    /**
     * Obtiene en Basse de Datos para uso interno.
     *
     * @throws ValorInvalidoException cuando cualquier valor del Miembro recuperado es inválido Esto
     *                                implica un grave error pues la única fomrma de solucionarlo es
     *                                corregir la Base de Datos
     */
    abstract void obtenerDesdeBaseDeDatos() throws ValorInvalidoException;

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + m_identificador;
        result = (prime * result) + m_sqlID;
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Miembro other = (Miembro) obj;
        return (m_identificador == other.m_identificador) && (m_sqlID == other.m_sqlID);
    }

    @Override
    public String toString() {
        return "Miembro{" + "m_clave=" + Arrays.toString(m_clave) + ", m_correoUniversidad='" +
                m_correoUniversidad + '\'' + ", m_identificador=" + m_identificador +
                ", m_segundoApellido='" + m_segundoApellido + '\'' + ", m_nombres='" + m_nombres +
                '\'' + ", m_primerApellido='" + m_primerApellido + '\'' + ", m_sqlID=" + m_sqlID +
                ", m_correoUsuario='" + m_correoUsuario + '\'' + ", m_correoHost='" + m_correoHost +
                '\'' + '}';
    }
}
