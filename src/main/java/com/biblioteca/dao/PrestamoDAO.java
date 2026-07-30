package com.biblioteca.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

import com.biblioteca.conexiones.DBConnection;

/**
 * DAO para la gestión de préstamos
 */
public class PrestamoDAO {
    /**
     * DBConnection para la base de datos
     */
    private final DBConnection dbConnection;

    // SQL constants 🔧
    private static final String SQL_PRESTAMOS_HOY = "SELECT COUNT(*) AS total FROM prestamos WHERE DATE(fecha_inicio) = ?";
    private static final String SQL_PRESTAMOS_PENDIENTES = "SELECT COUNT(*) AS TOTAL FROM prestamos WHERE ? > fecha_fin AND estado = TRUE";
    private static final String SQL_ULTIMOS_MOVIMIENTOS = "SELECT p.estado, e.id AS id_ejemplar, pub.titulo FROM prestamos p JOIN ejemplares e ON p.id_ejemplar = e.id JOIN publicaciones pub ON e.id_publicacion = pub.id ORDER BY p.fecha_inicio DESC LIMIT 9";
    private static final String SQL_INSERT_PRESTAMO = "INSERT INTO prestamos (id_usuario, id_ejemplar, fecha_inicio, fecha_fin, estado) VALUES (?, ?, ?, ?, TRUE)";
    private static final String SQL_CNT_PRESTAMO_TIPO = "SELECT COUNT(*) AS cnt FROM prestamos p JOIN ejemplares e ON p.id_ejemplar = e.id JOIN publicaciones pub ON e.id_publicacion = pub.id WHERE p.id_usuario = ? AND p.estado = TRUE AND pub.tipo = ?";
    private static final String SQL_CNT_PRESTAMO_USUARIO_EJEMPLAR = "SELECT COUNT(*) AS cnt FROM prestamos WHERE id_usuario = ? AND id_ejemplar = ? AND estado = TRUE";
    private static final String SQL_UPDATE_DEVOLVER = "UPDATE prestamos SET estado = FALSE WHERE id_usuario = ? AND id_ejemplar = ? AND estado = TRUE";
    private static final String SQL_SELECT_PRESTAMO_ACTIVO_POR_USUARIO_EJEMPLAR = "SELECT id, fecha_inicio, fecha_fin FROM prestamos WHERE id_usuario = ? AND id_ejemplar = ? AND estado = TRUE";
    private static final String SQL_SELECT_ULTIMO_PRESTAMO_POR_EJEMPLAR = "SELECT id, id_usuario, fecha_inicio, fecha_fin, estado FROM prestamos WHERE id_ejemplar = ? ORDER BY fecha_inicio DESC, id DESC LIMIT 1";

