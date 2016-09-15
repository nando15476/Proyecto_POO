package modelo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import controlador.MariaDBPool;

/**
 * La clase Miembro, del paquete {@link modelo} es el modelo fundamental de las clases
 * {@link Alumno}, {@link Tutor}, {@link Auxiliar}, {@link Administrador} y {@link Catedratico}.
 * Esta clase define a un miembro de la Universidad, y contiene toda la información que todos los
 * miembros de la Universidad deben brindar para pertenecer a la institución.
 */
public abstract class Miembro {
    private final Logger m_logger = Logger.getLogger(getClass().getCanonicalName());
    //private final byte[] m_contraseniaHash;
    //private final int m_sqlID;
    private final int m_id;
    private final MessageDigest m_messageDigest = MessageDigest.getInstance("SHA-512");
    private String m_nombres;
    private String m_apellidos;
    private String m_correoPersonal;
    private String m_correoUniversidad;
    // Token token;


    /**
     * Crea un nuevo objeto Miembro. La información que se provee en el constructor sirve para
     * recuperar o crear las correspondientes entradas desde la base de datos. En el caso de la
     * recuperación, sólo hace falta utilizar el identificador. En el caso de la creación de un
     * campo en la base de datos, la información está sujeta a posteriores pruebas, por lo que el
     * objeto creado no necesariamente representa un objeto presente en la base de datos. Los
     * Miembros no presentes en la base de datos no tienen permiso para solicitar un {@link Token}.
     *
     * @param id identificador único del miembro
     *
     * @throws NoSuchAlgorithmException cuando el algoritmo HASH no está disponible.
     */
    public Miembro(int id) throws NoSuchAlgorithmException {
        m_id = id;
    }

    /**
     * Obtiene la información básica del Miembro desde la base de datos.
     */
    public void getDesdeBaseDeDatos() {
        try (Connection con = MariaDBPool.getConexion()) {
            if (con != null) {
                try (Statement sta = con.createStatement()) {
                    //try (ResultSet res = sta.executeQuery()) {

                    //}
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
