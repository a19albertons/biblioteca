package com.biblioteca.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * DAO para la tabla `temas`.
 */
public class TemaDAO {
    /**
     * DBConnection para conexiones a la base de datos
     */
    private final Connection conexion;

    /**
     * Constante SQL para obtener el id de un tema por su nombre
     */
    private static final String SQL_SELECT_TEMA_ID_POR_NOMBRE = "SELECT id FROM temas WHERE nombre = ?";

    /**
     * Constante SQL para insertar un nuevo tema
     */
    private static final String SQL_INSERT_TEMA = "INSERT INTO temas (nombre) VALUES (?)";

    /**
     * Constructor del DAO
     * 
     * @param conexion la conexión a la base de datos
     * @throws IllegalArgumentException si conexion es null
     */
    public TemaDAO(final Connection conexion) {
        if (conexion == null) {
            throw new IllegalArgumentException("Connection cannot be null");
        }
        this.conexion = conexion;
    }

    /**
     * Obtiene id por nombre
     * 
     * @param nombre nombre del tema
     * @return el id del tema o -1 si no se encuentra
     */
    public final int obtenerIdPorNombre(final String nombre) {
        try (
                PreparedStatement ps = conexion.prepareStatement(SQL_SELECT_TEMA_ID_POR_NOMBRE)) {
            // establecer parámetro
            ps.setString(1, nombre);

            // ejecutar consulta
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
     * Crea un tema y devuelve su id
     * 
     * @param nombre nombre del tema
     * @return el id del tema creado o -1 si ocurre un error
     */
    public final int crearTema(final String nombre) {
        try (
                PreparedStatement ps = conexion.prepareStatement(SQL_INSERT_TEMA,
                        Statement.RETURN_GENERATED_KEYS)) {
            // establecer parámetro
            ps.setString(1, nombre);
            // ejecutar
            ps.executeUpdate();
            // obtener id generado
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            // debug
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
        return -1;
    }

    /**
     * Obtiene o crea un tema
     * 
     * @param nombre nombre del tema
     * @return el id del tema o -1 si no se encuentra
     */
    public final int obtenerOCrear(final String nombre) {
        int id = obtenerIdPorNombre(nombre);
        if (id != -1) {
            return id;
        }
        return crearTema(nombre);
    }

    /**
     * Variante que usa una Connection existente
     * 
     * @param conexion la conexión a la base de datos
     * @param nombre   nombre del tema
     * @return el id del tema o -1 si no se encuentra
     */
    public final int obtenerOCrear(final Connection conexion, final String nombre) {
        try (PreparedStatement ps = conexion.prepareStatement(SQL_SELECT_TEMA_ID_POR_NOMBRE)) {
            // establecer parámetro
            ps.setString(1, nombre);
            // ejecutar consulta
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
            // Si no existe, insertamos
            try (PreparedStatement ins = conexion.prepareStatement(SQL_INSERT_TEMA,
                    Statement.RETURN_GENERATED_KEYS)) {
                ins.setString(1, nombre);
                ins.executeUpdate();
                try (ResultSet rs2 = ins.getGeneratedKeys()) {
                    if (rs2.next()) {
                        return rs2.getInt(1);
                    }
                }
            }
        } catch (Exception e) {
            // debug
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
        return -1;
    }
}
