package com.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
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
                    Date fecha = rs.getDate("fecha_adquisicion");
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

    /**
     * Devuelve el siguiente número de ejemplar para una publicación dada (1..n).
     *
     * @param conexion
     * @param idPublicacion
     * @return siguiente num_ejemplar o -1 en caso de error
     */
    public int siguienteNumEjemplar(Connection conexion, int idPublicacion) {
        // Consulta SQL
        String sql = "SELECT COALESCE(MAX(num_ejemplar),0) + 1 AS siguiente FROM ejemplares WHERE id_publicacion = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idPublicacion);
            // Ejecutar consulta
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("siguiente");
                }
            }
        } catch (Exception e) {
            System.out.println("Error al obtener siguiente num_ejemplar: " + e.getMessage());
            System.out.println(e.getCause());
        }
        return -1;
    }

    /**
     * Inserta un nuevo ejemplar para una publicación (usa la Connection
     * proporcionada).
     *
     * @param conexion
     * @param idPublicacion
     * @param numEjemplar
     * @param fechaAdquisicion (java.sql.Date)
     * @return true si la inserción fue exitosa
     */
    public boolean insertarEjemplar(Connection conexion, int idPublicacion, int numEjemplar, Date fechaAdquisicion) {
        // Consulta SQL
        String sql = "INSERT INTO ejemplares (id_publicacion, num_ejemplar, fecha_adquisicion, estado) VALUES (?, ?, ?, TRUE)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            // Asignar parámetros
            ps.setInt(1, idPublicacion);
            ps.setInt(2, numEjemplar);
            ps.setDate(3, fechaAdquisicion);
            // Ejecutar inserción
            int rows = ps.executeUpdate();
            return rows == 1;
        } catch (Exception e) {
            // Error
            System.out.println("Error insertando ejemplar: " + e.getMessage());
            System.out.println(e.getCause());
            return false;
        }
    }

    /**
     * Obtiene los detalles de un ejemplar por su id.
     *
     * @param idEjemplar
     * @return arreglo con {id, id_publicacion, num_ejemplar, fecha_adquisicion,
     *         estado} o null
     */
    public String[] obtenerEjemplarPorId(int idEjemplar) {
        // Consulta SQL
        String sql = "SELECT id, id_publicacion, num_ejemplar, fecha_adquisicion, estado FROM ejemplares WHERE id = ?";
        try (Connection conexion = new MySQLConnection().getConnection();
                PreparedStatement ps = conexion.prepareStatement(sql)) {
            // Asignar parámetro
            ps.setInt(1, idEjemplar);
            // Ejecutar consulta
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String id = String.valueOf(rs.getInt("id"));
                    String idPub = String.valueOf(rs.getInt("id_publicacion"));
                    String num = String.valueOf(rs.getInt("num_ejemplar"));
                    Date fecha = rs.getDate("fecha_adquisicion");
                    String fechaStr = (fecha != null) ? fecha.toString() : "";
                    boolean enServicio = rs.getBoolean("estado");
                    String estadoStr = enServicio ? "DISPONIBLE" : "BAJA";
                    return new String[] { id, idPub, num, fechaStr, estadoStr };
                }
            }
        } catch (Exception e) {
            // Error
            System.out.println("Error obteniendo ejemplar: " + e.getMessage());
            System.out.println(e.getCause());
        }
        return null;
    }

    /**
     * Actualiza los campos editables de un ejemplar (fecha_adquisicion y estado)
     * usando la Connection proporcionada.
     *
     * @param conexion
     * @param idEjemplar
     * @param fechaAdquisicion (java.sql.Date)
     * @param estado           estado (true = activo/en servicio, false = baja)
     * @return true si la actualización afectó exactamente una fila
     */
    public boolean actualizarEjemplar(Connection conexion, int idEjemplar, Date fechaAdquisicion, boolean estado) {
        // Consulta SQL
        String sql = "UPDATE ejemplares SET fecha_adquisicion = ?, estado = ? WHERE id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            // Asignar parámetros
            ps.setDate(1, fechaAdquisicion);
            ps.setBoolean(2, estado);
            ps.setInt(3, idEjemplar);
            // Ejecutar actualización
            int rows = ps.executeUpdate();
            return rows == 1;
        } catch (Exception e) {
            // Error
            System.out.println("Error actualizando ejemplar: " + e.getMessage());
            System.out.println(e.getCause());
            return false;
        }
    }
}
