package com.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.example.conexiones.DBConnection;

/**
 * DAO para la tabla `temas`.
 */
public class TemaDAO {
    /**
     * DBConnection para conexiones a la base de datos
     */
    private final DBConnection dbConnection;

    // SQL constants 🔧
    private static final String SQL_SELECT_TEMA_ID_POR_NOMBRE = "SELECT id FROM temas WHERE nombre = ?";
    private static final String SQL_INSERT_TEMA = "INSERT INTO temas (nombre) VALUES (?)";

    /**
     * Constructor del DAO
     * 
     * @param dbConnection
     */
    public TemaDAO(DBConnection dbConnection) {
        if (dbConnection == null) {
            throw new IllegalArgumentException("DBConnection cannot be null");
        }
        this.dbConnection = dbConnection;
    }

    /**
     * Obtiene id por nombre
     * 
     * @param nombre
     * @return
     */
    public int obtenerIdPorNombre(String nombre) {
        try (Connection conexion = dbConnection.getConnection();
                PreparedStatement ps = conexion.prepareStatement(SQL_SELECT_TEMA_ID_POR_NOMBRE)) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return rs.getInt("id");
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
     * @param nombre
     * @return
     */
    public int crearTema(String nombre) {
        try (Connection conexion = dbConnection.getConnection();
                PreparedStatement ps = conexion.prepareStatement(SQL_INSERT_TEMA,
                        Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nombre);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next())
                    return rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
        return -1;
    }

    /**
     * Obtiene o crea un tema
     * 
     * @param nombre
     * @return
     */
    public int obtenerOCrear(String nombre) {
        int id = obtenerIdPorNombre(nombre);
        if (id != -1)
            return id;
        return crearTema(nombre);
    }

    /**
     * Variante que usa una Connection existente
     */
    public int obtenerOCrear(Connection conexion, String nombre) {
        try (PreparedStatement ps = conexion.prepareStatement(SQL_SELECT_TEMA_ID_POR_NOMBRE)) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return rs.getInt("id");
            }
            try (PreparedStatement ins = conexion.prepareStatement(SQL_INSERT_TEMA,
                    Statement.RETURN_GENERATED_KEYS)) {
                ins.setString(1, nombre);
                ins.executeUpdate();
                try (ResultSet rs2 = ins.getGeneratedKeys()) {
                    if (rs2.next())
                        return rs2.getInt(1);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
        return -1;
    }
}
