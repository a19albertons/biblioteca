package com.biblioteca.controlador;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.biblioteca.conexiones.DBConnection;
import com.biblioteca.dao.EjemplarDAO;
import com.biblioteca.dao.PrestamoDAO;
import com.biblioteca.dao.PublicacionDAO;
import com.biblioteca.dao.SancionDAO;
import com.biblioteca.dao.UsuarioDAO;
import com.biblioteca.dto.EjemplarTipoPublicacionDTO;
import com.biblioteca.dto.RegistroDevolucionDTO;
import com.biblioteca.dto.UsuarioFinSancionDTO;
import com.biblioteca.dto.UsuarioTipoDTO;
import com.biblioteca.modelo.TipoPublicacion;
import com.biblioteca.modelo.TipoUsuario;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

/**
 * Controlador para la lógica de devolución de préstamos
 */
public class ControladorDevolverPrestamo {
    /**
     * DBConnection para la base de datos
     */
    private final DBConnection dbConnection;

    /**
     * Mensaje informativo sobre sanción (si se creó/actualizó una sanción durante
     * la operación)
     */
    private String ultimaNotificacionSancion = null;

    /**
     * Constructor
     * 
     * @param dbConnection
     */
    public ControladorDevolverPrestamo(DBConnection dbConnection) {
        if (dbConnection == null) {
            throw new IllegalArgumentException("DBConnection cannot be null");
        }
        this.dbConnection = dbConnection;
    }

    /**
     * Obtiene y limpia la última notificación de sanción
     *
     * @return mensaje o null
     */
    public String obtenerYLimpiarUltimaNotificacionSancion() {
        String tmp = ultimaNotificacionSancion;
        ultimaNotificacionSancion = null;
        return tmp;
    }

    /**
     * Busca el usuario por DNI o ID y devuelve arreglo: id, dni, nombre_completo,
     * sancion_activa (SANCIONADO/ACTIVO/BAJA), tipo_desc
     */
    public String[] buscarUsuarioPorDniOId(String dniOrId) {
        try {
            UsuarioDAO dao = new UsuarioDAO(this.dbConnection);
            return dao.obtenerUsuarioYEstadoPorDniOId(dniOrId == null ? "" : dniOrId.trim());
        } catch (Throwable t) {
            // Evitar que errores de compilación/Classpath propaguen una excepción no
            // controlada
            System.out.println("Error buscando usuario por DNI/ID: " + t.getMessage());
            t.printStackTrace();
            return null;
        }
    }

    /**
     * Detecta un ejemplar por su id y devuelve arreglo con info o null
     * Retorna: idEjemplar, idPublicacion, numEjemplar, estadoEjemplar, titulo,
     * numEdicion, tipoPublicacion
     */
    public String[] detectarEjemplar(int idEjemplar) {
        String[] resultado = null;
        try (Connection conexion = this.dbConnection.getConnection()) {
            if (conexion == null) {
                System.out.println("No se puede obtener conexión a BD");
                return null;
            }
            EjemplarDAO ejemplarDAO = new EjemplarDAO(conexion);
            PublicacionDAO publicacionDAO = new PublicacionDAO(conexion);
            // obtener info ejemplar
            String[] ejemplar = ejemplarDAO.obtenerEjemplarPorId(idEjemplar);
            if (ejemplar == null) {
                return null;
            }

            int idPublicacion = Integer.parseInt(ejemplar[1]);
            String[] detallesPub = publicacionDAO.obtenerPublicacionDetallesPorId(idPublicacion);
            if (detallesPub == null) {
                return null;
            }
            String tipo = detallesPub[0];
            String titulo = detallesPub[1];
            String numEdicion = detallesPub[8];
            resultado = new String[] { ejemplar[0], ejemplar[1], ejemplar[2], ejemplar[4], titulo,
                    (numEdicion == null ? "" : numEdicion), (tipo == null ? "" : tipo) };
        } catch (Exception e) {
            System.out.println("Error al obtener conexión: " + e.getMessage());
            return null;
        }

        return resultado;
    }

