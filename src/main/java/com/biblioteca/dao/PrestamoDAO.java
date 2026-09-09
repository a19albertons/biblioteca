package com.biblioteca.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import com.biblioteca.dto.ObtenerUltimoPrestamoPorEjemplarDTO;
import com.biblioteca.dto.RegistroDevolucionDTO;

/**
 * DAO para la gestión de préstamos
 */
public class PrestamoDAO {
    /**
     * Conexión a la base de datos para operaciones SQL.
     */
    private final Connection conexion;

    /**
     * Constante SQL para contar préstamos realizados hoy.
     */
    private static final String SQL_PRESTAMOS_HOY = "SELECT COUNT(*) AS total FROM prestamos WHERE DATE(fecha_inicio) = ?";

    /**
     * Constante SQL para contar préstamos pendientes.
     */
    private static final String SQL_PRESTAMOS_PENDIENTES = "SELECT COUNT(*) AS TOTAL FROM prestamos WHERE ? > fecha_fin AND estado = TRUE";

    /**
     * Constante SQL para obtener los últimos movimientos de préstamos.
     */
    private static final String SQL_ULTIMOS_MOVIMIENTOS = "SELECT p.estado, e.id AS id_ejemplar, pub.titulo FROM prestamos p JOIN ejemplares e ON p.id_ejemplar = e.id JOIN publicaciones pub ON e.id_publicacion = pub.id ORDER BY p.fecha_inicio DESC LIMIT 9";

    /**
     * Constante SQL para insertar un nuevo préstamo.
     */
    private static final String SQL_INSERT_PRESTAMO = "INSERT INTO prestamos (id_usuario, id_ejemplar, fecha_inicio, fecha_fin, estado) VALUES (?, ?, ?, ?, TRUE)";

    /**
     * Constante SQL para contar préstamos de un usuario por tipo de publicación.
     */
    private static final String SQL_CNT_PRESTAMO_TIPO = "SELECT COUNT(*) AS cnt FROM prestamos p JOIN ejemplares e ON p.id_ejemplar = e.id JOIN publicaciones pub ON e.id_publicacion = pub.id WHERE p.id_usuario = ? AND p.estado = TRUE AND pub.tipo = ?";

    /**
     * Constante SQL para comprobar préstamo activo entre usuario y ejemplar.
     */
    private static final String SQL_CNT_PRESTAMO_USUARIO_EJEMPLAR = "SELECT COUNT(*) AS cnt FROM prestamos WHERE id_usuario = ? AND id_ejemplar = ? AND estado = TRUE";

    /**
     * Constante SQL para actualizar el estado de un préstamo devuelto.
     */
    private static final String SQL_UPDATE_DEVOLVER = "UPDATE prestamos SET estado = FALSE WHERE id_usuario = ? AND id_ejemplar = ? AND estado = TRUE";

    /**
     * Constante SQL para obtener el préstamo activo de un usuario por ejemplar.
     */
    private static final String SQL_SELECT_PRESTAMO_ACTIVO_POR_USUARIO_EJEMPLAR = "SELECT id, fecha_inicio, fecha_fin FROM prestamos WHERE id_usuario = ? AND id_ejemplar = ? AND estado = TRUE";

    /**
     * Constante SQL para obtener el último préstamo por ejemplar.
     */
    private static final String SQL_SELECT_ULTIMO_PRESTAMO_POR_EJEMPLAR = "SELECT id, id_usuario, fecha_inicio, fecha_fin, estado FROM prestamos WHERE id_ejemplar = ? ORDER BY fecha_inicio DESC, id DESC LIMIT 1";

    /**
     * Constructor con DBConnection (inyección)
     * 
     * @param conexion
     */
    public PrestamoDAO(final Connection conexion) {
        if (conexion == null) {
            throw new IllegalArgumentException("Connection cannot be null");
        }
        this.conexion = conexion;
    }

