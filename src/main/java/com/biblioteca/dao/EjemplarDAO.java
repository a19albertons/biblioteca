package com.biblioteca.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.biblioteca.dto.EjemplarTipoPublicacionDTO;
import com.biblioteca.dto.EstadoEjemplarDTO;
import com.biblioteca.modelo.TipoPublicacion;

/**
 * DAO para la tabla ejemplares
 */
public class EjemplarDAO {
    /**
     * Conexión para la base de datos.
     */
    private final Connection conexion;

    /** Constante SQL para listar ejemplares por publicación */
    private static final String SQL_LISTA_EJEMPLARES_POR_PUBLICACION = "SELECT e.id, e.num_ejemplar, e.fecha_adquisicion, e.estado AS en_servicio, "
            + "(SELECT COUNT(*) FROM prestamos p WHERE p.id_ejemplar = e.id AND p.estado = TRUE) AS prestamos_activos "
            + "FROM ejemplares e WHERE e.id_publicacion = ? ORDER BY e.num_ejemplar ASC";
    /** Constante SQL para obtener el siguiente número de ejemplar */
    private static final String SQL_SELECT_SIGUIENTE_NUM_EJEMPLAR = "SELECT COALESCE(MAX(num_ejemplar),0) + 1 AS siguiente FROM ejemplares WHERE id_publicacion = ?";
    /** Constante SQL para insertar un ejemplar */
    private static final String SQL_INSERT_EJEMPLAR = "INSERT INTO ejemplares (id_publicacion, num_ejemplar, fecha_adquisicion, estado) VALUES (?, ?, ?, TRUE)";
    /**
     * Constante SQL para seleccionar id, idpublicacion, num_ejemplar,
     * fecha_adquisicion y estado de un ejemplar por ID
     */
    private static final String SQL_SELECT_EJEMPLAR_POR_ID = "SELECT id, id_publicacion, num_ejemplar, fecha_adquisicion, estado FROM ejemplares WHERE id = ?";
    /** Constante SQL para actualizar un ejemplar */
    private static final String SQL_UPDATE_EJEMPLAR = "UPDATE ejemplares SET fecha_adquisicion = ?, estado = ? WHERE id = ?";
    /** Constante SQL para contar los préstamos activos de un ejemplar */
    private static final String SQL_CNT_PRESTAMOS_POR_EJEMPLAR = "SELECT COUNT(*) AS cnt FROM prestamos WHERE id_ejemplar = ? AND estado = TRUE";
    /** Constante SQL para dar de baja un ejemplar */
    private static final String SQL_UPDATE_BAJA_EJEMPLAR = "UPDATE ejemplares SET estado = FALSE WHERE id = ?";
    /** Constante SQL para obtener un ejemplar con su tipo de publicación */
    private static final String SQL_OBTENER_EJEMPLAR_TIPO_PUBLICACION_DTO = "SELECT e.num_ejemplar, p.tipo FROM ejemplares e JOIN publicaciones p ON e.id_publicacion = p.id WHERE e.id = ?";

    /**
     * Constructor del DAO.
     * 
     * @param conexion la conexión a la base de datos
     */
    public EjemplarDAO(final Connection conexion) {
        if (conexion == null) {
            throw new IllegalArgumentException("DBConnection cannot be null");
        }
        this.conexion = conexion;
    }

