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

}
