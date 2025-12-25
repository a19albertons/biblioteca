package com.example.controlador;

import java.sql.Connection;

import com.example.dao.EjemplarDAO;

/**
 * Controlador dedicado a la creación de nuevos ejemplares desde el diálogo.
 * Encapsula la operación transaccional que calcula el siguiente número de
 * ejemplar y lo inserta en la tabla `ejemplares`.
 */
public class ControladorNuevoEjemplarDialog {
    /**
     * Constructor
     */
    public ControladorNuevoEjemplarDialog() {
        // No state required
    }

    /**
     * Crea un nuevo ejemplar para la publicación especificada con la fecha de
     * adquisición indicada.
     * Esta operación es transaccional y calcula el siguiente número de ejemplar
     * para la publicación.
     *
     * @param idPublicacion
     * @param fechaAdquisicion fecha de adquisición (java.time.LocalDate)
     * @return true si la inserción fue satisfactoria
     */
    public boolean crearEjemplar(int idPublicacion, java.time.LocalDate fechaAdquisicion) {
        EjemplarDAO ejemplarDAO = new EjemplarDAO();
        // Convertir la fecha proporcionada (java.time.LocalDate) a java.sql.Date
        // Si no se proporciona fecha, usar la fecha actual
        java.sql.Date fechaSql = (fechaAdquisicion != null) ? java.sql.Date.valueOf(fechaAdquisicion)
                : new java.sql.Date(System.currentTimeMillis());

        // Obtener conexión a la base de datos
        Connection conexion = new com.example.conexiones.MySQLConnection().getConnection();
        // Comprobación básica de disponibilidad de conexión
        if (conexion == null) {
            System.out.println("No se puede obtener conexión a BD");
            return false;
        }
        try {
            // Iniciar transacción (desactivar auto-commit)
            conexion.setAutoCommit(false);

            // Calcular el siguiente número de ejemplar para la publicación
            int siguiente = ejemplarDAO.siguienteNumEjemplar(conexion, idPublicacion);
            if (siguiente == -1) {
                // Error al calcular siguiente número -> rollback y fallo
                conexion.rollback();
                return false;
            }

            // Intentar insertar el nuevo ejemplar con el número calculado
            boolean ok = ejemplarDAO.insertarEjemplar(conexion, idPublicacion, siguiente, fechaSql);
            if (!ok) {
                // Inserción fallida -> rollback
                conexion.rollback();
                return false;
            }

            // Commit de la transacción si todo fue correcto
            conexion.commit();
            return true;
        } catch (Exception e) {
            // En caso de excepción, intentar rollback y reportar el error
            try {
                conexion.rollback();
            } catch (Exception ex) {
                System.out.println("Error al hacer rollback: " + ex.getMessage());
            }
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            return false;
        } finally {
            // Restaurar auto-commit y cerrar conexión
            try {
                conexion.setAutoCommit(true);
                conexion.close();
            } catch (Exception ex) {
                System.out.println("Error cerrando conexión: " + ex.getMessage());
            }
        }
    }
}
