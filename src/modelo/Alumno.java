package modelo;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import controlador.IAdministrador;
import controlador.IMiembro;
import controlador.MariaDBPool;
import excepciones.CambioDenegadoException;
import excepciones.ErrorIrrecuperable;
import excepciones.NoEsUnCorreoValidoException;
import excepciones.NoEsUnNombreRealException;
import excepciones.PermisoDenegadoException;
import excepciones.ValorInvalidoException;

/**
 * Modela a un Alumno, hereda de {@link Miembro} y sirve de contenedor para la información
 * proveniente de la Base de Datos. Para más información, consúltese el Javadoc de {@link Miembro}.
 */
public class Alumno extends Miembro {
    /**
     * Capacidad inicial del Multiton.
     */
    private static final int INITIAL_CAPACITY = 25;
    /**
     * Nombre de la columna del ID único de SQL en la Base de Datos.
     */
    private static final String ALUMNOS_SQLID = "SQLID";
    /**
     * Nombre de la columna del nombre de usuario de correo personal en la Base de Datos.
     */
    private static final String CORREO_USR = "CORREO_USR";
    /**
     * Nombre de la tabla donde se almacenan los Alumnos.
     */
    @NonNls
    private static final String ALUMNOS = "Alumnos";
    /**
     * Nombre de la columna donde se almacenan los nombres de los Alumnos.
     */
    @NonNls
    private static final String ALUMNOS_NOMBRES = "NOMBRES";
    /**
     * Nombre de la columna donde se almacena el primer apellido de los Alumnos.
     */
    @NonNls
    private static final String ALUMNOS_APELLIDO1 = "APELLIDO1";
    /**
     * Nombre de la columna donde se almacena el segundo apellido de los Alumnos.
     */
    @NonNls
    private static final String ALUMNOS_APELLIDO2 = "APELLIDO2";
    /**
     * <p>Longitud máxima permitida para un nombre de una persona miembro de la Universidad.</p>
     * <b>¡ADVERTENCIA!</b>: Este valor coincide con el valor establecido en la Base de Datos.
     */
    private static final int NOMBRE_LEN;
    /**
     * <p>Longitud máxima permitida para un primer apellido de una persona miembro de la
     * Universidad.</p>
     * <b>¡ADVERTENCIA!</b>: Este valor coincide con el valor establecido en la Base de Datos.
     */
    private static final int APELLIDO1_LEN;
    /**
     * <p>Longitud máxima permitida para un segundo apellido de una persona miembro de la
     * Universidad.</p>
     * <b>¡ADVERTENCIA!</b>: Este valor coincide con el valor establecido en la Base de Datos.
     */
    private static final int APELLIDO2_LEN;
    /**
     * Nombre de la columna del ID en la Base de Datos.
     */
    private static final String ALUMNOS_ID = "ID";
    /**
     * Nombre de la columna del host de correo personal en la Base de Datos.
     */
    private static final String CORREO_HOST = "CORREO_HST";
    /**
     * Nombre de la columna del usuario de correo de la UVG en la Base de Datos.
     */
    private static final String CORREO_UVG = "CORREO_UVG";
    /**
     * Nombre de la columna de la contraseña/clave en la Base de Datos.
     */
    private static final String CLAVE = "CLAVE";
    // Mapa del Multitón
    private static final transient ConcurrentHashMap<Integer, Alumno> MAPA_VALIDOS_MULTITON =
            new ConcurrentHashMap<>(INITIAL_CAPACITY);
    // Logger :v
    private static final Logger LOGGER = Logger.getLogger(Alumno.class.getCanonicalName());
    @SuppressWarnings("HardcodedFileSeparator")
    private static final Pattern CORREO_ALUMNO_REGEX = Pattern.compile(
            "\\w{3,4}\\d{" + CORREO_UVG_DIGITOS + ',' + CORREO_UVG_DIGITOS + 1 + "}$");