    /**
     * Constructor con DBConnection (inyección)
     * 
     * @param dbConnection
     */
    public PrestamoDAO(DBConnection dbConnection) {
        if (dbConnection == null) {
            throw new IllegalArgumentException("DBConnection cannot be null");
        }
        this.dbConnection = dbConnection;
    }

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
        try (Connection connection = dbConnection.getConnection();
                PreparedStatement ps = connection.prepareStatement(SQL_PRESTAMOS_HOY)) {
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
        try (Connection connection = dbConnection.getConnection();
                PreparedStatement ps = connection.prepareStatement(SQL_PRESTAMOS_PENDIENTES)) {
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

    /**
     * Obtiene los últimos 9 movimientos de préstamos
     * @return
     */
    public String[][] ultimosMovimientos() {
        String[][] movimientos = new String[9][3];
        // Consulta SQL para obtener los últimos 9 movimientos de préstamos
        try (Connection connection = dbConnection.getConnection();
                PreparedStatement ps = connection.prepareStatement(SQL_ULTIMOS_MOVIMIENTOS)) {
            // Obtiene el resultado y cierra el result set
            try (ResultSet rs = ps.executeQuery()) {
                int index = 0;
                while (rs.next() && index < 9) {
                    movimientos[index][0] = String.valueOf(rs.getInt("id_ejemplar"));
                    movimientos[index][1] = rs.getString("titulo");
                    //  TRUE=sin devolver, FALSE=devuelto 
                    movimientos[index][2] = rs.getBoolean("estado") ? "Prestado" : "Devuelto";

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

    /**
     * Inserta un nuevo préstamo y devuelve true si fue insertado correctamente.
     *
     * @param idUsuario
     * @param idEjemplar
     * @param fechaInicio (Date)
     * @param fechaFin    (Date)
     * @return true si se insertó correctamente
     */
    public boolean insertarPrestamo(int idUsuario, int idEjemplar, Date fechaInicio, Date fechaFin) {
        // Consulta SQL para insertar el préstamo
        final String sql = SQL_INSERT_PRESTAMO;
        try (Connection conexion = dbConnection.getConnection();
                PreparedStatement ps = conexion.prepareStatement(sql)) {
            // Asignar parámetros
            ps.setInt(1, idUsuario);
            ps.setInt(2, idEjemplar);
            ps.setDate(3, fechaInicio);
            ps.setDate(4, fechaFin);

            // Ejecutar inserción
            int rows = ps.executeUpdate();
            return rows == 1;
        } catch (Exception e) {
            // Manejo de excepciones. Se ve en consola
            System.out.println("Error insertando préstamo: " + e.getMessage());
            System.out.println(e.getCause());
            return false;
        }
    }

    /**
     * Comprueba si el usuario tiene un préstamo activo de un tipo de publicación
     * (L = libro, R = revista)
     *
     * @param idUsuario
     * @param tipoPublicacion (char 'L' o 'R')
     * @return true si existe al menos un préstamo activo de ese tipo
     */
    public boolean tienePrestamoActivoTipo(int idUsuario, char tipoPublicacion) {
        // Consulta SQL para comprobar préstamos activos por tipo
        final String sql = SQL_CNT_PRESTAMO_TIPO;
        try (Connection conexion = dbConnection.getConnection();
                PreparedStatement ps = conexion.prepareStatement(sql)) {
            // Asignar parámetros
            ps.setInt(1, idUsuario);
            ps.setString(2, String.valueOf(Character.toUpperCase(tipoPublicacion)));
            try (ResultSet rs = ps.executeQuery()) {
                // Ejecutar consulta
                if (rs.next()) {
                    return rs.getInt("cnt") > 0;
                }
            }
        } catch (Exception e) {
            // Manejo de excepciones. Se ve en consola
            System.out.println("Error comprobando préstamo activo por tipo: " + e.getMessage());
            System.out.println(e.getCause());
        }
        return false;
    }

    /**
     * Comprueba si existe un préstamo activo entre un usuario y un ejemplar
     *
     * @param idUsuario
     * @param idEjemplar
     * @return true si existe un préstamo activo
     */
    public boolean existePrestamoActivoUsuarioEjemplar(int idUsuario, int idEjemplar) {
        // Consulta SQL para comprobar préstamo activo entre usuario y ejemplar
        final String sql = SQL_CNT_PRESTAMO_USUARIO_EJEMPLAR;
        try (Connection conexion = dbConnection.getConnection();
                PreparedStatement ps = conexion.prepareStatement(sql)) {
            // Asignar parámetros
            ps.setInt(1, idUsuario);
            ps.setInt(2, idEjemplar);
            // Ejecutar consulta
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cnt") > 0;
                }
            }
        } catch (Exception e) {
            // Manejo de excepciones. Se ve en consola
            System.out.println("Error comprobando préstamo activo usuario-ejemplar: " + e.getMessage());
            System.out.println(e.getCause());
        }
        return false;
    }

    /**
     * Marca un préstamo activo como devuelto (estado = FALSE) para el usuario y
     * ejemplar
     *
     * @param idUsuario
     * @param idEjemplar
     * @return true si se actualizó exactamente una fila
     */
    public boolean devolverPrestamoUsuarioEjemplar(int idUsuario, int idEjemplar) {
        // Consulta SQL para actualizar el estado del préstamo
        final String sql = SQL_UPDATE_DEVOLVER;
        try (Connection conexion = dbConnection.getConnection();
                PreparedStatement ps = conexion.prepareStatement(sql)) {
            // Asignar parámetros
            ps.setInt(1, idUsuario);
            ps.setInt(2, idEjemplar);
            // Ejecutar actualización
            int rows = ps.executeUpdate();
            return rows == 1;
        } catch (Exception e) {
            // Manejo de excepciones. Se ve en consola
            System.out.println("Error marcando devolución de préstamo: " + e.getMessage());
            System.out.println(e.getCause());
            return false;
        }
    }

    /**
     * Obtiene el préstamo activo (estado = TRUE) entre un usuario y un ejemplar
     * Devuelve arreglo {idPrestamo, fecha_inicio, fecha_fin} o null
     */
    public String[] obtenerPrestamoActivoPorUsuarioEjemplar(int idUsuario, int idEjemplar) {
        // Consulta SQL para obtener el préstamo activo
        final String sql = SQL_SELECT_PRESTAMO_ACTIVO_POR_USUARIO_EJEMPLAR;
        try (Connection conexion = dbConnection.getConnection();
                PreparedStatement ps = conexion.prepareStatement(sql)) {
            // Asignar parámetros
            ps.setInt(1, idUsuario);
            ps.setInt(2, idEjemplar);
            try (ResultSet rs = ps.executeQuery()) {
                // Ejecutar consulta
                if (rs.next()) {
                    // Construir y devolver el arreglo con los datos del préstamo
                    String idPrestamo = String.valueOf(rs.getInt("id"));
                    java.sql.Date fechaInicio = rs.getDate("fecha_inicio");
                    java.sql.Date fechaFin = rs.getDate("fecha_fin");
                    String fechaInicioStr = fechaInicio != null ? fechaInicio.toString() : "";
                    String fechaFinStr = fechaFin != null ? fechaFin.toString() : "";
                    return new String[] { idPrestamo, fechaInicioStr, fechaFinStr };
                }
            }
        } catch (Exception e) {
            // Manejo de excepciones. Se ve en consola
            System.out.println("Error obteniendo préstamo activo: " + e.getMessage());
            System.out.println(e.getCause());
        }
        return null;
    }

    /**
     * Obtiene el ultimo préstamo asociado a un ejemplar (independientemente de
     * estado)
     * Devuelve: idPrestamo, idUsuario, fecha_inicio, fecha_fin, estado
     */
    public String[] obtenerUltimoPrestamoPorEjemplar(int idEjemplar) {
        // Consulta SQL para obtener el último préstamo por ejemplar
        final String sql = SQL_SELECT_ULTIMO_PRESTAMO_POR_EJEMPLAR;
        try (Connection conexion = dbConnection.getConnection();
                PreparedStatement ps = conexion.prepareStatement(sql)) {
            // Asignar parámetro
            ps.setInt(1, idEjemplar);
            try (ResultSet rs = ps.executeQuery()) {
                // Ejecutar consulta
                if (rs.next()) {
                    String id = String.valueOf(rs.getInt("id"));
                    String idUsuario = String.valueOf(rs.getInt("id_usuario"));
                    java.sql.Date fi = rs.getDate("fecha_inicio");
                    java.sql.Date ff = rs.getDate("fecha_fin");
                    String estado = String.valueOf(rs.getBoolean("estado"));
                    return new String[] { id, idUsuario, fi != null ? fi.toString() : "",
                            ff != null ? ff.toString() : "", estado };
                }
            }
        } catch (Exception e) {
            // Manejo de excepciones. Se ve en consola
            System.out.println("Error obteniendo ultimo prestamo por ejemplar: " + e.getMessage());
            System.out.println(e.getCause());
        }
        return null;
    }

}
