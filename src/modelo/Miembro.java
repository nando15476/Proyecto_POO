package modelo;

import org.apache.commons.validator.routines.EmailValidator;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

import excepciones.CambioDenegadoException;
import excepciones.ErrorIrrecuperable;
import excepciones.NoEsUnCorreoValidoException;
import excepciones.NoEsUnNombreRealException;

/**
 * La clase Miembro, del paquete {@link modelo} es el modelo fundamental de las clases
 * {@link Alumno}, {@link Tutor}, {@link Auxiliar} y {@link Catedratico}.
 * Esta clase define a un miembro de la Universidad, y contiene toda la información que todos los
 * miembros de la Universidad deben brindar para pertenecer a la institución.
 * <p>
 * Se define como "Miembro Inválido" a cualquier Miembro que no figure en la base de datos. Esto
 * quiere decir que un objeto Miembro y algún heredero pueden no ser representaciones de objetos
 * en la Base de Datos. Esto es así porque al generar un objeto inválido y luego agregarlo a la
 * Base de Datos, el objeto se valida automáticamente.
 * </p>
 * <p>Los objetos inválidos no tienen acceso a solicitar un {@link Token}, y por lo tanto no se
 * puede iniciar sesión con ellos. Los objetos inválidos son mutables (es decir, se pueden
 * modificar), pero media vez se valide el objeto en la base de datos, el objeto será inmutable.</p>
 */
@SuppressWarnings("ConstructorWithTooManyParameters")
public abstract class Miembro {
    protected static final String YA_VALIDADO_MSG =
            "No se puede cambiar ningun capmo de un objeto validado.";
    /**
     * El nombre de host de la Universidad (al cual pertenecen todos los correos de la U).
     */
    @NonNls
    static final String UNIVERSIDAD_HOST = "uvg.edu.gt";
    /**
     * Con este REGEX se verifican la validez de los nombres de las personas. Un nombre de persona
     * puede contener cualquier caracter en cualquier idioma. Sin embargo, la primera letra debe ser
     * mayúscula, no puede contener números ni caracteres especiales, no puede superar el tamaño
     * máximo de la base de datos y sólo puede contener un punto al final (para nombres como J.
     * Ulises).
     */
    static final Pattern NOMBRE_REGEX = Pattern.compile("(\\p{Lu}\\p{L}*\\.?)$");
    /**
     * Encuentra todos los aciertos de caracteres UNICODE.
     */
    static final Pattern UNICODE_MATCHER = Pattern.compile("\\p{M}");
    private final EmailValidator m_emailValidator;
    private final MessageDigest m_messageDigest;
    /**
     * Un {@code array} de 64 posiciones que representa el SHA-512 de la contraseña de usuario.
     */
    // Token token;
    protected byte[] clave;
    /**
     * Nombre del usuario del correo de la Universidad. En este caso, el host es constante y el
     * mismo para todos los miembros (uvg.edu.gt).
     */
    String m_correoUniversidad;
    /**
     * Identificador único del Miembro en la Universidad.
     */
    int m_id = -1;
    /**
     * El Segundo Apellido del Miembro.
     */
    String m_segundoApellido;
    /**
     * Nombres del Miembro. En este se incluyen todos los nombres del Miembro.
     */
    String m_nombres;
    /**
     * El Primer Apellido del Miembro.
     */
    String m_primerApellido;
    /**
     * Identificador único del Miembro en la Base de Datos.
     */
    int m_sqlID = -1;
    /**
     * Nombre de usuario del correo electrónico personal (lo que va antes de la arroba).
     */
    @Nullable
    private String m_correoUsuario;
    /**
     * Host del correo electrónico personal (por ejemplo, gmail.com, yahoo.com, etc.).
     */
    @Nullable
    private String m_correoHost;

    /**
     * Genera un nuevo Miembro desde las clases herederas.
     */
    protected Miembro() {
        m_emailValidator = EmailValidator.getInstance();
        try {
            m_messageDigest = MessageDigest.getInstance("SHA-512");
        } catch (final NoSuchAlgorithmException e) {
            throw new ErrorIrrecuperable(e);
        }
    }

    /**
     * Devuelve el índice del Miembro en su respectiba tabla en la Base de Datos. Este índice
     * determina la validez del Miembro.
     *
     * @return el índice en la Base de Datos del Miembro o -1 si el miembro no está en la Base de
     * Datos.
     */
    public int getSqlID() {
        return m_sqlID;
    }

    /**
     * @return
     */
    public String getCorreoUniversidad() {
        return m_correoUniversidad;
    }