    static {
        final int nombreLenTmp;
        final int apellido1LenTmp;
        final int apellido2LenTmp;
        try (Connection conexion = MariaDBPool.getConexion()) {
            // Uso de PreparedStatement
            try (Statement statement = conexion.createStatement();
                 ResultSet result = statement.executeQuery("SELECT * FROM " + ALUMNOS)) {
                final ResultSetMetaData meta = result.getMetaData();
                nombreLenTmp = meta.getColumnDisplaySize(result.findColumn(ALUMNOS_NOMBRES));
                apellido1LenTmp = meta.getColumnDisplaySize(result.findColumn(ALUMNOS_APELLIDO1));
                apellido2LenTmp = meta.getColumnDisplaySize(result.findColumn(ALUMNOS_APELLIDO2));

            } catch (final SQLException e) {
                throw new ErrorIrrecuperable(e);
            }
        } catch (final SQLException e) {
            throw new ErrorIrrecuperable(e);
        }
        NOMBRE_LEN = nombreLenTmp;
        APELLIDO1_LEN = apellido1LenTmp;
        APELLIDO2_LEN = apellido2LenTmp;
        LOGGER.log(Level.INFO, "El modelo Alumno fue inicializado correctamente.");
    }

    private int m_referencias = 1;

    /**
     * Crea un nuevo Alumno con un ID.
     *
     * @param id ID para el alumno
     */
    private Alumno(final int id) {
        m_identificador = id;
        try {
            obtenerDesdeBaseDeDatos();
        } catch (ValorInvalidoException e) {
            LOGGER.log(Level.INFO,
                    "El Alumno con ID " + id + " no parece estar en la Base de Datos", e);
        }
    }

    @Override
    protected void invalidar() {
        if (m_sqlID != -1) {
            m_sqlID = -1;
            MAPA_VALIDOS_MULTITON.remove(m_identificador);
            m_clave = CLAVE_NOOP;
            // TODO Remover el Token
        }
    }

    @Override
    public void setNombres(@NonNls @NotNull final String nombres)
            throws NoEsUnNombreRealException, CambioDenegadoException {
        if (isValido()) {
            throw new CambioDenegadoException(YA_VALIDADO_MSG);
        } else {
            if (nombres.length() > NOMBRE_LEN) {
                throw new CambioDenegadoException("El nombre es demasiado largo.");
            }
            final String[] testList = nombres.split(" ");
            for (final String s : testList) {
                // Si el nombre no parece ser un nombre real...
                if (!NOMBRE_REGEX.matcher(s).matches()) {
                    throw new NoEsUnNombreRealException(s);
                }
            }
            m_nombres = nombres;
        }
    }

    @Override
    public void setPrimerApellido(@NonNls @NotNull final String primerApellido)
            throws NoEsUnNombreRealException, CambioDenegadoException {
        if (isValido()) {
            throw new CambioDenegadoException(YA_VALIDADO_MSG);
        } else {
            if (primerApellido.length() > APELLIDO1_LEN) {
                throw new CambioDenegadoException("El primer apellido es demasiado largo.");
            }
            final String[] testList = primerApellido.split(" ");
            for (final String s : testList) {
                // Si el nombre no parece ser un nombre real...
                if (!NOMBRE_REGEX.matcher(s).matches()) {
                    throw new NoEsUnNombreRealException(s);
                }
            }
            m_primerApellido = primerApellido;
        }
    }

    @Override
    public void setSegundoApellido(@NonNls @NotNull final String segundoApellido)
            throws NoEsUnNombreRealException, CambioDenegadoException {
        if (isValido()) {
            throw new CambioDenegadoException(YA_VALIDADO_MSG);
        } else {
            if (segundoApellido.length() > APELLIDO2_LEN) {
                throw new CambioDenegadoException("El segundo apellido es demasiado largo.");
            }
            final String[] testList = segundoApellido.split(" ");
            for (final String s : testList) {
                // Si el nombre no parece ser un nombre real...
                if (!NOMBRE_REGEX.matcher(s).matches()) {
                    // Lanza una excepción
                    throw new NoEsUnNombreRealException(s);
                }
            }
            m_segundoApellido = segundoApellido;
        }
    }

