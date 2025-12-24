package com.example.controlador;

import com.example.dao.EjemplarDAO;
import com.example.dao.PrestamoDAO;
import com.example.dao.PublicacionDAO;
import com.example.dao.SancionDAO;
import com.example.dao.UsuarioDAO;

/**
 * Controlador para la lógica de devolución de préstamos
 */
public class ControladorDevolverPrestamo {
    /**
     * Controlador principal de la aplicación
     */
    private Controlador controlador;

    /**
     * Mensaje informativo sobre sanción (si se creó/actualizó una sanción durante la operación)
     */
    private String ultimaNotificacionSancion = null;

    /**
     * Constructor
     * 
     * @param controlador
     */
    public ControladorDevolverPrestamo(Controlador controlador) {
        this.controlador = controlador;
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
            UsuarioDAO dao = new UsuarioDAO();
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
        EjemplarDAO ejemplarDAO = new EjemplarDAO();
        PublicacionDAO publicacionDAO = new PublicacionDAO();
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
        String[] resultado = new String[] { ejemplar[0], ejemplar[1], ejemplar[2], ejemplar[4], titulo,
                (numEdicion == null ? "" : numEdicion), (tipo == null ? "" : tipo) };
        return resultado;
    }

    /**
     * Comprueba si existe un préstamo activo entre un usuario y un ejemplar
     */
    public boolean existePrestamoActivoUsuarioEjemplar(int idUsuario, int idEjemplar) {
        PrestamoDAO prestamoDAO = new PrestamoDAO();
        return prestamoDAO.existePrestamoActivoUsuarioEjemplar(idUsuario, idEjemplar);
    }

