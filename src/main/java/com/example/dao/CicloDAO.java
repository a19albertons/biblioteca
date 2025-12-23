package com.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.example.conexiones.MySQLConnection;

/**
 * DAO para Ciclo
 */
public class CicloDAO {
    /**
     * Obtiene los nombres de los ciclos
     * 
     * @return
     */
    public String[] listaCiclos() {
        // Listado de ciclos
        ArrayList<String> devolver = new ArrayList<>();
        try (Connection conexion = new MySQLConnection().getConnection();
                // Consulta SQL
                PreparedStatement ps = conexion.prepareStatement("SELECT nombre FROM ciclos ORDER BY nombre ASC");) {
            // Ejecutar consulta
            try (ResultSet rs = ps.executeQuery();) {
                while (rs.next()) {
                    devolver.add(rs.getString("nombre"));
                }
            }
        } catch (Exception e) {
            devolver = new ArrayList<>();
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
        return devolver.toArray(new String[0]);
    }

    /**
     * Obtiene el id de un ciclo por nombre o lo crea si no existe
     *
     * @param nombre nombre del ciclo
     * @return id del ciclo o -1 en caso de error
     */
    public int obtenerOCrear(String nombre) {
        try (Connection conexion = new MySQLConnection().getConnection();
                PreparedStatement ps = conexion.prepareStatement("SELECT id FROM ciclos WHERE nombre = ?")) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
            // Si no existe, insertamos
            try (PreparedStatement ins = conexion.prepareStatement("INSERT INTO ciclos (nombre) VALUES (?)", java.sql.Statement.RETURN_GENERATED_KEYS)) {
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
        return -1;
    }

    /**
     * Variante que usa una Connection existente (transacciones)
     */
    public int obtenerOCrear(Connection conexion, String nombre) {
        try (PreparedStatement ps = conexion.prepareStatement("SELECT id FROM ciclos WHERE nombre = ?")) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
            try (PreparedStatement ins = conexion.prepareStatement("INSERT INTO ciclos (nombre) VALUES (?)", java.sql.Statement.RETURN_GENERATED_KEYS)) {
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
        return -1;
    }

}
