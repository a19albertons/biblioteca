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
        // Consulta SQL: obtenemos también el número de préstamos activos por ejemplar
        String sql = "SELECT e.id, e.num_ejemplar, e.fecha_adquisicion, e.estado AS en_servicio, "
                + "(SELECT COUNT(*) FROM prestamos p WHERE p.id_ejemplar = e.id AND p.estado = TRUE) AS prestamos_activos "
                + "FROM ejemplares e WHERE e.id_publicacion = ? ORDER BY e.num_ejemplar ASC";
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
                    boolean enServicio = rs.getBoolean("en_servicio");
                    int prestamosActivos = rs.getInt("prestamos_activos");

                    String estado;
                    if (!enServicio) {
                        estado = "BAJA"; // fuera de servicio
                    } else if (prestamosActivos > 0) {
                        estado = "PRESTADO"; // en préstamo activo
                    } else {
                        estado = "DISPONIBLE"; // disponible y en servicio
                    }

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
