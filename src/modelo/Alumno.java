package modelo;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.Normalizer;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import controlador.MariaDBPool;
import excepciones.CambioDenegadoException;
import excepciones.ErrorIrrecuperable;
import excepciones.NoEsUnNombreRealException;

/**
 * Created by notengobattery on 12/09/16.
 */
public class Alumno extends Miembro {
    /**
     * Nombre de la tabla donde se almacenan los Alumnos.
     */
    @NonNls
    public static final String ALUMNOS = "Alumnos";
    /**
     * Nombre de la columna donde se almacenan los nombres de los Alumnos.
     */
    @NonNls
    public static final String ALUMNOS_NOMBRES = "NOMBRES";
    /**
     * Nombre de la columna donde se almacena el primer apellido de los Alumnos.
     */
    @NonNls
    public static final String ALUMNOS_APELLIDO1 = "APELLIDO1";
    /**
     * Nombre de la columna donde se almacena el segundo apellido de los Alumnos.
     */
    @NonNls
    public static final String ALUMNOS_APELLIDO2 = "APELLIDO2";
    /**
     * <p>Longitud máxima permitida para un nombre de una persona miembro de la Universidad.</p>
     * <b>¡ADVERTENCIA!</b>: Este valor coincide con el valor establecido en la Base de Datos, o
     * es {@code 0} si la base de datos no puede accederse.
     */
    protected static final int NOMBRE_LEN;
    /**
     * <p>Longitud máxima permitida para un apellido de una persona miembro de la Universidad.</p>
     * <b>¡ADVERTENCIA!</b>: Este valor coincide con el valor establecido en la Base de Datos, o
     * es {@code 0} si la base de datos no puede accederse.
     */
    protected static final int APELLIDO1_LEN;
    /**
     * <p>Longitud máxima permitida para un segundo apellido de una persona miembro de la
     * Universidad.</p>
     * <b>¡ADVERTENCIA!</b>: Este valor coincide con el valor establecido en la Base de Datos, o
     * es {@code 0} si la base de datos no puede accederse.
     */
    protected static final int APELLIDO2_LEN;

    static {
        int nombreLenTmp = 0;
        int apellido1LenTmp = 0;
        int apellido2LenTmp = 0;
        final Logger logger = Logger.getLogger(Miembro.class.getCanonicalName());
        try (Connection conexion = MariaDBPool.getConexion()) {
            assert conexion != null;
            try (Statement statement = conexion.createStatement()) {
                try (ResultSet result = statement.executeQuery("SELECT * FROM " + ALUMNOS)) {
                    final ResultSetMetaData meta = result.getMetaData();
                    nombreLenTmp = meta.getColumnDisplaySize(result.findColumn(ALUMNOS_NOMBRES));
                    apellido1LenTmp =
                            meta.getColumnDisplaySize(result.findColumn(ALUMNOS_APELLIDO1));
                    apellido2LenTmp =
                            meta.getColumnDisplaySize(result.findColumn(ALUMNOS_APELLIDO2));
                }
            }
        } catch (final SQLException e) {
            logger.log(Level.SEVERE, "La conexión o la consulta falló al ejecutarse.");
            throw new ErrorIrrecuperable(e);
        }
        NOMBRE_LEN = nombreLenTmp;
        APELLIDO1_LEN = apellido1LenTmp;
        APELLIDO2_LEN = apellido2LenTmp;
    }

    private final Logger m_logger = Logger.getLogger(getClass().getCanonicalName());


    @Override
    public void setNombres(@NonNls @NotNull final String nombres)
            throws NoEsUnNombreRealException, CambioDenegadoException {
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
    }

    @Override
    public void setPrimerApellido(@NonNls @NotNull final String primerApellido)
            throws NoEsUnNombreRealException, CambioDenegadoException {
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
    }

    @Override
    public void setSegundoApellido(@NonNls @NotNull final String segundoApellido)
            throws NoEsUnNombreRealException, CambioDenegadoException {
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

    @Override
    public void makeCorreoU() throws CambioDenegadoException {
        // En español, nuestro apellidos tienen símbolos raros no permitidos en direcciones
        // de correo.
        String primerA = Normalizer.normalize(m_primerApellido, Normalizer.Form.NFD)
                .toLowerCase(Locale.ENGLISH);
        primerA = UNICODE_MATCHER.matcher(primerA).replaceAll("");
        // Separamos los apellidos como De Leon o De La Cruz para jugar con ellos uno por uno
        final String[] split = primerA.split(" ");
        final StringBuilder ucorreo = new StringBuilder();
        /*
         * En los correos de la U se usan 3 letras si la primera palabra del apellido tiene
         * más o 3 caracteres. Si tiene menos de 3, entonces se utiliza la siguient palabra
         * para ajustar un total de 4 letras.
         */
        
        if (m_id == -1) {
            throw new CambioDenegadoException(
                    "No se puede generar un correo de la Universidad de un Alumno inválido.");
        }
        ucorreo.append(m_id);
        setCorreoU(ucorreo.toString());
    }

    @Override
    void setCorreoU(@NonNls @NotNull final String correo) {

    }

}
