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
 * {@link Alumno}, {@link Tutor}, {@link Auxiliar}, {@link Administrador} y {@link Catedratico}.
 * Esta clase define a un miembro de la Universidad, y contiene toda la información que todos los
 * miembros de la Universidad deben brindar para pertenecer a la institución.
 * <p>
 * Se define como "Miembro Inválido" a cualquier Miembro que no figure en la base de datos. Esto
 * quiere decir que un objeto Miembro y algún heredero pueden no ser representaciones de objetos
 * en la Base de Datos. Esto es así porque al generar un objeto inválido y luego agregarlo a la
 * Base de Datos, el objeto se valida automáticamente.
 * </p>
 * <p>Los objetos inválidos no tienen acceso a solicitar un {@link Token}, y por lo tanto no se
 * puede iniciar sesión con ellos.</p>
 */
@SuppressWarnings("ConstructorWithTooManyParameters")
public abstract class Miembro {
    /**
     * Con este REGEX se verifican la validez de los nombres de las personas. Un nombre de persona
     * puede contener cualquier caracter en cualquier idioma. Sin embargo, la primera letra debe ser
     * mayúscula, no puede contener números ni caracteres especiales, no puede superar el tamaño
     * máximo de la base de datos y sólo puede contener un punto al final (para nombres como J.
     * Ulises).
     */
    public static final Pattern NOMBRE_REGEX = Pattern.compile("(\\p{Lu}\\p{L}*\\.?)$");
    protected static final Pattern UNICODE_MATCHER = Pattern.compile("\\p{M}");
    /**
     * Instancia de {@link EmailValidator} para validar correos electrónicos.
     */
    private final EmailValidator m_emailValidator;
    private final MessageDigest m_messageDigest;
    protected String m_nombres;
    protected String m_primerApellido;
    protected String m_segundoApellido;
    protected int m_id = -1;
    private int m_sqlID = -1;
    private String m_correoHost;
    private String m_correoUsuario;
    private String m_correoUniversidad;
    // Token token;
    private byte[] clave;


    /**
     * Genera un nuevo Miembro desde las clases herederas.
     */
    protected Miembro() {
        m_emailValidator = EmailValidator.getInstance();
        try {
            m_messageDigest = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new ErrorIrrecuperable(e);
        }
    }

    /**
     * Devuelve el ID del Miembro, si el miembro es inválido devuelve {@code -1}, aún si un ID se
     * ha configurado previamente.
     *
     * @return el ID del Miembro
     */
    public int getId() {
        return (m_sqlID < 0) ? m_id : -1;
    }

    /**
     * Configura el ID de un Miembro si no estaba configurado antes, si ya lo estaba, se lanza
     * una excepción {@link CambioDenegadoException}, que significa que el cambio no puede
     * realizarse.
     *
     * @param id el nuevo ID del objeto
     *
     * @throws CambioDenegadoException si el valor del ID es inválido o si un ID ya ha sido
     *                                 configurado.
     */
    public void setId(final int id) throws CambioDenegadoException {
        if ((m_id == -1) && (id >= 0)) {
            m_id = id;
        } else {
            throw new CambioDenegadoException("No se puede cambiar el ID a " + id +
                    ", o el nuevo valor es inválido o el ID ya se ha configurado anteriormente.");
        }
    }

    /**
     * Devuelve los nombres del Miembro si el Miembro está validado o {@code null} si el miembro
     * no está validado, aún si sus nombres se han configurado previamente.
     *
     * @return los nombres del Miembro
     */
    @Nullable
    public String getNombres() {
        return (m_sqlID < 0) ? m_nombres : null;
    }

    /**
     * Valida los nombres de los Miembros y luego asigna el nombre al Miembro.
     *
     * @param nombres los nombres del Miembro
     *
     * @throws NoEsUnNombreRealException si el nombre parece no ser un nombre válido
     */
    public abstract void setNombres(@NonNls @NotNull final String nombres)
            throws NoEsUnNombreRealException, CambioDenegadoException;

    /**
     * Devuelve el primer apellido del Miembro si el Miembro está validado o {@code null} si el
     * miembro no está validado, aún si sus apellidos se han configurado previamente.
     *
     * @return devuelve el primer apellido del Miembro
     */
    @Nullable
    public String getPrimerApellido() {
        return (m_sqlID < 0) ? m_primerApellido : null;
    }

    /**
     * Valida el primer apellido de los Miembros y luego asigna el primer apellido al Miembro.
     *
     * @param primerApellido el primer apellido del Miembro
     *
     * @throws NoEsUnNombreRealException si el primer apellido parece no ser un apellido válido
     */
    public abstract void setPrimerApellido(@NonNls @NotNull final String primerApellido)
            throws NoEsUnNombreRealException, CambioDenegadoException;

    /**
     * Devuelve el segundo apellido del Miembro si el Miembro está validado o {@code null} si el
     * miembro no está validado, aún si sus apellidos se han configurado previamente.
     *
     * @return devuelve el segundo apellido del Miembro
     */
    @Nullable
    public String getSegundoApellido() {
        return (m_sqlID < 0) ? m_segundoApellido : null;
    }

    /**
     * Valida el primer apellido de los Miembros y luego asigna el primer apellido al Miembro.
     *
     * @param segundoApellido el primer apellido del Miembro
     *
     * @throws NoEsUnNombreRealException si el primer apellido parece no ser un apellido válido
     */
    public abstract void setSegundoApellido(@NonNls @NotNull final String segundoApellido)
            throws NoEsUnNombreRealException, CambioDenegadoException;

    /**
     * Devuelve el host de correo electrónico del Miembro si el Miembro está validado o {@code
     * null} si el miembro no está validado, aún si su correo electrónico se ha configurado
     * previamente.
     *
     * @return devuelve el segundo apellido del Miembro
     */
    @Nullable
    public String getCorreoHost() {
        return (m_sqlID < 0) ? m_correoHost : null;
    }

    /**
     * Valida el primer apellido de los Miembros y luego asigna el primer apellido al Miembro.
     *
     * @param correoHost    host del correo electrónico (como gmail.com o yahoo.com)
     * @param correoUsuario nombre de usuario (la parte que va antes de la arroba)
     *
     * @throws NoEsUnCorreoValidoException cuando no se puede determinar la validez del correo.
     */
    public void setCorreo(@NonNls final String correoHost, @NonNls final String correoUsuario)
            throws NoEsUnCorreoValidoException {
        //noinspection MagicCharacter
        final String mail = correoUsuario + '@' + correoHost;
        if (m_emailValidator.isValid(mail)) {
            m_correoHost = correoHost;
            m_correoUsuario = correoUsuario;
        } else {
            throw new NoEsUnCorreoValidoException(mail);
        }
    }

    /**
     * Devuelve el nombre de usuario de correo electrónico del Miembro si el Miembro está
     * validado o {@code null} si el miembro no está validado, aún si su correo electrónico se ha
     * configurado previamente.
     *
     * @return devuelve el segundo apellido del Miembro
     */
    @Nullable
    public String getCorreoUsuario() {
        return (m_sqlID < 0) ? m_correoUsuario : null;
    }

    public abstract void makeCorreoU() throws CambioDenegadoException;

    abstract void setCorreoU(@NonNls @NotNull String correo);
}
