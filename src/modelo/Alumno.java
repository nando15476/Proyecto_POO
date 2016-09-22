package modelo;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import controlador.IAdministrador;
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
    static final int INITIAL_CAPACITY = 25;
    /**
     * Nombre de la columna del ID único de SQL en la Base de Datos.
     */
    static final String ALUMNOS_SQLID = "SQLID";
    /**
     * Nombre de la columna del nombre de usuario de correo personal en la Base de Datos.
     */
    static final String CORREO_USR = "CORREO_USR";
    /**
     * Nombre de la tabla donde se almacenan los Alumnos.
     */
    @NonNls
    static final String ALUMNOS = "Alumnos";
    /**
     * Nombre de la columna donde se almacenan los nombres de los Alumnos.
     */
    @NonNls
    static final String ALUMNOS_NOMBRES = "NOMBRES";
    /**
     * Nombre de la columna donde se almacena el primer apellido de los Alumnos.
     */
    @NonNls
    static final String ALUMNOS_APELLIDO1 = "APELLIDO1";
    /**
     * Nombre de la columna donde se almacena el segundo apellido de los Alumnos.
     */
    @NonNls
    static final String ALUMNOS_APELLIDO2 = "APELLIDO2";
    /**
     * <p>Longitud máxima permitida para un nombre de una persona miembro de la Universidad.</p>
     * <b>¡ADVERTENCIA!</b>: Este valor coincide con el valor establecido en la Base de Datos, o
     * es {@code 0} si la Base de Datos no puede accederse.
     */
    static final int NOMBRE_LEN;
    /**
     * <p>Longitud máxima permitida para un apellido de una persona miembro de la Universidad.</p>
     * <b>¡ADVERTENCIA!</b>: Este valor coincide con el valor establecido en la Base de Datos, o
     * es {@code 0} si la Base de Datos no puede accederse.
     */
    static final int APELLIDO1_LEN;
    /**
     * <p>Longitud máxima permitida para un segundo apellido de una persona miembro de la
     * Universidad.</p>
     * <b>¡ADVERTENCIA!</b>: Este valor coincide con el valor establecido en la Base de Datos, o
     * es {@code 0} si la Base de Datos no puede accederse.
     */
    static final int APELLIDO2_LEN;
    /**
     * Nombre de la columna del ID en la Base de Datos.
     */
    static final String ALUMNOS_ID = "ID";
    /**
     * Nombre de la columna del host de correo personal en la Base de Datos.
     */
    static final String CORREO_HOST = "CORREO_HST";
    /**
     * Nombre de la columna del usuario de correo de la UVG en la Base de Datos.
     */
    static final String CORREO_UVG = "CORREO_UVG";
    /**
     * Nombre de la columna de la contraseña/clave en la Base de Datos.
     */
    static final String CLAVE = "CLAVE";
    private static final transient ConcurrentHashMap<Integer, Alumno> MAPA_VALIDOS_MULTITON =
            new ConcurrentHashMap<>(INITIAL_CAPACITY);

    static {
        final int nombreLenTmp;
        final int apellido1LenTmp;
        final int apellido2LenTmp;
        try (Connection conexion = MariaDBPool.getConexion()) {
            try (Statement statement = conexion.createStatement();
                 ResultSet result = statement.executeQuery(SELECT_FROM + ALUMNOS)) {
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
    }

    private int m_referencias;

    /**
     * Crea un nuevo Alumno con un ID.
     *
     * @param id ID para el alumno
     */
    protected Alumno(final int id) {
        m_identificador = id;
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
    public static Alumno getAlumnoById(final int id) {
        if (MAPA_VALIDOS_MULTITON.containsKey(id)) {
            final Alumno tmp = MAPA_VALIDOS_MULTITON.get(id);
            tmp.m_referencias += 1;
            return tmp;
        } else {
            return new Alumno(id);
        }
    }

    @Override
    public void close() {
        if (MAPA_VALIDOS_MULTITON.contains(this)) {
            if (m_referencias <= 1) {
                MAPA_VALIDOS_MULTITON.remove(m_identificador);
            } else {
                m_referencias -= 1;
            }
        }
    }

    @Override
    public void setNombres(@NonNls @NotNull final String nombres)
            throws NoEsUnNombreRealException, CambioDenegadoException {
        if (m_sqlID == -1) {
            if (nombres.length() > NOMBRE_LEN) {
                throw new CambioDenegadoException("El nombre es demasiado largo.");
            }
            final String[] testList = nombres.split(" ");
            for (final String s : testList) {
                // Si el nombre no parece ser un nombre real...
                if (!NOMBRE_REGEX.matcher(s).matches()) {
                    // Lanza una excepción
                    throw new NoEsUnNombreRealException(s);
                }
            }
            m_nombres = nombres;
        } else {
            throw new CambioDenegadoException(YA_VALIDADO_MSG);
        }
    }

    @Override
    public void setPrimerApellido(@NonNls @NotNull final String primerApellido)
            throws NoEsUnNombreRealException, CambioDenegadoException {
        if (m_sqlID == -1) {
            if (primerApellido.length() > APELLIDO1_LEN) {
                throw new CambioDenegadoException("El primer apellido es demasiado largo.");
            }
            final String[] testList = primerApellido.split(" ");
            for (final String s : testList) {
                // Si el nombre no parece ser un nombre real...
                if (!NOMBRE_REGEX.matcher(s).matches()) {
                    // Lanza una excepción
                    throw new NoEsUnNombreRealException(s);
                }
            }
            m_primerApellido = primerApellido;
        } else {
            throw new CambioDenegadoException(YA_VALIDADO_MSG);
        }
    }

    @Override
    public void setSegundoApellido(@NonNls @NotNull final String segundoApellido)
            throws NoEsUnNombreRealException, CambioDenegadoException {
        if (m_sqlID == -1) {
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
        } else {
            throw new CambioDenegadoException(YA_VALIDADO_MSG);
        }
    }

    @Override
    public void setCorreoUniversidad(@NonNls @NotNull final String correoUniversidad)
            throws NoEsUnCorreoValidoException, CambioDenegadoException {
        if (m_sqlID == -1) {
            if (Pattern.compile("\\w{3,4}\\d{5,6}$").matcher(correoUniversidad).matches()) {
                m_correoUniversidad = correoUniversidad;
            } else {
                throw new NoEsUnCorreoValidoException(correoUniversidad);
            }
        } else {
            throw new CambioDenegadoException(YA_VALIDADO_MSG);
        }
    }

    @Override
    public void makeCorreoU() throws CambioDenegadoException, NoEsUnCorreoValidoException {
        if (m_sqlID == -1) {
            // En español, nuestro apellidos tienen símbolos raros no permitidos en direcciones
            // de correo.
            String primerA =
                    Normalizer.normalize(m_primerApellido, Form.NFD).toLowerCase(Locale.ENGLISH);
            primerA = UNICODE_MATCHER.matcher(primerA).replaceAll("");
            // Separamos los apellidos como De Leon o De La Cruz para jugar con ellos uno por uno
            final String[] split = primerA.split(" ");
            final StringBuilder ucorreo = new StringBuilder(7);
        /*
         * En los correos de la U se usan 3 letras si la primera palabra del apellido tiene
         * más o 3 caracteres. Si tiene menos de 3, entonces se utiliza la siguient palabra
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
            if (m_identificador == -1) {
                throw new CambioDenegadoException(
                        "No se puede generar un correo de la Universidad para un Alumno cuyo ID" +
                                " aún no se ha configurado.");
            }
            ucorreo.append(m_identificador);
            setCorreoUniversidad(ucorreo.toString());
        } else {
            throw new CambioDenegadoException(YA_VALIDADO_MSG);
        }
    }

    @Override
    public void obtenerDesdeBaseDeDatos() throws SQLException, ValorInvalidoException {
        try (final Connection conexion = MariaDBPool.getConexion()) {
            try (Statement statement = conexion.createStatement(); ResultSet result = statement
                    .executeQuery(SELECT_FROM + ALUMNOS + " WHERE " + ALUMNOS_ID + "='" +
                            m_identificador + '\'')) {
                if (result.next()) {
                    // En teoría cualquier información en la Base de Datos es válida.
                    m_sqlID = -1;
                    setNombres(result.getString(ALUMNOS_NOMBRES));
                    setPrimerApellido(result.getString(ALUMNOS_APELLIDO1));
                    setSegundoApellido(result.getString(ALUMNOS_APELLIDO2));
                    setCorreo(result.getString(CORREO_HOST), result.getString(CORREO_USR));
                    setCorreoUniversidad(result.getString(CORREO_UVG));
                    m_sqlID = result.getInt(ALUMNOS_SQLID);
                    m_clave = result.getBytes(CLAVE);
                    MAPA_VALIDOS_MULTITON.put(m_identificador, this);
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
            }
        }
    }

    @Override
    public void guardarEnBaseDeDatos(final IAdministrador admin)
            throws SQLException, PermisoDenegadoException {
        if (admin.isAutorizado()) {
            final String query =
                    "INSERT INTO `" + ALUMNOS + "`(`" + ALUMNOS_ID + "`," + '`' + ALUMNOS_NOMBRES +
                            "`,`" + ALUMNOS_APELLIDO1 + "`," + '`' + ALUMNOS_APELLIDO2 + "`,`" +
                            CORREO_USR + "`,`" + CORREO_HOST + "`," + '`' + CORREO_UVG +
                            "`) VALUES (" + getIdentificador() + ",'" + getNombres() + "'," + '\'' +
                            getPrimerApellido() + "','" + getSegundoApellido() + "','" +
                            getCorreoUsuario() + "'," + '\'' + getCorreoHost() + "','" +
                            getCorreoUniversidad() + "') ON DUPLICATE KEY UPDATE " +
                            ALUMNOS_NOMBRES + "=\'" + getNombres() + "\', " + ALUMNOS_APELLIDO1 +
                            "=\'" + getPrimerApellido() + "\', " + ALUMNOS_APELLIDO2 + "=\'" +
                            getSegundoApellido() + "\', " + CORREO_USR + "=\'" +
                            getCorreoUsuario() + "\', " + CORREO_HOST + "=\'" + getCorreoHost() +
                            "\', " + CORREO_UVG + "=\'" + getCorreoUniversidad() + '\'';
            try (final Connection conexion = MariaDBPool.getConexion()) {
                try (Statement statement = conexion.createStatement()) {
                    statement.executeUpdate(query);
                }
            }
        } else {
            throw new PermisoDenegadoException(admin);
        }
    }

}
