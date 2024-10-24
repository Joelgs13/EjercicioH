package Dao;

import BBDD.ConexionBBDD;
import Model.Persona;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DaoPersona {

    public List<Persona> obtenerTodas() throws SQLException {
        // Crear una instancia de ConexionBBDD
        ConexionBBDD conn = new ConexionBBDD();
        // Obtener la conexión de la instancia
        Connection conexion = conn.getConnection();
        List<Persona> personas = new ArrayList<>();
        String query = "SELECT * FROM Persona";

        try (Statement statement = conexion.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Persona persona = new Persona(
                        resultSet.getInt("id"),
                        resultSet.getString("nombre"),
                        resultSet.getString("apellidos"),
                        resultSet.getInt("edad")
                );
                personas.add(persona);
            }
        } finally {
            // Asegúrate de cerrar la conexión al final
            conn.CloseConexion();
        }

        return personas;
    }

    public void agregar(Persona persona) throws SQLException {
        // Crear una instancia de ConexionBBDD
        ConexionBBDD conn = new ConexionBBDD();
        Connection conexion = conn.getConnection();
        String query = "INSERT INTO Persona (nombre, apellidos, edad) VALUES (?, ?, ?)";

        try (PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setString(1, persona.getNombre());
            statement.setString(2, persona.getApellido());
            statement.setInt(3, persona.getEdad());
            statement.executeUpdate();
        } finally {
            // Asegúrate de cerrar la conexión al final
            conn.CloseConexion();
        }
    }

    public void modificar(Persona persona) throws SQLException {
        // Crear una instancia de ConexionBBDD
        ConexionBBDD conn = new ConexionBBDD();
        Connection conexion = conn.getConnection();
        String query = "UPDATE Persona SET nombre = ?, apellidos = ?, edad = ? WHERE id = ?";

        try (PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setString(1, persona.getNombre());
            statement.setString(2, persona.getApellido());
            statement.setInt(3, persona.getEdad());
            statement.setInt(4, persona.getId());
            statement.executeUpdate();
        } finally {
            // Asegúrate de cerrar la conexión al final
            conn.CloseConexion();
        }
    }

    public void eliminar(int id) throws SQLException {
        // Crear una instancia de ConexionBBDD
        ConexionBBDD conn = new ConexionBBDD();
        Connection conexion = conn.getConnection();
        String query = "DELETE FROM Persona WHERE id = ?";

        try (PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } finally {
            // Asegúrate de cerrar la conexión al final
            conn.CloseConexion();
        }
    }
}
