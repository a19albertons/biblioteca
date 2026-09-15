package com.biblioteca.controlador;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.annotation.Nonnull;

import com.biblioteca.conexiones.DBConnection;
import com.biblioteca.dao.EjemplarDAO;
import com.biblioteca.dao.PrestamoDAO;
import com.biblioteca.dao.PublicacionDAO;
import com.biblioteca.dao.UsuarioDAO;
import com.biblioteca.dto.EjemplarConTituloDTO;
import com.biblioteca.dto.EstadoEjemplarDTO;
import com.biblioteca.dto.ObtenerPublicacionDetallesPorIdDTO;
import com.biblioteca.dto.UsuarioEstadoPorDNIOID;
import com.biblioteca.modelo.TipoPublicacion;

/**
 * Controlador para la lógica de concesión de préstamos
 */
public class ControladorConcederPrestamo {
    /**
     * DBConnection para conexiones a la base de datos
     */
    private final DBConnection dbConnection;

    /**
     * Constructor con DBConnection (inyección)
     *
     * @param dbConnection la conexión a la base de datos
     */
    public ControladorConcederPrestamo(@Nonnull final DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    /**
     * Busca el usuario por DNI o ID y devuelve arreglo: id, dni, nombre_completo,
     * sancion_activa (SANCIONADO/ACTIVO/BAJA), tipo_desc
     *
     * @param dniOrId el DNI o ID del usuario a buscar
     * @return el usuario encontrado con su estado o null si no existe o hay error
     */
    public UsuarioEstadoPorDNIOID buscarUsuarioPorDniOId(final String dniOrId) {
        try (Connection conexion = this.dbConnection.getConnection()) {
            if (conexion == null) {
                System.out.println("No se puede obtener conexión a BD");
                return null;
            }
            UsuarioDAO dao = new UsuarioDAO(conexion);
            return dao.obtenerUsuarioYEstadoPorDniOId(dniOrId == null ? "" : dniOrId.trim());
        } catch (SQLException e) {
            System.out.println("Error al obtener conexión: " + e.getMessage());
            return null;
        }
    }

    /**
     * Detecta un ejemplar por su id y devuelve arreglo con info o null
     * Retorna: idEjemplar, idPublicacion, numEjemplar, estadoEjemplar, titulo,
     * numEdicion, tipoPublicacion
     *
     * @param idEjemplar el ID del ejemplar a buscar
     * @return el ejemplar encontrado con su información o null si no existe o hay
     *         error
     */
    public EjemplarConTituloDTO detectarEjemplar(final int idEjemplar) {
        EjemplarConTituloDTO resultado = null;
        try (Connection conexion = this.dbConnection.getConnection()) {
            EjemplarDAO ejemplarDAO = new EjemplarDAO(conexion);
            PublicacionDAO publicacionDAO = new PublicacionDAO(conexion);

            // obtener info ejemplar
            EstadoEjemplarDTO ejemplar = ejemplarDAO.obtenerEjemplarPorId(idEjemplar);
            if (ejemplar == null) {
                return null;
            }
            // ejemplar: id, id_publicacion, num_ejemplar, estado, ubicacion
            int idPublicacion = ejemplar.getIdPublicacion();
            ObtenerPublicacionDetallesPorIdDTO detallesPub = publicacionDAO
                    .obtenerPublicacionDetallesPorId(idPublicacion);
            if (detallesPub == null) {
                return null;
            }

            // detallesPub: tipo, titulo, codigo_isbn, idioma, temas, modulos, ciclos,
            // editorial, num_edicion, fecha_publicacion, autores, periodicidad, id
            String tipo = detallesPub.getTipoPublicacion().name();
            String titulo = detallesPub.getTitulo();
            int numEdicion;
            try {
                numEdicion = Integer.parseInt(detallesPub.getNumEdicion());
            } catch (NumberFormatException e) {
                numEdicion = 0; // si no hay numEdicion, poner 0
            }
            resultado = new EjemplarConTituloDTO(
                    ejemplar.getId(),
                    ejemplar.getIdPublicacion(),
                    ejemplar.getNumEjemplar(),
                    ejemplar.getEstado(),
                    titulo,
                    numEdicion,
                    TipoPublicacion.valueOf(tipo));
        } catch (SQLException e) {
            System.out.println("Error al obtener conexión: " + e.getMessage());
            return null;
        }
        return resultado;
    }

    /**
     * Registra un préstamo con la lógica de negocio solicitada.
     * Devuelve null en caso de éxito, o mensaje de error en caso de fallo.
     *
     * @param idUsuario  el ID del usuario que realiza el préstamo
     * @param idEjemplar el ID del ejemplar a prestar
     * @return null si el préstamo se registró con éxito, o mensaje de error en caso
     *         de fallo
     */
    public String registrarPrestamo(final int idUsuario, final int idEjemplar) {
        try (Connection conexion = this.dbConnection.getConnection()) {
            boolean exito = false; // bandera para controlar commit/rollback
            try {
                if (conexion == null) {
                    return "No se puede obtener conexión a BD";
                }

                conexion.setAutoCommit(false); // Inicia transacción

                // 1. ejemplar existe y obtener info publicación
                EjemplarDAO ejemplarDAO = new EjemplarDAO(conexion);
                EstadoEjemplarDTO ejemplar = ejemplarDAO.obtenerEjemplarPorId(idEjemplar);
                if (ejemplar == null) {
                    return "Ejemplar no encontrado";
                }

                // 2. comprobar estado del ejemplar
                String estadoEjemplar = ejemplar.getEstado();
                if (!"DISPONIBLE".equalsIgnoreCase(estadoEjemplar)) {
                    return "El ejemplar esta dado de baja";
                }

                // 3. publicacion existe
                // Obtiene detalles de la publicación
                int idPublicacion = ejemplar.getIdPublicacion();
                PublicacionDAO publicacionDAO = new PublicacionDAO(conexion);
                ObtenerPublicacionDetallesPorIdDTO detallesPub = publicacionDAO
                        .obtenerPublicacionDetallesPorId(idPublicacion);
                if (detallesPub == null) {
                    return "Publicación no encontrada";
                }

                // 4. estado de la publicacion esta en true o false (activo o baja)
                if (!detallesPub.getEstado()) {
                    return "La publicación asociada al ejemplar está dada de baja";
                }

                String tipoPub = detallesPub.getTipoPublicacion().name(); // 'L' o 'R'

                // 5. Ejemplar no tiene prestamo activo
                // comprobar si el ejemplar ya tiene préstamo activo
                if (ejemplarDAO.tienePrestamosActivosEjemplar(idEjemplar)) {
                    return "El ejemplar ya tiene un préstamo activo";
                }

                // Validar usuario
                // Validaciones usuario
                UsuarioDAO usuarioDAO = new UsuarioDAO(conexion);
                UsuarioEstadoPorDNIOID usuario = usuarioDAO.obtenerUsuarioYEstadoPorDniOId(String.valueOf(idUsuario));
                // 6. comprobar usuario existe
                if (usuario == null) {
                    return "Usuario no encontrado";
                }

                // 7. comprobar usuario activo (no sancionado ni baja)
                String sancion = usuario.getSancionactiva();
                // comprobar sanciones o baja
                if ("SANCIONADO".equalsIgnoreCase(sancion) || "BAJA".equalsIgnoreCase(sancion)) {
                    return "El usuario tiene sanciones o está dado de baja";
                }

                // Ver reglas: libros -> 7 días para todos. Revistas -> solo 1 concedida (por
                // usuario) y durante el propio día,
                // pero si eres profesor son 7 días
                PrestamoDAO prestamoDAO = new PrestamoDAO(conexion);
                // comprobar revista activa por usuario
                if ("R".equalsIgnoreCase(tipoPub)) {
                    // comprobar si ya tiene revista en préstamo activo
                    if (prestamoDAO.tienePrestamoActivoTipo(idUsuario, 'R')) {
                        return "El usuario ya tiene una revista en préstamo activo";
                    }
                }

                // calcular fechas
                LocalDate hoy = LocalDate.now();
                LocalDate fin;
                // detectar tipo usuario (tipo desc está en usuario[4]) -> necesitamos code P o
                // no; mejor recuperar tipo directo
                // UsuarioDAO.obtenerUsuarioYEstadoPorDniOId no devuelve el código;
                // obtenerDetallesUsuario no da tipo en código form
                // Para obtener código de tipo, usar obtenerDetallesUsuario por id
                String[] detUsuario = usuarioDAO.obtenerDetallesUsuario(idUsuario);
                String tipoCode = detUsuario != null ? detUsuario[5] : null; // tipo (E,P,...)

                // aplicar reglas de fechas
                if ("R".equalsIgnoreCase(tipoPub)) {
                    if ("P".equalsIgnoreCase(tipoCode)) {
                        fin = hoy.plusDays(7);
                    } else {
                        fin = hoy; // mismo día
                    }
                } else {
                    // libros u otros -> 7 días
                    fin = hoy.plusDays(7);
                }

                // registrar préstamo
                boolean inserted = prestamoDAO.insertarPrestamo(idUsuario, idEjemplar, hoy,
                        fin);
                if (!inserted) {
                    return "Error registrando el préstamo en la base de datos";
                }
                conexion.commit(); // Confirmar transacción
                exito = true;
                return null; // null indica éxito
            } finally {
                // Restaurar auto-commit
                if (!exito && conexion != null) {
                    try {
                        conexion.rollback();
                        conexion.setAutoCommit(true);
                    } catch (SQLException ex) {
                        System.out
                                .println("Error al hacer rollback o al restablecer el auto-commit: " + ex.getMessage());
                    }
                }

            }

        } catch (SQLException e) {
            return "Error al obtener conexión a BD: " + e.getMessage();
        }

    }

}
