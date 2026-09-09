package com.biblioteca.controlador;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

import com.biblioteca.conexiones.DBConnection;
import com.biblioteca.dao.EjemplarDAO;

/**
 * Controlador dedicado a la creación de nuevos ejemplares desde el diálogo.
 * Encapsula la operación transaccional que calcula el siguiente número de
 * ejemplar y lo inserta en la tabla `ejemplares`.
 */
public class ControladorNuevoEjemplarDialog {
    /**
     * DBConnection para conexiones a la base de datos
     */
    private final DBConnection dbConnection;

    /**
     * Constructor que permite inyectar una `DBConnection` (recomendado para tests
     * y para la nueva arquitectura).
     *
     * @param dbConnection la conexión a la base de datos
     */
    public ControladorNuevoEjemplarDialog(final DBConnection dbConnection) {
        if (dbConnection == null) {
            throw new IllegalArgumentException("DBConnection cannot be null");
        }
        this.dbConnection = dbConnection;
    }

    /**
     * Crea un nuevo ejemplar para la publicación especificada con la fecha de
     * adquisición indicada.
     * Esta operación es transaccional y calcula el siguiente número de ejemplar
     * para la publicación.
     *
     * @param idPublicacion    ID de la publicación
     * @param fechaAdquisicion fecha de adquisición (java.time.LocalDate)
     * @return true si la inserción fue satisfactoria
     */
    public boolean crearEjemplar(final int idPublicacion, final LocalDate fechaAdquisicion) {

        // Obtener conexión a la base de datos
        try (Connection conexion = this.dbConnection.getConnection()) {
            // Comprobación básica de disponibilidad de conexión
            if (conexion == null) {
                System.out.println("No se puede obtener conexión a BD");
                return false;
            }

            EjemplarDAO ejemplarDAO = new EjemplarDAO(conexion);
            // Convertir la fecha proporcionada (LocalDate) a java.sql.Date
            // Si no se proporciona fecha, usar la fecha actual
            Date fechaSql = (fechaAdquisicion != null) ? Date.valueOf(fechaAdquisicion)
                    : new Date(System.currentTimeMillis());

            try {
                // Iniciar transacción (desactivar auto-commit)
                conexion.setAutoCommit(false);

                // Calcular el siguiente número de ejemplar para la publicación
                int siguiente = ejemplarDAO.siguienteNumEjemplar(idPublicacion);
                if (siguiente == -1) {
                    // Error al calcular siguiente número -> rollback y fallo
                    conexion.rollback();
                    return false;
                }

                // Intentar insertar el nuevo ejemplar con el número calculado
                boolean ok = ejemplarDAO.insertarEjemplar(idPublicacion, siguiente, fechaSql);
                if (!ok) {
                    // Inserción fallida -> rollback
                    conexion.rollback();
                    return false;
                }

                // Commit de la transacción si todo fue correcto
                conexion.commit();
                return true;
            } catch (SQLException e) {
                // En caso de excepción, intentar rollback y reportar el error
                try {
                    conexion.rollback();
                } catch (SQLException ex) {
                    System.out.println("Error al hacer rollback: " + ex.getMessage());
                }
                System.out.println(e.getMessage());
                System.out.println(e.getCause());
                return false;
            } finally {
                // Restaurar auto-commit y cerrar conexión
                try {
                    conexion.setAutoCommit(true);
                } catch (SQLException ex) {
                    System.out.println("Error habilitando el modo autocommit de la base de datos: " + ex.getMessage());
                }
            }
        } catch (SQLException e1) {
            System.out.println("Error al obtener conexión: " + e1.getMessage());
            return false;
        }
    }
}
