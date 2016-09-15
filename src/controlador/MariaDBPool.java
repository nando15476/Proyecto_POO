package controlador;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import snaq.db.ConnectionPool;

/**
 * La clase MariaDBPool inicializa una {@link Connection} (conexión) a la base de datos de MariaDB.
 * Esta clase está delegada desde {@link vista}, dado que la conexión a la base de datos es
 * prioridad sobre la dispocisión de esta clase. Esta clase es una clase utilitaria.
 */
@SuppressWarnings("UseOfSystemOutOrSystemErr")
public enum MariaDBPool {
    ;
    public static final int TAMANO_MAX = 10;
    public static final long TIEMPO_INACTIVO = 1000L;
    public static final int MAX_POOL = 5;
    public static final int MIN_POOL = 1;
    public static final String ULR_BASE_DE_DATOS = "jdbc:mariadb://localhost:3306/";
    public static final String NOMBRE_POOL = "maria-pool";
    @NonNls
    public static final String NOMBRE_BASE_DE_DATOS = "UVGDB";
    public static final String DRIVER = "org.mariadb.jdbc.Driver";
    @NonNls
    private static final Logger LOGGER = Logger.getLogger(MariaDBPool.class.getCanonicalName());
    @Nullable
    private static final ConnectionPool CONNECTION_POOL;

    static {
        ConnectionPool tmpPool = null;
        boolean invalido = true;
        while (invalido) {
            final Scanner scanner = new Scanner(System.in, "UTF-8");
            try {
                final Class driverClass = Class.forName(DRIVER);
                final Driver driver = (Driver) driverClass.getConstructor().newInstance();
                DriverManager.registerDriver(driver);
                System.out.println("Bienvenido al inicializador de la conexión con MariaDB.");
                // Repite la petición hasta que las credenciales sean válidas.

                System.out.println(
                        "Antes de continuar con el programa, ingrese las credenciales para" +
                                " acceder a MariaDB.");
                System.out.println("Ingrese el nombre de usuario de MariaDB:");
                final String username = scanner.nextLine();
                System.out.println(
                        "Ingrese la contraseña del usuario \"" + username + "\" de MariaDB:");
                final String clave = scanner.nextLine();
                //noinspection ObjectAllocationInLoop
                tmpPool = new ConnectionPool(NOMBRE_POOL, MIN_POOL, MAX_POOL, TAMANO_MAX,
                        TIEMPO_INACTIVO, ULR_BASE_DE_DATOS, username, clave);
                //noinspection resource
                final Connection con = tmpPool.getConnection();
                // Con esta aseguramos la existencia de la base de datos
                try (Statement statement = con.createStatement()) {
                    try (ResultSet resultSet = statement.executeQuery(
                            "CREATE DATABASE IF NOT EXISTS " + NOMBRE_BASE_DE_DATOS)) {
                        LOGGER.log(Level.INFO, "Conexión abierta y funcional.",
                                resultSet.toString());
                        // Selecciona la base de datos que vamos a usar
                        con.setCatalog(NOMBRE_BASE_DE_DATOS);
                        invalido = false;
                    }
                }
            } catch (final ClassNotFoundException e) {
                LOGGER.log(Level.SEVERE,
                        "No se ha encontrado la clase. ¿Se han satisfecho todas las dependencias?",
                        e);
            } catch (final InstantiationException e) {
                LOGGER.log(Level.SEVERE, "La clase no pudo instanciarse.", e);
            } catch (final IllegalAccessException e) {
                LOGGER.log(Level.SEVERE, "¡Acceso ilegal!", e);
            } catch (final SQLException e) {
                LOGGER.log(Level.SEVERE, "¡Error en el acceso a SQL!", e);
            } catch (final NoSuchMethodException e) {
                LOGGER.log(Level.SEVERE, "La clase instanciada no contiene el método solicitado.",
                        e);
            } catch (final InvocationTargetException e) {
                LOGGER.log(Level.SEVERE, "No se pudo invocar el objetivo.", e);
            } finally {
                scanner.close();
            }
        }
        CONNECTION_POOL = tmpPool;

    }

    /**
     * Inicializa la base de datos. Cuando el programa se ejecuta en una computadora que no tiene
     * la base de datos importada, es necesario crear un nuevo esqueleto para la base de datos.
     * En este método se define este esqueleto. Para generalizar, en este método se describe la
     * base de datos por completo.
     */
    public static void makeBaseDeDatos() throws SQLException {
        final Connection conexion = getConexion();
        if (conexion == null) {
            LOGGER.log(Level.SEVERE,
                    "No se puede verificar la base de datos sin una conexión válida.");
        } else {
            try (ResultSet resultados = conexion.createStatement()
                    .executeQuery("CREATE TABLE IF NOT EXISTS `" + NOMBRE_BASE_DE_DATOS + "`" +
                            ".`Alumnos`")) {

            }
        }

    }

    /**
     * Este método devuelve una conexión ({@link Connection}) de la pila común de conexiones SQL.
     * Esta conexión sirve para crear declaraciones ({@link Statement}), y obtener los juegos de
     * resultados ({@link ResultSet}) donde se encuentran los resultados de la ejecución de la
     * consulta.
     *
     * @return una conexión a SQL
     */
    @Nullable
    public static Connection getConexion() {
        try {
            return CONNECTION_POOL.getConnection();
        } catch (final SQLException e) {
            LOGGER.log(Level.SEVERE,
                    "No se ha podido generar una conexión con la base de datos de MariaDB.", e);
            return null;
        }
    }
}
