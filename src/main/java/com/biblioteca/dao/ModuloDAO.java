package com.biblioteca.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * DAO para la tabla `modulo` (módulos).
 */
public class ModuloDAO {
    /**
     * DBConnection para conexiones a la base de datos
     */
    private final Connection conexion;

    // SQL constants 🔧
    /**
     * SQL query para obtener el id de un módulo por su nombre.
     */
    private static final String SQL_SELECT_MODULO_ID_POR_NOMBRE = "SELECT id FROM modulo WHERE nombre = ?";
    /**
     * SQL query para insertar un nuevo módulo.
     */
    private static final String SQL_INSERT_MODULO = "INSERT INTO modulo (nombre) VALUES (?)";

    /**
     * Constructor del DAO.
     * 
     * @param conexion conexión a la base de datos
     */
    public ModuloDAO(final Connection conexion) {
        if (conexion == null) {
            throw new IllegalArgumentException("conexion cannot be null");
        }
        this.conexion = conexion;
    }

    /**
     * Obtiene id por nombre
     *
     * @param nombre
     * @return id o -1
     */
    public int obtenerIdPorNombre(final String nombre) {
        try (PreparedStatement ps = conexion.prepareStatement(SQL_SELECT_MODULO_ID_POR_NOMBRE)) {
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
     * Crea un módulo si no existe y devuelve su id.
     * 
     * @param nombre nombre del módulo a crear
     * @return id del módulo creado o -1 en caso de error
     */
    public int crearModulo(final String nombre) {
        try (PreparedStatement ps = conexion.prepareStatement(SQL_INSERT_MODULO, Statement.RETURN_GENERATED_KEYS)) {
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
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
        return -1;
    }

    /**
     * Obtiene o crea un módulo por nombre y devuelve su id
     * 
     * @param nombre nombre del módulo
     * @return id del módulo o -1 en caso de error
     */
    public int obtenerOCrear(final String nombre) {
        int id = -1;
        try (PreparedStatement ps = conexion.prepareStatement(SQL_SELECT_MODULO_ID_POR_NOMBRE)) {
            // establecer parámetro
            ps.setString(1, nombre);
            // ejecutar consulta
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
            // Si no existe, insertamos
            try (PreparedStatement ins = conexion.prepareStatement(SQL_INSERT_MODULO,
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
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
        return id;
    }
}
