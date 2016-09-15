package modelo;

import org.apache.commons.validator.routines.EmailValidator;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import excepciones.NoEsUnCorreoValidoException;
import excepciones.NoEsUnNombreRealException;

/**
 * La clase Miembro, del paquete {@link modelo} es el modelo fundamental de las clases
 * {@link Alumno}, {@link Tutor}, {@link Auxiliar}, {@link Administrador} y {@link Catedratico}.
 * Esta clase define a un miembro de la Universidad, y contiene toda la información que todos los
 * miembros de la Universidad deben brindar para pertenecer a la institución.
 */
@SuppressWarnings("ConstructorWithTooManyParameters")
public abstract class Miembro {
    /**
     * Con este REGEX se verifican la validez de los nombres de las personas. Un nombre de
     * persona puede contener cualquier caracter en cualquier idioma. Sin embargo, la primera y
     * sólo la primera letra debe ser mayúscula, no puede contener números ni caracteres
     * especiales, no puede superar el tamaño máximo de la base de datos y sólo puede contener un
     * punto al final (para nombres como J. Ulises).
     */
    public static final Pattern NOMBRE_REGEX = Pattern.compile("(\\p{Lu}\\p{L}*\\.?)$");
    /**
     * REGEX para validar un correo electrónico. Si bien esto no resuleve el problema de una
     * dirección de correo electrónico falso, es muy útil para detectar la mayoría de correos
     * inválidos. Esto es, porque si bien la única y auténica forma de verificarlo es enviando un
     * correo de confirmación, este programa no ofrece esa funcionalidad.
     *
     * @see <a href="http://emailregex.com/">Email Regex</a>
     */
    public static final EmailValidator EMAIL_VALIDATOR = EmailValidator.getInstance();
    private final Logger m_logger = Logger.getLogger(getClass().getCanonicalName());
    private final MessageDigest m_messageDigest = MessageDigest.getInstance("SHA-512");
    private int m_id;
    private int m_sqlID;
    private String m_nombres;
    private String m_apellido1;
    private String m_apellido2;
    private String m_correoHost;
    private String m_correoUsuario;
    private String m_correoUniversidad;
    // Token token;
    private byte[] clave;

    /**
     * Crea un nuevo objeto Miembro inválido. La instanciación directa de objetos Miembro y sus
     * herederos está prohibida, sin embargo, un constructor público o protegido es necesario
     * para la herencia y el polimorfismo. La forma correcta de recuperar y generar instancias de
     * estas clases es utilizando los métodos fábrica.
     *
     * @throws NoSuchAlgorithmException cuando el algoritmo HASH no está disponible.
     */
    protected Miembro() throws NoSuchAlgorithmException {
    }

    private Miembro(@NotNull final String nombres, @NotNull final String apellido1,
            @NotNull final String apellido2, @NotNull @NonNls final String correoHost,
            @NotNull @NonNls final String correoUsuario, final int id)
            throws NoSuchAlgorithmException, NoEsUnNombreRealException,
            NoEsUnCorreoValidoException {
        // Separa los nombres para verificarlos uno por uno
        final String[] nombres2test = nombres.split(" ");
        for (final String s : nombres2test) {
            // Si el nombre no parece ser un nombre real...
            if (!NOMBRE_REGEX.matcher(s).matches()) {
                // Lanza una excepción
                throw new NoEsUnNombreRealException(s);
            }
        }
        m_nombres = nombres;
        // Verifica el primer apellido
        if (NOMBRE_REGEX.matcher(apellido1).matches()) {
            m_apellido1 = apellido1;
        } else {
            // Lanza una excepción
            throw new NoEsUnNombreRealException(apellido1);
        }
        // Verifica el segundo apellido
        if (NOMBRE_REGEX.matcher(apellido2).matches()) {
            m_apellido2 = apellido2;
        } else {
            // Lanza una excepción
            throw new NoEsUnNombreRealException(apellido2);
        }
        // Verifica el Email
        //noinspection MagicCharacter
        final String mail = correoUsuario + '@' + correoHost;
        if (EMAIL_VALIDATOR.isValid(mail)) {
            m_correoUsuario = correoUsuario;
            m_correoHost = correoHost;
        } else {
            throw new NoEsUnCorreoValidoException(mail);
        }
    }
}
