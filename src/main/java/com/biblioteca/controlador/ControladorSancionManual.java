package com.biblioteca.controlador;

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
        SancionDAO sancionDAO = new SancionDAO(this.dbConnection);
        return sancionDAO.obtenerSancionActivaPorUsuario(idUsuario);
    }

    /**
     * Desactiva una sanción por id (delegación a SancionDAO)
     *
     * @param idSancion
     * @return true si desactivó la sanción
     */
    public boolean desactivarSancionPorId(int idSancion) {
        SancionDAO sancionDAO = new SancionDAO(this.dbConnection);
        return sancionDAO.desactivarSancionPorId(idSancion);
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
    public boolean insertarSancion(int idUsuario, int idPrestamo, java.sql.Date inicio, java.sql.Date fin,
            String descripcion) {
        SancionDAO sancionDAO = new SancionDAO(this.dbConnection);
        return sancionDAO.insertarSancion(idUsuario, idPrestamo, inicio, fin, descripcion);
    }

}
