package com.biblioteca.controlador;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

import javax.swing.JOptionPane;

import com.biblioteca.conexiones.DBConnection;
import com.biblioteca.dao.PrestamoDAO;
import com.biblioteca.dao.SancionDAO;
import com.biblioteca.dto.ObtenerUltimoPrestamoPorEjemplarDTO;
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
    public ControladorSancionManual(final DBConnection dbConnection) {
        if (dbConnection == null) {
            throw new IllegalArgumentException("DBConnection cannot be null");
        }
        this.dbConnection = dbConnection;
    }

    /**
     * Aplica una sanción manual al usuario del ejemplar especificado.
     * 
     * @param idUsuario        ID del usuario seleccionado
     * @param idEjemplar       ID del ejemplar al que se aplica la sanción
     * @param finSancionString Fecha final de la sanción en formato YYYY-MM-DD
     * @param descripcion      Descripción de la sanción
     * @return null en caso de éxito, mensaje de error en caso de fallo
     */
    public final String aplicarSancionManual(final int idUsuario, final int idEjemplar, final String finSancionString,
            final String descripcion) {
        // Validar usuario seleccionado
        if (idUsuario == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione primero un usuario", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return "Error: Usuario no seleccionado";
        }
        // Convertir el string de fecha a LocalDate
        LocalDate finSancion;
        try {
            finSancion = LocalDate.parse(finSancionString);
        } catch (DateTimeParseException ex) {
            return "Error: Fecha fin inválida (formato YYYY-MM-DD)";
        }
        boolean exito = false;
        try (Connection conexion = this.dbConnection.getConnection()) {
            try {
                if (conexion == null) {
                    System.out.println("No se puede obtener conexión a BD");
                    return "Error: No se puede obtener conexión a BD";
                }

                // Empezamos transacción
                conexion.setAutoCommit(false);

                // Obtener el último préstamo por ejemplar.
                PrestamoDAO prestamoDAO = new PrestamoDAO(conexion);
                ObtenerUltimoPrestamoPorEjemplarDTO ultimoPrestamo = prestamoDAO
                        .obtenerUltimoPrestamoPorEjemplar(idEjemplar);

                // Validamos que la sanción sea para el usuario que tiene el último préstamo del
                // ejemplar
                if (ultimoPrestamo == null) {
                    return "Error: No se encontró un préstamo para el ejemplar seleccionado";
                } else if (ultimoPrestamo.getIdUsuario() != idUsuario) {
                    return "Error: El usuario seleccionado no es el último en tener el ejemplar";
                }

                // Obtención del id de préstamo
                int idPrestamo = ultimoPrestamo.getId();

                LocalDate fechaActual = LocalDate.now();
                if (finSancion.isBefore(fechaActual)) {
                    return "Error: La fecha fin no puede ser anterior a la fecha de inicio";
                }

                // Consultar sancion usuario
                SancionDAO sancionDAO = new SancionDAO(conexion);
                UsuarioFinSancionDTO sancionActiva = sancionDAO.obtenerSancionActivaPorUsuario(idUsuario);

                // Gestion fecha de sancion manual con consulta de sanción activa previa
                LocalDate nuevoFinSancion;
                if (sancionActiva != null && sancionActiva.getFinSancion() != null
                        && !sancionActiva.getFinSancion().isEmpty()) {
                    LocalDate finSancionActiva = LocalDate.parse(sancionActiva.getFinSancion());
                    long diasPendienteOriginal = ChronoUnit.DAYS.between(fechaActual, finSancionActiva);
                    long diasPendienteNuevaSancion = ChronoUnit.DAYS.between(fechaActual, finSancion);
                    nuevoFinSancion = fechaActual.plusDays(diasPendienteOriginal + diasPendienteNuevaSancion);
                    finSancion = nuevoFinSancion;
                } else {
                    nuevoFinSancion = finSancion;
                }

                // Desactivar sanción activa si existe
                if (sancionActiva != null) {
                    boolean desactivada = sancionDAO.desactivarSancionPorId(sancionActiva.getIdSancion());
                    if (!desactivada) {
                        return "Error: No se pudo desactivar la sanción activa";
                    }
                }

                // Insertar nueva sanción
                boolean insertado = sancionDAO.insertarSancion(idUsuario, idPrestamo, Date.valueOf(fechaActual),
                        Date.valueOf(nuevoFinSancion), descripcion);
                if (!insertado) {
                    return "Error: No se pudo insertar la nueva sanción";
                }

                // cerramos operacion y confirmamos transacción
                conexion.commit();
                exito = true;
                return null; // Indica éxito

            } finally {
                try {
                    if (!exito) {
                        conexion.rollback();
                    }
                    conexion.setAutoCommit(true);
                } catch (SQLException e) {
                    System.out.println("Error al restaurar auto-commit: " + e.getMessage());
                }
            } // comprobar que el usuario fue el ultimo en tener el ejemplar
        } catch (SQLException e) {
            System.out.println("Error al obtener conexión: " + e.getMessage());
            return "Error: " + e.getMessage();
        }

    }

}
