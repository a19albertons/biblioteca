package com.example.controlador;

import com.example.dao.EjemplarDAO;

/**
 * Controlador para la gestión de ejemplares
 */
public class ControladorEjemplares {
    /**
     * DBConnection para conexiones a la base de datos
     */
    private final com.example.conexiones.DBConnection dbConnection;

    /**
     * Constructor con DBConnection (inyección)
     * 
     * @param dbConnection
     */
    public ControladorEjemplares(com.example.conexiones.DBConnection dbConnection) {
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
        EjemplarDAO ejemplarDAO = new EjemplarDAO(this.dbConnection);
        return ejemplarDAO.listaEjemplaresPorPublicacion(idPublicacion);
    }

    /**
     * Obtiene los detalles de un ejemplar por id (para vistas/diálogos).
     *
     * @param idEjemplar
     * @return arreglo con {id, id_publicacion, num_ejemplar, fecha_adquisicion,
     *         estado} o null
     */
    public String[] obtenerDetallesEjemplar(int idEjemplar) {
        EjemplarDAO ejemplarDAO = new EjemplarDAO(this.dbConnection);
        return ejemplarDAO.obtenerEjemplarPorId(idEjemplar);
    }

}
