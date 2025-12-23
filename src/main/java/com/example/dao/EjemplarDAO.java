package com.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.example.conexiones.MySQLConnection;

/**
 * DAO para la tabla ejemplares
 */
public class EjemplarDAO {

    /**
     * Lista los ejemplares de una publicación
     *
     * @param idPublicacion
     * @return String[][] con columnas: id, num_ejemplar, fecha_adquisicion, estado
     */
    public String[][] listaEjemplaresPorPublicacion(int idPublicacion) {
        List<String[]> lista = new ArrayList<>();
        // Consulta SQL
        String sql = "SELECT id, num_ejemplar, fecha_adquisicion, estado FROM ejemplares WHERE id_publicacion = ? ORDER BY num_ejemplar ASC";
        try (Connection conexion = new MySQLConnection().getConnection();
                PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idPublicacion);
            // Ejecutar consulta
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String id = String.valueOf(rs.getInt("id"));
                    String num = String.valueOf(rs.getInt("num_ejemplar"));
                    java.sql.Date fecha = rs.getDate("fecha_adquisicion");
                    String fechaStr = (fecha != null) ? fecha.toString() : "";
                    String estado = rs.getBoolean("estado") ? "DISPONIBLE" : "BAJA";
                    lista.add(new String[] { id, num, fechaStr, estado });
                }
            }
        } catch (Exception e) {
            lista = new ArrayList<>();
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
        return lista.toArray(new String[0][0]);
    }
}
