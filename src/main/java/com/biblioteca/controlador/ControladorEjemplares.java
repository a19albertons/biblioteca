package com.biblioteca.controlador;

import java.sql.Connection;

import com.biblioteca.conexiones.DBConnection;
import com.biblioteca.dao.EjemplarDAO;
import com.biblioteca.dto.EstadoEjemplarDTO;

/**
 * Controlador para la gestión de ejemplares
 */
public class ControladorEjemplares {
    /**
     * DBConnection para conexiones a la base de datos
     */
    private final DBConnection dbConnection;

    /**
     * Constructor con DBConnection (inyección)
     * 
     * @param dbConnection
     */
    public ControladorEjemplares(DBConnection dbConnection) {
        if (dbConnection == null) {
            throw new IllegalArgumentException("DBConnection cannot be null");
        }
        this.dbConnection = dbConnection;
    }

    /**
     * Obtiene los ejemplares de una publicación
     *
     * @param idPublicacion
     * @return String[][] con columnas: id, num_ejemplar, fecha, estado
     */
    public String[][] obtenerEjemplaresPorPublicacion(int idPublicacion) {
        try (Connection conexion = this.dbConnection.getConnection()) {
            if (conexion == null) {
                System.out.println("No se puede obtener conexión a BD");
                return null;
            }
            EjemplarDAO ejemplarDAO = new EjemplarDAO(conexion);
            return ejemplarDAO.listaEjemplaresPorPublicacion(idPublicacion);
        } catch (Exception e) {
            System.out.println("Error al obtener conexión: " + e.getMessage());
            return null;
        }
    }

    /**
     * Obtiene los detalles de un ejemplar por id (para vistas/diálogos).
     *
     * @param idEjemplar
     * @return arreglo con {id, id_publicacion, num_ejemplar, fecha_adquisicion,
     *         estado} o null
     */
    public EstadoEjemplarDTO obtenerDetallesEjemplar(int idEjemplar) {
        try (Connection conexion = this.dbConnection.getConnection()) {
            if (conexion == null) {
                System.out.println("No se puede obtener conexión a BD");
                return null;
            }
            EjemplarDAO ejemplarDAO = new EjemplarDAO(conexion);
            return ejemplarDAO.obtenerEjemplarPorId(idEjemplar);
        } catch (Exception e) {
            System.out.println("Error al obtener conexión: " + e.getMessage());
            return null;
        }
    }

}
