package com.example.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

import com.example.conexiones.MySQLConnection;

/**
 * DAO para la gestión de préstamos
 */
public class PrestamoDAO {
    /**
     * Obtiene el número de préstamos realizados hoy
     * 
     * @return
     */
    public String prestamosHoy() {
        // Obtener la fecha actual
        LocalDate hoy = LocalDate.now();
        String totalPrestamos = "-1";
        // Consulta SQL para contar los préstamos de hoy
        try (Connection connection = new MySQLConnection().getConnection();
                PreparedStatement ps = connection.prepareStatement(
                        "SELECT COUNT(*) AS total FROM prestamos WHERE DATE(fecha_inicio) = ?")) {
            ps.setDate(1, Date.valueOf(hoy));
            // Obtiene el resultado y cierra el result set
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    totalPrestamos = rs.getString("total");
                }
            }
        } catch (Exception e) {
            // Manejo de excepciones. Se ve en consola
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            totalPrestamos = "-1";
        }
        return totalPrestamos;
    }

    /**
     * Obtiene el número de préstamos pendientes
     * 
     * @return
     */
    public String prestamosPendientes() {
        LocalDate hoy = LocalDate.now();
        String totalPendientes = "-1";
        // Consulta SQL para contar los préstamos pendientes
        try (Connection connection = new MySQLConnection().getConnection();
                PreparedStatement ps = connection.prepareStatement(
                        "SELECT COUNT(*) AS TOTAL FROM prestamos WHERE ? > fecha_fin AND estado = false")) {
            ps.setDate(1, Date.valueOf(hoy));
            // Obtiene el resultado y cierra el result set
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    totalPendientes = rs.getString("TOTAL");
                }
            }
        } catch (Exception e) {
            // Manejo de excepciones. Se ve en consola
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            totalPendientes = "-1";
        }
        return totalPendientes;
    }

    // Ultimos movimientos de prestamos (Estado prestamos, id ejemplar, titulo publicacion)
    public String[][] ultimosMovimientos() {
        String[][] movimientos = new String[9][3];
        // Consulta SQL para obtener los últimos 9 movimientos de préstamos
        try (Connection connection = new MySQLConnection().getConnection();
                PreparedStatement ps = connection.prepareStatement(
                        "SELECT p.estado, e.id AS id_ejemplar, pub.titulo " +
                                "FROM prestamos p " +
                                "JOIN ejemplares e ON p.id_ejemplar = e.id " +
                                "JOIN publicaciones pub ON e.id_publicacion = pub.id " +
                                "ORDER BY p.fecha_inicio DESC " +
                                "LIMIT 9")) {
            // Obtiene el resultado y cierra el result set
            try (ResultSet rs = ps.executeQuery()) {
                int index = 0;
                while (rs.next() && index < 9) {
                    movimientos[index][0] = String.valueOf(rs.getInt("id_ejemplar"));
                    movimientos[index][1] = rs.getString("titulo");
                    movimientos[index][2] = rs.getBoolean("estado") ? "Devuelto" : "Prestado";
                    
                    index++;
                }
            }
        } catch (Exception e) {
            // Manejo de excepciones. Se ve en consola
            movimientos = new String[0][0];
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
        return movimientos;
    }



}