    /**
     * Lista los ejemplares de una publicación
     *
     * @param idPublicacion el ID de la publicación
     * @return String[][] con columnas: id, num_ejemplar, fecha_adquisicion, estado
     */
    public String[][] listaEjemplaresPorPublicacion(final int idPublicacion) {
        List<String[]> lista = new ArrayList<>();
        // Consulta SQL: obtenemos también el número de préstamos activos por ejemplar
        final String sql = SQL_LISTA_EJEMPLARES_POR_PUBLICACION;
        try (
                PreparedStatement ps = conexion.prepareStatement(sql)) {
            // establecer parámetro
            ps.setInt(1, idPublicacion);
            // Ejecutar consulta
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // obtener datos
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
        } catch (SQLException e) {
            lista = new ArrayList<>();
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
        return lista.toArray(new String[0][0]);
    }

    /**
     * Devuelve el siguiente número de ejemplar para una publicación dada (1..n).
     *
     * @param idPublicacion el ID de la publicación
     * @return el siguiente num_ejemplar o -1 en caso de error
     */
    public int siguienteNumEjemplar(final int idPublicacion) {
        // Consulta SQL
        final String sql = SQL_SELECT_SIGUIENTE_NUM_EJEMPLAR;
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idPublicacion);
            // Ejecutar consulta
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("siguiente");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener siguiente num_ejemplar: " + e.getMessage());
            System.out.println(e.getCause());
        }
        return -1;
    }

    /**
     * Inserta un nuevo ejemplar para una publicación (usa la Connection
     * proporcionada).
     *
     * @param idPublicacion    el ID de la publicación
     * @param numEjemplar      el número de ejemplar
     * @param fechaAdquisicion la fecha de adquisición
     * @return true si la inserción fue exitosa
     */
    public boolean insertarEjemplar(final int idPublicacion, final int numEjemplar, final Date fechaAdquisicion) {
        // Consulta SQL
        final String sql = SQL_INSERT_EJEMPLAR;
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            // Asignar parámetros
            ps.setInt(1, idPublicacion);
            ps.setInt(2, numEjemplar);
            ps.setDate(3, fechaAdquisicion);
            // Ejecutar inserción
            int rows = ps.executeUpdate();
            return rows == 1;
        } catch (SQLException e) {
            // Error
            System.out.println("Error insertando ejemplar: " + e.getMessage());
            System.out.println(e.getCause());
            return false;
        }
    }

    /**
     * Obtiene los detalles de un ejemplar por su id.
     *
     * @param idEjemplar el ID del ejemplar
     * @return un objeto EstadoEjemplarDTO con los detalles del ejemplar o null
     */
    public EstadoEjemplarDTO obtenerEjemplarPorId(final int idEjemplar) {
        // Consulta SQL
        final String sql = SQL_SELECT_EJEMPLAR_POR_ID;
        try (
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
                    return new EstadoEjemplarDTO(
                            Integer.parseInt(id),
                            Integer.parseInt(idPub),
                            Integer.parseInt(num),
                            fechaStr, estadoStr);
                }
            }
        } catch (SQLException e) {
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
     * @param idEjemplar       el ID del ejemplar
     * @param fechaAdquisicion la fecha de adquisición
     * @param estado           el estado del ejemplar (true = activo/en servicio,
     *                         false = baja)
     * @return true si la actualización afectó exactamente una fila
     */
    public boolean actualizarEjemplar(final int idEjemplar, final Date fechaAdquisicion, final boolean estado) {
        // Consulta SQL
        final String sql = SQL_UPDATE_EJEMPLAR;
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            // Asignar parámetros
            ps.setDate(1, fechaAdquisicion);
            ps.setBoolean(2, estado);
            ps.setInt(3, idEjemplar);
            // Ejecutar actualización
            int rows = ps.executeUpdate();
            return rows == 1;
        } catch (SQLException e) {
            // Error
            System.out.println("Error actualizando ejemplar: " + e.getMessage());
            System.out.println(e.getCause());
            return false;
        }
    }

    /**
     * Comprueba si un ejemplar tiene préstamos activos (estado TRUE).
     *
     * @param idEjemplar el ID del ejemplar
     * @return true si existen préstamos activos
     */
    public boolean tienePrestamosActivosEjemplar(final int idEjemplar) {
        // Consulta SQL
        final String sql = SQL_CNT_PRESTAMOS_POR_EJEMPLAR;
        try (
                PreparedStatement ps = conexion.prepareStatement(sql)) {
            // Asignar parámetro
            ps.setInt(1, idEjemplar);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cnt") > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error comprobando préstamos activos: " + e.getMessage());
            System.out.println(e.getCause());
        }
        return false;
    }

    /**
     * Marca un ejemplar como baja (estado = FALSE) dentro de la transacción.
     *
     * @param idEjemplar el ID del ejemplar
     * @return true si la actualización afectó exactamente una fila
     */
    public boolean bajaEjemplar(final int idEjemplar) {
        // Consulta SQLSQL_OBTENER_EJEMPLAR_PUBLICACION_DTO
        final String sql = SQL_UPDATE_BAJA_EJEMPLAR;
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            // Asignar parámetro
            ps.setInt(1, idEjemplar);
            // Ejecutar actualización
            int rows = ps.executeUpdate();
            return rows == 1;
        } catch (SQLException e) {
            System.out.println("Error marcando ejemplar como baja: " + e.getMessage());
            System.out.println(e.getCause());
            return false;
        }
    }

    /**
     * Extension method that returns the ejemplar type and publication DTO.
     *
     * @param idEjemplar the ejemplar ID
     * @return EjemplarTipoPublicacionDTO with ejemplar number and publication type
     */
    public final EjemplarTipoPublicacionDTO obtenerEjemplarPublicacionDTO(final int idEjemplar) {
        EjemplarTipoPublicacionDTO dto = null;
        // Consulta SQL
        try (
                // PreparedStatement para obtener el DTO
                PreparedStatement ps = conexion.prepareStatement(SQL_OBTENER_EJEMPLAR_TIPO_PUBLICACION_DTO)) {
            // Asignar parámetro
            ps.setInt(1, idEjemplar);
            try (ResultSet rs = ps.executeQuery()) {
                // Si hay resultado, crear el DTO
                if (rs.next()) {
                    int numEjemplar = rs.getInt("num_ejemplar");
                    String tipoPublicacionStr = rs.getString("tipo");
                    TipoPublicacion tipoPublicacion = TipoPublicacion.valueOf(tipoPublicacionStr);
                    dto = new EjemplarTipoPublicacionDTO(numEjemplar, tipoPublicacion);
                }
            } catch (SQLException e) {
                System.out.println("Error obteniendo EjemplarTipoPublicacionDTO: " + e.getMessage());
                System.out.println(e.getCause());
                dto = null;
            }
        } catch (SQLException e) {
            System.out.println("Error obteniendo EjemplarTipoPublicacionDTO: " + e.getMessage());
            System.out.println(e.getCause());
            dto = null;
        }
        return dto;
    }
}