    @Override
    public void setCorreoUniversidad(@NonNls @NotNull final String correoUniversidad)
            throws NoEsUnCorreoValidoException, CambioDenegadoException {
        if (isValido()) {
            throw new CambioDenegadoException(YA_VALIDADO_MSG);
        } else {
            if (CORREO_ALUMNO_REGEX.matcher(correoUniversidad).matches()) {
                m_correoUniversidad = correoUniversidad;
            } else {
                LOGGER.log(Level.SEVERE,
                        "El nuevo nombre de usuario de correo de la Universidad debe coincidir" +
                                " con el REGEX " + CORREO_ALUMNO_REGEX + '.');
                throw new NoEsUnCorreoValidoException(correoUniversidad);
            }
        }
    }

    @SuppressWarnings("BreakStatement")
    @Override
    public void makeCorreoU() throws CambioDenegadoException, NoEsUnCorreoValidoException {
        if ((m_primerApellido == null) || (m_identificador <= 0)) {
            throw new CambioDenegadoException("No se generará un correo si el primer apellido " +
                    "está vacío o si el ID es inválido.");
        }
        if (isValido()) {
            throw new CambioDenegadoException(YA_VALIDADO_MSG);
        } else {
            // En español, nuestros apellidos tienen símbolos raros no permitidos en direcciones
            // de correo.
            String primerA =
                    Normalizer.normalize(m_primerApellido, Form.NFD).toLowerCase(Locale.ENGLISH);
            primerA = UNICODE_MATCHER.matcher(primerA).replaceAll("");
            // Separamos los apellidos como De Leon o De La Cruz para jugar con ellos uno por uno
            final String[] split = primerA.split(" ");
            final StringBuilder ucorreo = new StringBuilder(7);
        /*
         * En los correos de la U se usan 3 letras si la primera palabra del apellido tiene
         * más o 3 caracteres. Si tiene menos de 3, entonces se utiliza la siguiente palabra
         * para ajustar un total de 4 letras.
         */
            for (final String s : split) {
                if ((s.length() >= 3) && ((ucorreo.length() + 3) <= 4)) {
                    ucorreo.append(s.substring(0, 3));
                    break;
                }
                ucorreo.append((s.length() >= (4 - ucorreo.length())) ?
                        s.substring(0, 4 - ucorreo.length()) : s);
                if (ucorreo.length() >= 3) {
                    break;
                }
            }
            ucorreo.append(
                    String.format(String.format("%%0%dd", CORREO_UVG_DIGITOS), m_identificador));
            setCorreoUniversidad(ucorreo.toString());
        }
    }

    @Override
    public void guardarEnBaseDeDatos(final IAdministrador admin)
            throws SQLException, PermisoDenegadoException, ValorInvalidoException,
            CambioDenegadoException {
        if (admin.isAutorizado()) {
            guardarEnBaseDeDatos();
        } else {
            throw new PermisoDenegadoException(admin);
        }
    }

    @Override
    public boolean isValido() {
        return m_sqlID >= 1;
    }

    @Override
    public void obtenerDesdeBaseDeDatos(final IAdministrador administrador)
            throws SQLException, ValorInvalidoException, PermisoDenegadoException {
        if (administrador.isAutorizado()) {
            obtenerDesdeBaseDeDatos();
        } else {
            throw new PermisoDenegadoException(administrador);
        }
    }

