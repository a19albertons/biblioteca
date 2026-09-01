package com.biblioteca.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * DAO para la tabla `autores`.
 */
public class AutorDAO {
    /**
     * conexión a usar en la base de datos. Se inyecta desde afuera para permitir
     * transacciones y pruebas.
     */
    private final Connection conexion;

    // SQL constants 🔧
    /**
     * SQL query para obtener el ID de un autor por nombre.
     */
    private static final String SQL_SELECT_AUTOR_ID_POR_NOMBRE = "SELECT id FROM autores WHERE nombre = ?";
    /**
     * SQL query para insertar un nuevo autor.
     */
    private static final String SQL_INSERT_AUTOR = "INSERT INTO autores (nombre, nacionalidad) VALUES (?, ?)";

    /**
     * Constructor del DAO
     * 
     * @param conexion conexión a la base de datos (no puede ser null)
     */
    public AutorDAO(final Connection conexion) {
        if (conexion == null) {
            throw new IllegalArgumentException("Connection cannot be null");
        }
        this.conexion = conexion;
    }

    /**
     * Busca el id del autor por nombre
     *
     * @param nombre nombre completo del autor
     * @return id si existe, -1 si no
     */
    public final int obtenerIdPorNombre(final String nombre) {
        try (
                PreparedStatement ps = conexion.prepareStatement(SQL_SELECT_AUTOR_ID_POR_NOMBRE)) {
            // establecer parámetro
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
        return -1;
    }

    /**
     * Inserta un nuevo autor y devuelve su id generado.
     * Asigna nacionalidad 'ES' por defecto si no se proporciona.
     *
     * @param nombre       nombre del autor
     * @param nacionalidad código de nacionalidad (ej: ES)
     * @return id generado o -1 en caso de error
     */
    public final int crearAutor(final String nombre, final String nacionalidad) {
        try (
                // preparar sentencia
                PreparedStatement ps = conexion.prepareStatement(
                        SQL_INSERT_AUTOR,
                        Statement.RETURN_GENERATED_KEYS)) {
            // establecer parámetros
            ps.setString(1, nombre);
            ps.setString(2, nacionalidad == null || nacionalidad.trim().isEmpty() ? "ES" : nacionalidad);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
        return -1;
    }

    /**
     * Obtiene el id del autor, creándolo si no existe.
     *
     * @param nombre nombre del autor
     * @return id del autor o -1 en caso de error
     */
    public final int obtenerOCrearPorNombre(final String nombre) {
        int id = obtenerIdPorNombre(nombre);
        if (id != -1) {
            return id;
        }
        return crearAutor(nombre, "ES");
    }
}
