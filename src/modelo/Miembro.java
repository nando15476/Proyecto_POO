package modelo;

import org.apache.commons.validator.routines.EmailValidator;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;
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
    public static final int CORREO_UVG_DIGITOS = 5;
    /**
     * Clave vacía usada para invalidar la clave anterior.
     */
    protected static final byte[] CLAVE_NOOP = new byte[0];
    @SuppressWarnings("JavaDoc")
    static final String YA_VALIDADO_MSG = "No se puede cambiar ningun campo de un objeto validado.";
    /** 'SELECT FROM ' */
    @NonNls
    static final String SELECT_FROM = "SELECT * FROM ";
    /** El nombre de host de la Universidad (al cual pertenecen todos los correos de la U). */
    @NonNls
    static final String UNIVERSIDAD_HOST = "uvg.edu.gt";
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
    private transient String m_correoUsuario;
    /** Host del correo electrónico personal (por ejemplo, gmail.com, yahoo.com, etc.). */
    @Nullable
    private transient String m_correoHost;

    /** Genera un nuevo Miembro desde las clases herederas. */
    protected Miembro() {
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
        if ((m_sqlID == -1) && (identificador > 0)) {
            m_identificador = identificador;
        } else {
            throw new CambioDenegadoException("No se puede cambiar el ID a " + identificador +
                    ". O bien el ID es inválido o el Miembro ya está validado.");
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
        if (m_sqlID == -1) {
            //noinspection MagicCharacter
            final String mail = correoUsuario + '@' + correoHost;
            if (m_emailValidator.isValid(mail)) {
                m_correoHost = correoHost;
                m_correoUsuario = correoUsuario;
            } else {
                throw new NoEsUnCorreoValidoException(mail);
            }
        } else {
            throw new CambioDenegadoException(YA_VALIDADO_MSG);
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
        //noinspection MagicCharacter
        return m_correoUniversidad;
    }

    /**
     * Recuepera el objeto acutal desde la Base de Datos, y una vez recuperado de la base de datos,
     * el objeto se valida. La información previamente configurada en la instancia podría o no
     * cambiar para coincidir con la Base de Datos de acuerdo a las siguientes condiciones:
     * <p>
     * <ul> <li>Si el ID de SQL es distinto de {@code -1}, es decir el objeto <b>está</b> validado,
     * entonces toda la información se actualiza desde la Base de Datos, incluyendo el identificador
     * de la Universidad.</li> <li>Si el ID de SQL es {@code -1}, es decir el objeto <b>no está</b>
     * validado, entonces toda la información se actualiza desde la Base de Datos, incluyendo el
     * ID de SQL.</li> </ul>
     *
     * @throws SQLException           si las peticiones SQL fallan
     * @throws ValorInvalidoException si algún valor de la Base de Datos es inválido
     */
    protected abstract void obtenerDesdeBaseDeDatos() throws SQLException, ValorInvalidoException;

    public void invalidar(IAdministrador admin) {
        if (admin.isAutorizado()) {
            invalidar();
        }
    }

    protected abstract void invalidar();

    @Override
    public int hashCode() {
        // Las dos claves principales deberían ser suficientes
        int hash = 1;
        hash += m_sqlID;
        hash += m_identificador;
        // Pero no lo son
        hash += ((m_correoUsuario != null) ? m_correoUsuario.hashCode() : 0) +
                ((m_correoHost != null) ? m_correoHost.hashCode() : 0);
        // No hace falta verificar CorreoU porque viene de  Nombre e Identificador
        hash += ((m_nombres != null) ? m_nombres.hashCode() : 0);
        // Muestra el ID en la "cola" del hash
        hash -= hash % 100000;
        hash += (hash < 0) ? -m_identificador : m_identificador;
        return hash;
    }

    @SuppressWarnings("OverlyComplexBooleanExpression")
    @Override
    public boolean equals(final Object o) {
        final Miembro aComparar;
        // Vamos a sobreescribir Equals para fines de comparación en Alumno
        if (getClass().isAssignableFrom(o.getClass())) {
            aComparar = ((Miembro) o);
        } else {
            return false;
        }
        return (aComparar.m_sqlID == m_sqlID) && (aComparar.m_identificador == m_identificador) &&
                Objects.equals(aComparar.m_nombres, m_nombres) &&
                Objects.equals(aComparar.m_primerApellido, m_primerApellido) &&
                Objects.equals(aComparar.m_segundoApellido, m_segundoApellido) &&
                Objects.equals(aComparar.m_correoUsuario, m_correoUsuario) &&
                Objects.equals(aComparar.m_correoHost, m_correoHost) &&
                Arrays.equals(aComparar.m_clave, m_clave) &&
                Objects.equals(aComparar.m_correoUniversidad, m_correoUniversidad);
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
