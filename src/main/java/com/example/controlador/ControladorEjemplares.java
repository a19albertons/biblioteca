package com.example.controlador;

import com.example.dao.EjemplarDAO;

/**
 * Controlador para la gestión de ejemplares
 */
public class ControladorEjemplares {

    /**
     * Obtiene los ejemplares de una publicación
     *
     * @param idPublicacion
     * @return String[][] con columnas: id, num_ejemplar, fecha, estado
     */
    public String[][] obtenerEjemplaresPorPublicacion(int idPublicacion) {
        EjemplarDAO ejemplarDAO = new EjemplarDAO();
        return ejemplarDAO.listaEjemplaresPorPublicacion(idPublicacion);
    }


}
