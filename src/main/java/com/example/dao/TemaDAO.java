package com.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.example.conexiones.MySQLConnection;

/**
 * DAO para la tabla `temas`.
 */
public class TemaDAO {

    public int obtenerIdPorNombre(String nombre) {
        try (Connection conexion = new MySQLConnection().getConnection();
                PreparedStatement ps = conexion.prepareStatement("SELECT id FROM temas WHERE nombre = ?")) {
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

    public int crearTema(String nombre) {
        try (Connection conexion = new MySQLConnection().getConnection();
                PreparedStatement ps = conexion.prepareStatement("INSERT INTO temas (nombre) VALUES (?)", java.sql.Statement.RETURN_GENERATED_KEYS)) {
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
        try (PreparedStatement ps = conexion.prepareStatement("SELECT id FROM temas WHERE nombre = ?")) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return rs.getInt("id");
            }
            try (PreparedStatement ins = conexion.prepareStatement("INSERT INTO temas (nombre) VALUES (?)", java.sql.Statement.RETURN_GENERATED_KEYS)) {
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