    /**
     * Registra la devolución (marca estado = FALSE). Devuelve null si éxito o
     * mensaje de error si fallo.
     */
    public String registrarDevolucion(int idUsuario, int idEjemplar) {
        // Validar usuario
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        String[] usuario = usuarioDAO.obtenerUsuarioYEstadoPorDniOId(String.valueOf(idUsuario));
        // Si no existe el usuario, devolver error
        if (usuario == null) {
            return "Usuario no encontrado";
        }
        // comprobar que exista préstamo activo
        PrestamoDAO prestamoDAO = new PrestamoDAO();
        String[] prestamo = prestamoDAO.obtenerPrestamoActivoPorUsuarioEjemplar(idUsuario, idEjemplar);
        // Si no existe préstamo activo, devolver error
        if (prestamo == null) {
            return "No existe un préstamo activo entre este usuario y el ejemplar";
        }
        // antes de marcar devolución, obtener detalles necesarios para sanción
        int idPrestamo = Integer.parseInt(prestamo[0]);
        java.time.LocalDate fechaFin = prestamo[2] == null || prestamo[2].isEmpty() ? null
                : java.time.LocalDate.parse(prestamo[2]);

        // ejecutar devolución
        boolean ok = prestamoDAO.devolverPrestamoUsuarioEjemplar(idUsuario, idEjemplar);
        if (!ok) {
            return "Error al marcar la devolución en la base de datos";
        }

        // aplicar sanción automática si corresponde
        // Solo a estudiantes
        String[] detUsuario = usuarioDAO.obtenerDetallesUsuario(idUsuario);
        String tipoCode = detUsuario != null ? detUsuario[5] : null; // E,P,...
        // Si es estudiante y hay fecha fin de préstamo
        if ("E".equalsIgnoreCase(tipoCode) && fechaFin != null) {
            java.time.LocalDate hoy = java.time.LocalDate.now();
            long diasRetraso = java.time.temporal.ChronoUnit.DAYS.between(fechaFin, hoy);
            // Si hay días de retraso
            if (diasRetraso > 0) {
                // obtener tipo de publicacion para el ejemplar
                EjemplarDAO ejemplarDAO = new EjemplarDAO();
                String[] ejemplar = ejemplarDAO.obtenerEjemplarPorId(idEjemplar);
                String tipoPub = null;
                if (ejemplar != null) {
                    int idPublicacion = Integer.parseInt(ejemplar[1]);
                    PublicacionDAO publicacionDAO = new PublicacionDAO();
                    String[] detalles = publicacionDAO.obtenerPublicacionDetallesPorId(idPublicacion);
                    tipoPub = detalles != null ? detalles[0] : null;
                }
                int diasSancion = 0;
                // calcular días sanción según tipo publicación
                if ("L".equalsIgnoreCase(tipoPub)) {
                    diasSancion = (int) (2 * diasRetraso);
                } else if ("R".equalsIgnoreCase(tipoPub)) {
                    diasSancion = 10;
                }
                // aplicar sanción si hay días a sancionar
                if (diasSancion > 0) {
                    java.time.LocalDate inicioSancion = hoy;
                    SancionDAO sancionDAO = new SancionDAO();
                    // Comprobar si ya existe una sanción activa para este usuario
                    String[] sancionActiva = sancionDAO.obtenerSancionActivaPorUsuario(idUsuario);
                    java.time.LocalDate finSancion;
                    String descripcionBase = "Retraso en devolución " + diasRetraso + " días";
                    String descripcion = descripcionBase;
                    boolean previaDesactivada = false;
                    // Si ya hay sanción activa, acumular días y desactivar la previa
                    if (sancionActiva != null && sancionActiva[1] != null && !sancionActiva[1].isEmpty()) {
                        try {
                            java.time.LocalDate finAct = java.time.LocalDate.parse(sancionActiva[1]);
                            // Extender la fecha final acumulando los días nuevos
                            finSancion = finAct.plusDays(diasSancion);
                            descripcion = descripcionBase + " (Acumulativa: sanción activa hasta " + finAct
                                    + "; se añaden " + diasSancion + " días)";
                            // intentar desactivar la sanción previa
                            try {
                                int idPrev = Integer.parseInt(sancionActiva[0]);
                                previaDesactivada = sancionDAO.desactivarSancionPorId(idPrev);
                            } catch (Exception ex2) {
                                // registrar el error al intentar desactivar la sanción previa
                                System.out.println("Error desactivando sanción previa (id=" + sancionActiva[0] + "): " + ex2.getMessage());
                                ex2.printStackTrace();
                            }
                        } catch (Exception ex) {
                            // si no se puede parsear la fecha anterior, fallback a hoy + dias
                            System.out.println("Error parsing previous sanction end date: " + ex.getMessage());
                            ex.printStackTrace();
                            finSancion = hoy.plusDays(diasSancion);
                            descripcion = descripcionBase + " (Acumulativa: fallo leyendo sanción previa; se añaden "
                                    + diasSancion + " días)";
                        }
                    } else {
                        finSancion = hoy.plusDays(diasSancion);
                    }
                    // insertar sanción
                    boolean ins = sancionDAO.insertarSancion(idUsuario, idPrestamo,
                            java.sql.Date.valueOf(inicioSancion),
                            java.sql.Date.valueOf(finSancion), descripcion);
                    if (!ins) {
                        return "Devolución registrada, pero error aplicando sanción automática";
                    }
                    // Preparar notificación para la UI
                    try {
                        // analizar sanción activa previa para el mensaje
                        String finActStr = sancionActiva != null ? (sancionActiva[1] == null ? "" : sancionActiva[1]) : null;
                        // construir mensaje adecuado
                        if (finActStr != null && !finActStr.isEmpty()) {
                            java.time.LocalDate finAct = java.time.LocalDate.parse(finActStr);
                            java.time.LocalDate finNuevo = finSancion;
                            // comparar fechas
                            if (finNuevo.isAfter(finAct)) {
                                ultimaNotificacionSancion = "Se ha aplicado una sanción acumulativa: anterior fin " + finAct + 
                                        ", nuevo fin " + finNuevo + "." + (previaDesactivada ? " La sanción previa ha sido desactivada." : "");
                            } else {
                                ultimaNotificacionSancion = "Se ha aplicado una sanción (acumulativa): anterior fin " + finAct + 
                                        ", no se añadió plazo adicional." + (previaDesactivada ? " La sanción previa ha sido desactivada." : "");
                            }
                        } else {
                            ultimaNotificacionSancion = "Se ha aplicado una sanción: fin " + finSancion + "." + (previaDesactivada ? " La sanción previa ha sido desactivada." : "");
                        }
                    } catch (Exception e) {
                        System.out.println("Error preparando notificación de sanción automática: " + e.getMessage());
                        e.printStackTrace();
                        ultimaNotificacionSancion = "Se ha aplicado una sanción." + (previaDesactivada ? " La sanción previa ha sido desactivada." : "");
                    }                }
            }
        }
        return null;
    }
}
