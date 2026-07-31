package com.biblioteca.controlador;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

import com.biblioteca.conexiones.DBConnection;
import com.biblioteca.dao.PrestamoDAO;
import com.biblioteca.dao.SancionDAO;
import com.biblioteca.dto.UsuarioFinSancionDTO;

/**
 * Controlador dedicado a operaciones relacionadas con sanciones manuales y
 * consultas auxiliares necesarias por la vista de sanción manual.
 */
public class ControladorSancionManual {

    /**
     * DBConnection para conexiones a la base de datos
     */
    private final DBConnection dbConnection;

    /**
     * Constructor con DBConnection (inyección)
     * 
     * @param dbConnection
     */
    public ControladorSancionManual(DBConnection dbConnection) {
        if (dbConnection == null) {
            throw new IllegalArgumentException("DBConnection cannot be null");
        }
        this.dbConnection = dbConnection;
    }

    /**
     * Obtiene el último préstamo por ejemplar delegando en PrestamoDAO.
     *
     * @param idEjemplar
     * @return arreglo con {id_prestamo, id_usuario, fecha_inicio, fecha_fin} o null
     */
    public String[] obtenerUltimoPrestamoPorEjemplar(int idEjemplar) {
        try (var conexion = this.dbConnection.getConnection()) {
            if (conexion == null) {
                System.out.println("No se puede obtener conexión a BD");
                return null;
            }
            PrestamoDAO prestamoDAO = new PrestamoDAO(conexion);
            return prestamoDAO.obtenerUltimoPrestamoPorEjemplar(idEjemplar);
        } catch (Exception e) {
            // En caso de error, devolver null y loguear el error
            System.out.println("Error al obtener conexión: " + e.getMessage());
            return null;
        }
    }

    /**
     * Obtiene la sanción activa para un usuario (delegación a SancionDAO)
     *
     * @param idUsuario
     * @return arreglo {id, fin_sancion} o null
     */
    public UsuarioFinSancionDTO obtenerSancionActivaPorUsuario(int idUsuario) {
        try (Connection conexion = this.dbConnection.getConnection()) {
            if (conexion == null) {
                System.out.println("No se puede obtener conexión a BD");
                return null;
            }
            SancionDAO sancionDAO = new SancionDAO(conexion);
            return sancionDAO.obtenerSancionActivaPorUsuario(idUsuario);
        } catch (SQLException e) {
            System.out.println("Error al obtener conexión: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.out.println("Error al obtener conexión: " + e.getMessage());
            return null;
        }
    }

    /**
     * Desactiva una sanción por id (delegación a SancionDAO)
     *
     * @param idSancion
     * @return true si desactivó la sanción
     */
    public boolean desactivarSancionPorId(int idSancion) {
        try (Connection conexion = this.dbConnection.getConnection()) {
            if (conexion == null) {
                System.out.println("No se puede obtener conexión a BD");
                return false;
            }
            SancionDAO sancionDAO = new SancionDAO(conexion);
            return sancionDAO.desactivarSancionPorId(idSancion);
        } catch (SQLException e) {
            System.out.println("Error al obtener conexión: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Error al obtener conexión: " + e.getMessage());
            return false;
        }
    }

    /**
     * Inserta una sanción (delegación a SancionDAO)
     *
     * @param idUsuario
     * @param idPrestamo
     * @param inicio
     * @param fin
     * @param descripcion
     * @return true si la inserción fue correcta
     */
    public boolean insertarSancion(int idUsuario, int idPrestamo, Date inicio, Date fin,
            String descripcion) {
        try (Connection conexion = this.dbConnection.getConnection()) {
            if (conexion == null) {
                System.out.println("No se puede obtener conexión a BD");
                return false;
            }
            SancionDAO sancionDAO = new SancionDAO(conexion);
            return sancionDAO.insertarSancion(idUsuario, idPrestamo, inicio, fin, descripcion);
        } catch (SQLException e) {
            System.out.println("Error al obtener conexión: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Error al obtener conexión: " + e.getMessage());
            return false;
        }
    }

}
