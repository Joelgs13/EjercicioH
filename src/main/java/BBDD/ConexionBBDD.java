package BBDD;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Clase para gestionar la conexión con la base de datos MariaDB.
 * Se encarga de establecer y cerrar la conexión con la base de datos.
 */
public class ConexionBBDD {

    /** Conexión activa a la base de datos. */
    private final Connection connection;

    /**
     * Constructor que establece la conexión con la base de datos.
     * Configura las propiedades de usuario y contraseña, y realiza la conexión
     * a una base de datos MariaDB en la dirección especificada.
     *
     * @throws SQLException si ocurre un error al establecer la conexión
     */
    public ConexionBBDD() throws SQLException {
        Properties connConfig = new Properties();
        connConfig.setProperty("user", "root");
        connConfig.setProperty("password", "mypass");
        connection = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:33066/personas?serverTimezone=Europe/Madrid", connConfig);
        connection.setAutoCommit(true);

        // Meta información de la base de datos para depuración (comentada en producción)
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        /*
         System.out.println();
         System.out.println("--- Datos de conexión ------------------------------------------");
         System.out.printf("Base de datos: %s%n", databaseMetaData.getDatabaseProductName());
         System.out.printf("  Versión: %s%n", databaseMetaData.getDatabaseProductVersion());
         System.out.printf("Driver: %s%n", databaseMetaData.getDriverName());
         System.out.printf("  Versión: %s%n", databaseMetaData.getDriverVersion());
         System.out.println("----------------------------------------------------------------");
         System.out.println();
         */
        connection.setAutoCommit(true);
    }

    /**
     * Devuelve la conexión actual a la base de datos.
     *
     * @return la conexión activa
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Cierra la conexión activa con la base de datos.
     *
     * @return la conexión cerrada
     * @throws SQLException si ocurre un error al cerrar la conexión
     */
    public Connection CloseConexion() throws SQLException {
        connection.close();
        return connection;
    }
}