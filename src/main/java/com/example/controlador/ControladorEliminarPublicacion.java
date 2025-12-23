package com.example.controlador;

import java.sql.Connection;

import com.example.dao.PublicacionDAO;

/**
 * Controlador dedicado a la eliminación de publicaciones.
 * Encapsula la operación transaccional de dar de baja una publicación y sus
 * ejemplares,
 * además de comprobar la existencia de préstamos activos.
 */
public class ControladorEliminarPublicacion {
    /**
     * Referencia al controlador principal
     */
    private Controlador controlador;

    /**
     * Constructor
     * 
     * @param controlador
     */
    public ControladorEliminarPublicacion(Controlador controlador) {
        this.controlador = controlador;
    }

    /**
     * Elimina (marca como baja) una publicación si no tiene préstamos activos.
     *
     * @param idPublicacion id de la publicación a dar de baja
     * @return true si la baja fue satisfactoria, false si hay préstamos activos o
     *         error
     */
    public boolean eliminarPublicacion(int idPublicacion) {
        PublicacionDAO publicacionDAO = new PublicacionDAO();
        Connection conexion = new com.example.conexiones.MySQLConnection().getConnection();
        if (conexion == null) {
            System.out.println("No se puede obtener conexión a BD");
            return false;
        }

        // Comprobar préstamos activos (no transaccional, solo lectura)
        if (publicacionDAO.tienePrestamosActivos(idPublicacion)) {
            return false;
        }

        try {
            conexion.setAutoCommit(false);

            // Marcar ejemplares y publicación como baja dentro de la transacción
            if (!publicacionDAO.bajaPublicacion(conexion, idPublicacion)) {
                conexion.rollback();
                return false;
            }

            conexion.commit();
            return true;
        } catch (Exception e) {
            try {
                conexion.rollback();
            } catch (Exception ex) {
                System.out.println("Error al hacer rollback: " + ex.getMessage());
            }
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            return false;
        } finally {
            try {
                conexion.setAutoCommit(true);
                conexion.close();
            } catch (Exception ex) {
                System.out.println("Error cerrando conexión: " + ex.getMessage());
            }
        }
    }
}
