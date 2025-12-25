package com.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.example.conexiones.DBConnection;

/**
 * DAO para la tabla `autores`.
 */
public class AutorDAO {
    /**
     * DBConnection para conexiones a la base de datos
     */
    private final DBConnection dbConnection;

    // SQL constants 🔧
    private static final String SQL_SELECT_AUTOR_ID_POR_NOMBRE = "SELECT id FROM autores WHERE nombre = ?";
    private static final String SQL_INSERT_AUTOR = "INSERT INTO autores (nombre, nacionalidad) VALUES (?, ?)";

    /**
     * Constructor del DAO
     * 
     * @param dbConnection
     */
    public AutorDAO(DBConnection dbConnection) {
        if (dbConnection == null) {
            throw new IllegalArgumentException("DBConnection cannot be null");
        }
        this.dbConnection = dbConnection;
    }

    /**
     * Busca el id del autor por nombre
     *
     * @param nombre nombre completo del autor
     * @return id si existe, -1 si no
     */
    public int obtenerIdPorNombre(String nombre) {
        try (Connection conexion = dbConnection.getConnection();
                PreparedStatement ps = conexion.prepareStatement(SQL_SELECT_AUTOR_ID_POR_NOMBRE)) {
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
     * Variante que usa una Connection existente (útil para transacciones)
     *
     * @param conexion Connection abierta
     * @param nombre   nombre del autor
     * @return id si existe o -1
     */
    public int obtenerIdPorNombre(Connection conexion, String nombre) {
        try (PreparedStatement ps = conexion.prepareStatement(SQL_SELECT_AUTOR_ID_POR_NOMBRE)) {
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
    public int crearAutor(String nombre, String nacionalidad) {
        try (Connection conexion = dbConnection.getConnection();
                PreparedStatement ps = conexion.prepareStatement(
                        SQL_INSERT_AUTOR,
                        Statement.RETURN_GENERATED_KEYS)) {
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
     * Variante que crea el autor usando una Connection existente (no cierra la
     * Connection)
     *
     * @param conexion     Connection activa
     * @param nombre       nombre del autor
     * @param nacionalidad codigo nacionalidad
     * @return id generado o -1
     */
    public int crearAutor(Connection conexion, String nombre, String nacionalidad) {
        try (PreparedStatement ps = conexion.prepareStatement(
                SQL_INSERT_AUTOR,
                Statement.RETURN_GENERATED_KEYS)) {
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
    public int obtenerOCrearPorNombre(String nombre) {
        int id = obtenerIdPorNombre(nombre);
        if (id != -1)
            return id;
        return crearAutor(nombre, "ES");
    }

    /**
     * Variante que opera con una Connection externa (útil para transacciones)
     *
     * @param conexion Connection activa
     * @param nombre   nombre del autor
     * @return id del autor o -1
     */
    public int obtenerOCrearPorNombre(Connection conexion, String nombre) {
        int id = obtenerIdPorNombre(conexion, nombre);
        if (id != -1)
            return id;
        return crearAutor(conexion, nombre, "ES");
    }
}
