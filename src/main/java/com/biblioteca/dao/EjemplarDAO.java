package com.biblioteca.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
     * Conexion para la base de datos
     */
    private final Connection conexion;

    // SQL constants 🔧
    private static final String SQL_LISTA_EJEMPLARES_POR_PUBLICACION = "SELECT e.id, e.num_ejemplar, e.fecha_adquisicion, e.estado AS en_servicio, "
            + "(SELECT COUNT(*) FROM prestamos p WHERE p.id_ejemplar = e.id AND p.estado = TRUE) AS prestamos_activos "
            + "FROM ejemplares e WHERE e.id_publicacion = ? ORDER BY e.num_ejemplar ASC";
    private static final String SQL_SELECT_SIGUIENTE_NUM_EJEMPLAR = "SELECT COALESCE(MAX(num_ejemplar),0) + 1 AS siguiente FROM ejemplares WHERE id_publicacion = ?";
    private static final String SQL_INSERT_EJEMPLAR = "INSERT INTO ejemplares (id_publicacion, num_ejemplar, fecha_adquisicion, estado) VALUES (?, ?, ?, TRUE)";
    private static final String SQL_SELECT_EJEMPLAR_POR_ID = "SELECT id, id_publicacion, num_ejemplar, fecha_adquisicion, estado FROM ejemplares WHERE id = ?";
    private static final String SQL_UPDATE_EJEMPLAR = "UPDATE ejemplares SET fecha_adquisicion = ?, estado = ? WHERE id = ?";
    private static final String SQL_CNT_PRESTAMOS_POR_EJEMPLAR = "SELECT COUNT(*) AS cnt FROM prestamos WHERE id_ejemplar = ? AND estado = TRUE";
    private static final String SQL_UPDATE_BAJA_EJEMPLAR = "UPDATE ejemplares SET estado = FALSE WHERE id = ?";
    private static final String SQL_OBTENER_EJEMPLAR_TIPO_PUBLICACION_DTO = "SELECT e.num_ejemplar, p.tipo FROM ejemplares e JOIN publicaciones p ON e.id_publicacion = p.id WHERE e.id = ?";

    /**
     * Constructor del DAO
     * 
     * @param conexion
     */
    public EjemplarDAO(Connection conexion) {
        if (conexion == null) {
            throw new IllegalArgumentException("DBConnection cannot be null");
        }
        this.conexion = conexion;
    }

    /**
     * Lista los ejemplares de una publicación
     *
     * @param idPublicacion
     * @return String[][] con columnas: id, num_ejemplar, fecha_adquisicion, estado
     */
    public String[][] listaEjemplaresPorPublicacion(int idPublicacion) {
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
     * @param idPublicacion
     * @return siguiente num_ejemplar o -1 en caso de error
     */
    public int siguienteNumEjemplar(int idPublicacion) {
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
     * @param idPublicacion
     * @param numEjemplar
     * @param fechaAdquisicion (java.sql.Date)
     * @return true si la inserción fue exitosa
     */
    public boolean insertarEjemplar(int idPublicacion, int numEjemplar, Date fechaAdquisicion) {
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
    public EstadoEjemplarDTO obtenerEjemplarPorId(int idEjemplar) {
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
     * @param idEjemplar
     * @param fechaAdquisicion (java.sql.Date)
     * @param estado           estado (true = activo/en servicio, false = baja)
     * @return true si la actualización afectó exactamente una fila
     */
    public boolean actualizarEjemplar(int idEjemplar, Date fechaAdquisicion, boolean estado) {
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
        } catch (Exception e) {
            // Error
            System.out.println("Error actualizando ejemplar: " + e.getMessage());
            System.out.println(e.getCause());
            return false;
        }
    }

    /**
     * Comprueba si un ejemplar tiene préstamos activos (estado TRUE).
     *
     * @param idEjemplar
     * @return true si existen préstamos activos
     */
    public boolean tienePrestamosActivosEjemplar(int idEjemplar) {
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
        } catch (Exception e) {
            System.out.println("Error comprobando préstamos activos: " + e.getMessage());
            System.out.println(e.getCause());
        }
        return false;
    }

    /**
     * Marca un ejemplar como baja (estado = FALSE) dentro de la transacción.
     *
     * @param idEjemplar
     * @return true si la actualización afectó exactamente una fila
     */
    public boolean bajaEjemplar(int idEjemplar) {
        // Consulta SQLSQL_OBTENER_EJEMPLAR_PUBLICACION_DTO
        final String sql = SQL_UPDATE_BAJA_EJEMPLAR;
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            // Asignar parámetro
            ps.setInt(1, idEjemplar);
            // Ejecutar actualización
            int rows = ps.executeUpdate();
            return rows == 1;
        } catch (Exception e) {
            System.out.println("Error marcando ejemplar como baja: " + e.getMessage());
            System.out.println(e.getCause());
            return false;
        }
    }

    public EjemplarTipoPublicacionDTO obtenerEjemplarPublicacionDTO(int idEjemplar) {
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
            } catch (Exception e) {
                System.out.println("Error obteniendo EjemplarTipoPublicacionDTO: " + e.getMessage());
                System.out.println(e.getCause());
                dto = null;
            }
        } catch (Exception e) {
            System.out.println("Error obteniendo EjemplarTipoPublicacionDTO: " + e.getMessage());
            System.out.println(e.getCause());
            dto = null;
        }
        return dto;
    }
}