    /**
     * Comprueba si existe un préstamo activo entre un usuario y un ejemplar
     * 
     * @param idUsuario  ID del usuario
     * @param idEjemplar ID del ejemplar
     * @return true si existe un préstamo activo, false en caso contrario
     */
    private boolean existePrestamoActivoUsuarioEjemplar(int idUsuario, int idEjemplar) {
        try (Connection conexion = this.dbConnection.getConnection()) {
            if (conexion == null) {
                System.out.println("No se puede obtener conexión a BD");
                return false;
            }
            PrestamoDAO prestamoDAO = new PrestamoDAO(conexion);
            return prestamoDAO.existePrestamoActivoUsuarioEjemplar(idUsuario, idEjemplar);
        } catch (Exception e) {
            System.out.println("Error al obtener conexión: " + e.getMessage());
            return false;
        }
    }

    /**
     * Valida si un prestamo esta activo, marca la devolución, calcula si existe
     * alguna sanción a aplicar y la aplica. Además gestiona la transacción de la
     * base de datos. Devuelve null si éxito o mensaje de error si fallo.
     * 
     * @param idUsuario  Usuario que devuelve el ejemplar
     * @param idEjemplar codigo del ejemplar que se devuelve
     * @return null si éxito o mensaje de error si fallo
     */
    public String devolverPrestamo(int idUsuario, int idEjemplar) {
        try (Connection conexion = this.dbConnection.getConnection()) {
            // 1. Comprobar si existe un préstamo activo entre el usuario y el ejemplar
            Boolean existe = this.existePrestamoActivoUsuarioEjemplar(idUsuario, idEjemplar);
            if (!existe) {
                return "No se ha encontrado un préstamo activo para el usuario y ejemplar proporcionados.";
            }

            // Cambiar tipo de conextión a manual para controlar la transacción
            try {
                conexion.setAutoCommit(false);
            } catch (SQLException e) {
                e.printStackTrace();
                return "Error al iniciar la transacción de la base de datos: " + e.getMessage();
            }
            ;

            // 2. Registrar la devolución
            PrestamoDAO prestamoDAO = new PrestamoDAO(conexion);
            RegistroDevolucionDTO resultado = prestamoDAO.obtenerPrestamoActivoPorUsuarioEjemplar(idUsuario,
                    idEjemplar);

            // Comprobacińo respuesta consulta
            if (resultado == null) {
                return "No se ha encontrado un préstamo activo para el usuario y ejemplar proporcionados.";
            }

            // 3. Registrar la devolución
            int idPrestamo = resultado.getIdPrestamo();
            LocalDate fechaFin = resultado.getFechaFin();

            boolean devolver = prestamoDAO.devolverPrestamoUsuarioEjemplar(idUsuario, idEjemplar);
            if (!devolver) {
                return "Error al marcar la devolución en la base de datos.";
            }

            // 4. Aplicar sanción automática si corresponde
            UsuarioDAO usuarioDAO = new UsuarioDAO(this.dbConnection);
            UsuarioTipoDTO usuario = usuarioDAO.obtenerUSuarioTipoDTO(idUsuario);

            if (usuario == null) {
                return "Error al obtener información del usuario para aplicar sanción automática.";
            }

            // 4.1 Valida el tipo de usuario
            // Solo se hace la sanción al estudiante (TipoUsuario.E)
            if (usuario.getTipoUsuario() == TipoUsuario.E) {
                LocalDate hoy = LocalDate.now();
                long diasRetraso = ChronoUnit.DAYS.between(fechaFin, hoy);

                // 4.2 valida si hay retraso en la devolución
                // Comprobamos si hay algun dia de retraso para activar la sanción automática
                if (diasRetraso > 0) {
                    EjemplarDAO ejemplarDAO = new EjemplarDAO(conexion);
                    EjemplarTipoPublicacionDTO ejemplarTipoPublicacionDTO = ejemplarDAO
                            .obtenerEjemplarPublicacionDTO(idEjemplar);

                    // Verificamos que se haya obtenido correctamente la información del ejemplar y
                    // su tipo de publicación
                    if (ejemplarTipoPublicacionDTO == null) {
                        return "Error al obtener información del ejemplar para aplicar sanción automática.";
                    }

                    // 4.3 Calcula los días de sanción según el tipo de publicación
                    int diasSancion = 0;
                    // Calcular dias de sanción según tipo de publicación
                    // Si el tipo de publicación es Libro (L), la sanción es 2 días por cada día de
                    // retraso, mientras que revista es 10 días fijos
                    if (ejemplarTipoPublicacionDTO.getTipoPublicacion() == TipoPublicacion.L) {
                        diasSancion = (int) (2 * diasRetraso);
                    } else if (ejemplarTipoPublicacionDTO.getTipoPublicacion() == TipoPublicacion.R) {
                        diasSancion = 10;
                    }

                    // 5. Aplicar sanción
                    SancionDAO sancionDAO = new SancionDAO(conexion);
                    UsuarioFinSancionDTO sancionActiva = sancionDAO.obtenerSancionActivaPorUsuario(idUsuario);

                    LocalDate finSancion;
                    String descripcionBase = "Retraso en devolución " + diasRetraso + " días";
                    String descripcion = descripcionBase;
                    boolean previaDesactivada = false;
                    // Si ya hay sanción activa, acumular días y desactivar la previa

                    if (sancionActiva != null && sancionActiva.getFinSancion() != null
                            && !sancionActiva.getFinSancion().isEmpty()) {
                        try {
                            LocalDate finAct = LocalDate.parse(sancionActiva.getFinSancion());

                            // Incrementar la fecha final acumulando los días nuevos
                            finSancion = finAct.plusDays(diasSancion);

                            descripcion = descripcionBase + " (Acumulativa: sanción activa hasta " + finAct
                                    + "; se añaden " + diasSancion + " días)";

                            try {
                                int idPrev = sancionActiva.getIdSancion();
                                previaDesactivada = sancionDAO.desactivarSancionPorId(idPrev);

                                if (!previaDesactivada) {
                                    return "Error al desactivar la sanción previa para aplicar la sanción acumulativa.";
                                }
                            } catch (Exception ex2) {
                                // registrar el error al intentar desactivar la sanción previa
                                System.out
                                        .println("Error desactivando sanción previa (id=" + sancionActiva.getIdSancion()
                                                + "): " + ex2.getMessage());
                                ex2.printStackTrace();
                                return "Error al desactivar la sanción previa para aplicar la sanción acumulativa.";
                            }

                        } catch (Exception e) {
                            // control de errores de conversion.
                            System.out
                                    .println("Error al sumar la nueva sanción a la sanción activa: " + e.getMessage());
                            e.printStackTrace();
                            return "Error al calcular la fecha de fin de sanción acumulativa.";
                        }
                        // Calculo para cuando no hay sancion previa
                    } else {
                        finSancion = hoy.plusDays(diasSancion);
                    }

                    // Insertar la nueva sanción en la base de datos
                    Boolean crearSancon = sancionDAO.insertarSancion(
                            idUsuario,
                            idPrestamo,
                            Date.valueOf(hoy),
                            Date.valueOf(finSancion),
                            descripcion);

                    if (!crearSancon) {
                        return "Error al insertar la sanción automática en la base de datos.";
                    }

                    try {
                        String finOriginal = sancionActiva.getFinSancion();
                        if (finOriginal != null && !finOriginal.isEmpty()) {
                            LocalDate finAct = LocalDate.parse(finOriginal);
                            LocalDate finNuevo = finSancion;
                            if (finNuevo.isAfter(finAct)) {
                                ultimaNotificacionSancion = "La nueva fecha de sanción ha sido actualizada a "
                                        + finNuevo;
                            } else {
                                ultimaNotificacionSancion = "La sanción permanece sin cambios a " + finAct;
                            }
                        } else {
                            ultimaNotificacionSancion = "Se ha aplicado una nueva sanción hasta " + finSancion;
                        }
                    } catch (Exception e) {
                        System.out.println("Error preparando notificación de sanción automática: " + e.getMessage());
                        e.printStackTrace();
                        ultimaNotificacionSancion = "Se ha aplicado una sanción automática.";
                    }
                }
            }

            // 6. Commit de la transacción
            try {
                this.dbConnection.getConnection().commit();
            } catch (SQLException e) {
                e.printStackTrace();
                return "Error al finalizar la transacción de la base de datos: " + e.getMessage();
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener conexión: " + e.getMessage());
            return null;
        } finally {
            // Restaurar el modo de auto-commit y hacer rollack no debería hacer nada si
            // todo fue exitoso
            try {
                this.dbConnection.getConnection().rollback();
                this.dbConnection.getConnection().setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
                return "Error al restaurar el modo de auto-commit de la base de datos: " + e.getMessage();
            }
        }

        return null; // éxito
    }
}