    /**
     * Agrega el correo de la Universidad. En este caso, solo se usa el <b>nombre de usuario</b>,
     * pues el dominio de la universidad es constante y el mismo para todos los Miembros.
     *
     * @param usuarioU nombre de usuario de la Universidad
     *
     * @throws NoEsUnCorreoValidoException si el correo de la Universidad no pasa la prueba de
     *                                     validez.
     * @throws CambioDenegadoException     si el Miembro ya estaba validado
     */
    abstract void setCorreoUniversidad(@NonNls @NotNull String usuarioU)
            throws NoEsUnCorreoValidoException, CambioDenegadoException;

    /**
     * Devuelve el ID del Miembro, si es -1 es que no se ha configurado un ID para el Miembro.
     *
     * @return el ID del Miembro
     */
    public int getId() {
        return m_id;
    }

    /**
     * Cambia el ID del Miembro. Genera una excepción si el ID es 0, negativo o si el Miembro ya
     * está validad.
     *
     * @param id el nuevo ID del objeto
     *
     * @throws CambioDenegadoException si el valor del ID es inválido o si el Miembro ya está
     *                                 validado.
     */
    public void setId(final int id) throws CambioDenegadoException {
        if ((m_sqlID == -1) && (id > 0)) {
            m_id = id;
        } else {
            throw new CambioDenegadoException("No se puede cambiar el ID a " + id +
                    ". O bien el ID es inválido o el Miembro ya está validado.");
        }
    }

    /**
     * Devuelve los nombres del Miembro.
     *
     * @return los nombres del Miembro
     */
    @Nullable
    public String getNombres() {
        return m_nombres;
    }

    /**
     * Valida un nuevo nombre para el Miembro y luego asigna el nombre al Miembro.
     *
     * @param nombres los nuevos nombres del Miembro
     *
     * @throws NoEsUnNombreRealException si el nombre parece no ser un nombre válido
     * @throws CambioDenegadoException   cuando no se puede realizar el cambio de nombres
     */
    public abstract void setNombres(@NonNls @NotNull final String nombres)
            throws NoEsUnNombreRealException, CambioDenegadoException;

    /**
     * Devuelve el primer apellido del Miembro.
     *
     * @return el primer apellido del Miembro
     */
    @Nullable
    public String getPrimerApellido() {
        return m_primerApellido;
    }

    /**
     * Valida el nuevo primer apellido del Miembro y luego asigna el primer apellido al Miembro.
     *
     * @param primerApellido el nuevo primer apellido del Miembro
     *
     * @throws NoEsUnNombreRealException si el primer apellido parece no ser un apellido válido
     * @throws CambioDenegadoException   si el Miembro ya estaba validado
     */
    public abstract void setPrimerApellido(@NonNls @NotNull final String primerApellido)
            throws NoEsUnNombreRealException, CambioDenegadoException;

    /**
     * Devuelve el segundo apellido del Miembro.
     *
     * @return el segundo apellido del Miembro
     */
    @Nullable
    public String getSegundoApellido() {
        return m_segundoApellido;
    }

    /**
     * Valida el nuevo segundo apellido del Miembro y luego asigna el segundo apellido al Miembro.
     *
     * @param segundoApellido el primer apellido del Miembro
     *
     * @throws NoEsUnNombreRealException si el segundo apellido parece no ser un apellido válido
     * @throws CambioDenegadoException   si el Miembro ya estaba validado
     */
    public abstract void setSegundoApellido(@NonNls @NotNull final String segundoApellido)
            throws NoEsUnNombreRealException, CambioDenegadoException;

    /**
     * Devuelve el host de correo electrónico personal del Miembro.
     *
     * @return el host de correo electrónico
     */
    @Nullable
    public String getCorreoHost() {
        return m_correoHost;
    }

    /**
     * Valida el correo electrónico personal del Miembro y luego asigna el Host y el Nombre de
     * Usuario al Miembro. Tanto el Nombre de Usuario como el Host son {@link Nullable}, es
     * decir, pueden ser {@code null}. Esto hará que el Miembro no tenga correo electrónico
     * personal.
     *
     * @param correoHost    host del correo electrónico (como gmail.com o yahoo.com)
     * @param correoUsuario nombre de usuario (la parte que va antes de la arroba)
     *
     * @throws NoEsUnCorreoValidoException cuando no se puede determinar la validez del correo.
     */
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

    /**
     * Devuelve el nombre de usuario de correo electrónico personal del Miembro.
     *
     * @return devuelve el segundo apellido del Miembro
     */
    @Nullable
    public String getCorreoUsuario() {
        return m_correoUsuario;
    }

    /**
     * Genera un nombre de usuario para el correo electrónico de la Universidad.
     *
     * @throws CambioDenegadoException     si el Miembro ya estaba validado
     * @throws NoEsUnCorreoValidoException
     */
    public abstract void makeCorreoU() throws CambioDenegadoException, NoEsUnCorreoValidoException;
}
