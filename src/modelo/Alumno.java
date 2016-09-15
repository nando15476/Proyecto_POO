package modelo;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import controlador.MariaDBPool;


import static controlador.MariaDBPool.ALUMNOS;
import static controlador.MariaDBPool.ALUMNOS_NOMBRES;

/**
 * Created by notengobattery on 12/09/16.
 */
public class Alumno extends Miembro {
    /**
     * <p>Longitud máxima permitida para un nombre de una persona miembro de la Universidad.</p>
     * <b>¡ADVERTENCIA!</b>: Este valor debe coincidir con el valor establecido en la Base de Datos.
     */
    protected static final int NOMBRE_LEN;

    static {
        int nombreLenTmp = 0;
        final Logger logger = Logger.getLogger(Miembro.class.getCanonicalName());
        try (Connection conexion = MariaDBPool.getConexion()) {
            assert conexion != null;
            try (Statement statement = conexion.createStatement()) {
                try (ResultSet result = statement
                        .executeQuery("SELECT " + ALUMNOS_NOMBRES + " FROM " + ALUMNOS)) {
                    nombreLenTmp = result.getMetaData()
                            .getColumnDisplaySize(result.findColumn(ALUMNOS_NOMBRES));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "La conexión o la consulta falló al ejecutarse.", e);
        }
        NOMBRE_LEN = nombreLenTmp;
    }

    private final Logger m_logger = Logger.getLogger(getClass().getCanonicalName());

    /**
     * Crea un nuevo objeto Alumno inválido. La instanciación directa de objetos Miembro y sus
     * herederos está prohibida, sin embargo, un constructor público o protegido es necesario
     * para la herencia y el polimorfismo. La forma correcta de recuperar y generar instancias de
     * estas clases es utilizando los métodos fábrica.
     *
     * @throws NoSuchAlgorithmException cuando el algoritmo HASH no está disponible.
     */
    protected Alumno() throws NoSuchAlgorithmException {
    }
}