    @SuppressWarnings({"OverlyLongMethod", "MagicNumber"})
    @Override
    void guardarEnBaseDeDatos()
            throws SQLException, ValorInvalidoException, CambioDenegadoException {
        // Vamos a crear un objeto Alumno de tara para determinar los cambios
        try (Miembro tara = new Alumno(m_identificador)) {
            // Si no se pudo recuperar con el ID, quizás se pueda recuperar con el SQLID
            if (!tara.isValido()) {
                tara.m_sqlID = m_sqlID;
                tara.obtenerDesdeBaseDeDatos();
            }
            try {
                // Verificar toda la información
                setIdentificador(m_identificador);
                setNombres(m_nombres);
                setPrimerApellido(m_primerApellido);
                setSegundoApellido(m_segundoApellido);
                setCorreo(getCorreoHost(), getCorreoUsuario());
                setCorreoUniversidad(m_correoUniversidad);
            } catch (final Exception e) {
                throw new ValorInvalidoException(
                        "No se puede guardar en la Base de Datos si los valores ingresados son " +
                                "inválidos.", e);
            }
            final String prepStatement =
                    "INSERT INTO `" + ALUMNOS + "`(`" + ALUMNOS_ID + "`," + '`' + ALUMNOS_NOMBRES +
                            "`,`" + ALUMNOS_APELLIDO1 + "`," + '`' + ALUMNOS_APELLIDO2 + "`,`" +
                            CORREO_USR + "`,`" + CORREO_HOST + "`," + '`' + CORREO_UVG +
                            "`) VALUES(?,?,?,?,?,?,?) ON DUPLICATE KEY " + "UPDATE `" +
                            ALUMNOS_NOMBRES + "`=?," + '`' + ALUMNOS_APELLIDO1 + "`=?,`" +
                            ALUMNOS_APELLIDO2 + "`=?," + '`' + CORREO_USR + "`=?,`" + CORREO_HOST +
                            "`=?,`" + CORREO_UVG + "`=?";
            try (final Connection conexion = MariaDBPool.getConexion();
                 PreparedStatement statement = conexion
                         .prepareStatement(prepStatement, Statement.RETURN_GENERATED_KEYS)) {
                // Preparamos los parámetros del Prepared Statement
                statement.setInt(1, m_identificador);
                statement.setString(2, m_nombres);
                statement.setString(3, m_primerApellido);
                statement.setString(4, m_segundoApellido);
                statement.setString(5, m_correoUsuario);
                statement.setString(6, m_correoHost);
                statement.setString(7, m_correoUniversidad);
                statement.setString(8, m_nombres);
                statement.setString(9, m_primerApellido);
                statement.setString(10, m_segundoApellido);
                statement.setString(11, m_correoUsuario);
                statement.setString(12, m_correoHost);
                statement.setString(13, m_correoUniversidad);
                statement.executeUpdate();
                try (ResultSet rs = statement.getGeneratedKeys()) {
                    if (rs.next()) {
                        final int genID = rs.getInt(1);
                        if (genID == 0) {
                            LOGGER.log(Level.WARNING,
                                    "No se ha ingresado ni modificado ningún Alumno en la base " +
                                            "de datos.");
                            /*
                             Esta parte del código es importante: su función es determinar si
                             el código ha fallado porque la Base de Datos ha denegado el
                             cambio o bien si ha fallado porque el objeto ya está ahí. Si el
                             objeto está idéntico en la Base de Datos, no hay problema. Sin
                             embargo, si los objetos difieren quiere decir que el cambio ha sido
                             denegado por cualquier motivo por la Base de Datos y que el nuevo
                             objeto cambiado es inválido. Para logarlo se utiliza un objeto de
                             tara, al cual se le coonfigura el ID de este objeto y se recupera de
                             la Base de Datos.
                             */
                            if (tara.equals(this)) {
                                LOGGER.log(Level.WARNING, "Este Alumno es indéntico al de la base" +
                                        " de datos. Ningún cambio efectuado.");
                            } else {
                                final CambioDenegadoException e = new CambioDenegadoException(
                                        "La Base de Datos ha rechazado el cambio.");
                                invalidar();
                                throw e;
                            }
                        } else {
                            m_sqlID = genID;
                            obtenerDesdeBaseDeDatos();
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "No se pudo instanciar un Alumno. Este puede ser un bug muy" +
                    " oscuro y extraño.");
            assert false;
        }
    }

    @Override
    protected void validar(final int sqlID) {
        if (sqlID >= 1) {
            m_sqlID = sqlID;
            MAPA_VALIDOS_MULTITON.put(m_identificador, this);
            LOGGER.log(Level.INFO, "Se ha agregado " + hashCode() + " al multitón.");
        }
    }

    @Override
    void obtenerDesdeBaseDeDatos() throws ValorInvalidoException {
        if ((m_sqlID <= 0) && (m_identificador <= 0)) {
            return;
        }
        // Si el objeto está validado usar el SQLID, si no, usar el ID
        final String query = "SELECT * FROM `" + ALUMNOS + "` WHERE " +
                (isValido() ? ALUMNOS_SQLID : ALUMNOS_ID) + "=?";
        try (final Connection conexion = MariaDBPool.getConexion();
             PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setInt(1, isValido() ? m_sqlID : m_identificador);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    // En teoría cualquier información en la Base de Datos es válida.
                    invalidar();
                    setNombres(result.getString(ALUMNOS_NOMBRES));
                    setPrimerApellido(result.getString(ALUMNOS_APELLIDO1));
                    setSegundoApellido(result.getString(ALUMNOS_APELLIDO2));
                    setCorreo(result.getString(CORREO_HOST), result.getString(CORREO_USR));
                    setCorreoUniversidad(result.getString(CORREO_UVG));
                    setIdentificador(result.getInt(ALUMNOS_ID));
                    m_clave = result.getBytes(CLAVE);
                    validar(result.getInt(ALUMNOS_SQLID));
                } else {
                    LOGGER.log(Level.WARNING, "No se ha encontrado el Alumno en la Base de Datos." +
                            " Verifique que el Alumno exista.");

                }
            }
        } catch (final NoEsUnNombreRealException e) {
            throw new ValorInvalidoException(
                    "El nombre recuperado de la Base de Datos es inválido.", e);
        } catch (final NoEsUnCorreoValidoException e) {
            throw new ValorInvalidoException(
                    "El correo recuperado de la Base de Datos es inválido.", e);
        } catch (final CambioDenegadoException e) {
            throw new ValorInvalidoException(
                    "Se ha rechazado el cambio de valores para este objeto.", e);
        } catch (final SQLException e) {
            // Invalidar el objeto y borrar la clave actual
            invalidar();
        }
    }

    /**
     * Busca una instancia del Alumno en el Multitón de Alumnos validados y devuelve dicha
     * instancia. Si no encuentra una instancia en le multitón, devuelve una nueva instancia.
     * Cabe recordar que el multitón solo lleva registro de las instancias validadas.
     *
     * @param id identificador único del Alumno.
     *
     * @return una instancia del Alumno
     */
    public static IMiembro getAlumnoById(final int id) {
        if (MAPA_VALIDOS_MULTITON.containsKey(id)) {
            final Alumno tmp = MAPA_VALIDOS_MULTITON.get(id);
            tmp.m_referencias += 1;
            LOGGER.log(Level.INFO, "Recuperando instancia para " + id);
            return tmp;
        } else {
            final Alumno tmp = new Alumno(id);
            LOGGER.log(Level.INFO, "Instanciado un nuevo Alumno para " + id);
            return tmp;
        }
    }

    @Override
    public void close() {
        if (MAPA_VALIDOS_MULTITON.contains(this)) {
            if (m_referencias > 1) {
                m_referencias -= 1;
            }
            if (m_referencias <= 0) {
                LOGGER.log(Level.WARNING, "BUG: Llamada ilegal a close() en Alumno");
                assert false;
            }
        }
    }

}