    /**
     * Obtiene el número de préstamos realizados hoy.
     * 
     * @return número de préstamos realizados hoy como string
     */
    public String prestamosHoy() {
        // Obtener la fecha actual
        LocalDate hoy = LocalDate.now();
        String totalPrestamos = "-1";
        // Consulta SQL para contar los préstamos de hoy
        try (
                PreparedStatement ps = conexion.prepareStatement(SQL_PRESTAMOS_HOY)) {
            ps.setDate(1, Date.valueOf(hoy));
            // Obtiene el resultado y cierra el result set
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    totalPrestamos = rs.getString("total");
                }
            }
        } catch (SQLException e) {
            // Manejo de excepciones. Se ve en consola
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            totalPrestamos = "-1";
        }
        return totalPrestamos;
    }

    /**
     * Obtiene el número de préstamos pendientes.
     * 
     * @return número de préstamos pendientes como string
     */
    public String prestamosPendientes() {
        LocalDate hoy = LocalDate.now();
        String totalPendientes = "-1";
        // Consulta SQL para contar los préstamos pendientes
        try (
                PreparedStatement ps = conexion.prepareStatement(SQL_PRESTAMOS_PENDIENTES)) {
            ps.setDate(1, Date.valueOf(hoy));
            // Obtiene el resultado y cierra el result set
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    totalPendientes = rs.getString("TOTAL");
                }
            }
        } catch (SQLException e) {
            // Manejo de excepciones. Se ve en consola
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            totalPendientes = "-1";
        }
        return totalPendientes;
    }

    /**
     * Obtiene los últimos 9 movimientos de préstamos.
     * 
     * @return arreglo con información de los últimos movimientos
     */
    public String[][] ultimosMovimientos() {
        String[][] movimientos = new String[9][3];
        // Consulta SQL para obtener los últimos 9 movimientos de préstamos
        try (
                PreparedStatement ps = conexion.prepareStatement(SQL_ULTIMOS_MOVIMIENTOS)) {
            // Obtiene el resultado y cierra el result set
            try (ResultSet rs = ps.executeQuery()) {
                int index = 0;
                while (rs.next() && index < 9) {
                    movimientos[index][0] = String.valueOf(rs.getInt("id_ejemplar"));
                    movimientos[index][1] = rs.getString("titulo");
                    // TRUE=sin devolver, FALSE=devuelto
                    movimientos[index][2] = rs.getBoolean("estado") ? "Prestado" : "Devuelto";

                    index++;
                }
            }
        } catch (SQLException e) {
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
     * @param idUsuario   ID del usuario que realiza el préstamo
     * @param idEjemplar  ID del ejemplar que se presta
     * @param fechaInicio fecha en la que comienza el préstamo
     * @param fechaFin    fecha en la que termina el préstamo
     * @return true si se insertó correctamente
     */
    public boolean insertarPrestamo(final int idUsuario, final int idEjemplar, final Date fechaInicio,
            final Date fechaFin) {
        // Consulta SQL para insertar el préstamo
        final String sql = SQL_INSERT_PRESTAMO;
        try (
                PreparedStatement ps = conexion.prepareStatement(sql)) {
            // Asignar parámetros
            ps.setInt(1, idUsuario);
            ps.setInt(2, idEjemplar);
            ps.setDate(3, fechaInicio);
            ps.setDate(4, fechaFin);

            // Ejecutar inserción
            int rows = ps.executeUpdate();
            return rows == 1;
        } catch (SQLException e) {
            // Manejo de excepciones. Se ve en consola
            System.out.println("Error insertando préstamo: " + e.getMessage());
            System.out.println(e.getCause());
            return false;
        }
    }

    /**
     * Comprueba si el usuario tiene un préstamo activo de un tipo de publicación
     * (L = libro, R = revista).
     *
     * @param idUsuario       ID del usuario
     * @param tipoPublicacion tipo de publicación ('L' para libro o 'R' para
     *                        revista)
     * @return true si existe al menos un préstamo activo de ese tipo
     */
    public boolean tienePrestamoActivoTipo(final int idUsuario, final char tipoPublicacion) {
        // Consulta SQL para comprobar préstamos activos por tipo
        final String sql = SQL_CNT_PRESTAMO_TIPO;
        try (
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
        } catch (SQLException e) {
            // Manejo de excepciones. Se ve en consola
            System.out.println("Error comprobando préstamo activo por tipo: " + e.getMessage());
            System.out.println(e.getCause());
        }
        return false;
    }

    /**
     * Comprueba si existe un préstamo activo entre un usuario y un ejemplar.
     *
     * @param idUsuario  ID del usuario
     * @param idEjemplar ID del ejemplar
     * @return true si existe un préstamo activo
     */
    public boolean existePrestamoActivoUsuarioEjemplar(final int idUsuario, final int idEjemplar) {
        // Consulta SQL para comprobar préstamo activo entre usuario y ejemplar
        final String sql = SQL_CNT_PRESTAMO_USUARIO_EJEMPLAR;
        try (
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
        } catch (SQLException e) {
            // Manejo de excepciones. Se ve en consola
            System.out.println("Error comprobando préstamo activo usuario-ejemplar: " + e.getMessage());
            System.out.println(e.getCause());
        }
        return false;
    }

    /**
     * Marca un préstamo activo como devuelto (estado = FALSE) para el usuario y
     * ejemplar.
     *
     * @param idUsuario  ID del usuario
     * @param idEjemplar ID del ejemplar
     * @return true si se actualizó exactamente una fila
     */
    public boolean devolverPrestamoUsuarioEjemplar(final int idUsuario, final int idEjemplar) {
        // Consulta SQL para actualizar el estado del préstamo
        final String sql = SQL_UPDATE_DEVOLVER;
        try (
                PreparedStatement ps = conexion.prepareStatement(sql)) {
            // Asignar parámetros
            ps.setInt(1, idUsuario);
            ps.setInt(2, idEjemplar);
            // Ejecutar actualización
            int rows = ps.executeUpdate();
            return rows == 1;
        } catch (SQLException e) {
            // Manejo de excepciones. Se ve en consola
            System.out.println("Error marcando devolución de préstamo: " + e.getMessage());
            System.out.println(e.getCause());
            return false;
        }
    }

    /**
     * Obtiene el préstamo activo (estado = TRUE) entre un usuario y un ejemplar.
     * Devuelve objeto con información del préstamo o null.
     *
     * @param idUsuario  ID del usuario
     * @param idEjemplar ID del ejemplar
     * @return préstamo activo o null
     */
    public RegistroDevolucionDTO obtenerPrestamoActivoPorUsuarioEjemplar(final int idUsuario, final int idEjemplar) {
        // Consulta SQL para obtener el préstamo activo
        final String sql = SQL_SELECT_PRESTAMO_ACTIVO_POR_USUARIO_EJEMPLAR;
        try (
                PreparedStatement ps = conexion.prepareStatement(sql)) {
            // Asignar parámetros
            ps.setInt(1, idUsuario);
            ps.setInt(2, idEjemplar);
            try (ResultSet rs = ps.executeQuery()) {
                // Ejecutar consulta
                if (rs.next()) {
                    // Construir y devolver el DTO con los datos del préstamo
                    int idPrestamo = rs.getInt("id");
                    LocalDate fechaInicio = rs.getDate("fecha_inicio").toLocalDate();
                    LocalDate fechaFin = rs.getDate("fecha_fin").toLocalDate();
                    return new RegistroDevolucionDTO(idPrestamo, fechaInicio, fechaFin);
                }
            }
        } catch (SQLException e) {
            // Manejo de excepciones. Se ve en consola
            System.out.println("Error obteniendo préstamo activo: " + e.getMessage());
            System.out.println(e.getCause());
        }
        return null;
    }

    /**
     * Obtiene el último préstamo asociado a un ejemplar (independientemente de
     * estado).
     * Devuelve objeto con información del préstamo o null.
     *
     * @param idEjemplar ID del ejemplar
     * @return último préstamo asociado al ejemplar o null
     */
    public ObtenerUltimoPrestamoPorEjemplarDTO obtenerUltimoPrestamoPorEjemplar(final int idEjemplar) {
        // Consulta SQL para obtener el último préstamo por ejemplar
        final String sql = SQL_SELECT_ULTIMO_PRESTAMO_POR_EJEMPLAR;
        try (
                PreparedStatement ps = conexion.prepareStatement(sql)) {
            // Asignar parámetro
            ps.setInt(1, idEjemplar);
            try (ResultSet rs = ps.executeQuery()) {
                // Ejecutar consulta
                if (rs.next()) {
                    int id = rs.getInt("id");
                    int idUsuario = rs.getInt("id_usuario");
                    Date fechaInicio = rs.getDate("fecha_inicio");
                    Date fechaFin = rs.getDate("fecha_fin");
                    boolean estado = rs.getBoolean("estado");
                    return new ObtenerUltimoPrestamoPorEjemplarDTO(id, idUsuario, fechaInicio, fechaFin, estado);
                }
            }
        } catch (SQLException e) {
            // Manejo de excepciones. Se ve en consola
            System.out.println("Error obteniendo ultimo prestamo por ejemplar: " + e.getMessage());
            System.out.println(e.getCause());
        }
        return null;
    }

}
