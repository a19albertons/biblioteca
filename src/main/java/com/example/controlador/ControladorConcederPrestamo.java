package com.example.controlador;

import java.sql.Date;
import java.time.LocalDate;

import com.example.dao.EjemplarDAO;
import com.example.dao.PublicacionDAO;
import com.example.dao.PrestamoDAO;
import com.example.dao.UsuarioDAO;

/**
 * Controlador para la lógica de concesión de préstamos
 */
public class ControladorConcederPrestamo {
    /**
     * Controlador principal de la aplicación
     */
    private Controlador controlador;

    /**
     * Constructor
     * 
     * @param controlador
     */
    public ControladorConcederPrestamo(Controlador controlador) {
        this.controlador = controlador;
    }

    /**
     * Busca el usuario por DNI o ID y devuelve arreglo: id, dni, nombre_completo,
     * sancion_activa (SANCIONADO/ACTIVO/BAJA), tipo_desc
     */
    public String[] buscarUsuarioPorDniOId(String dniOrId) {
        UsuarioDAO dao = new UsuarioDAO();
        return dao.obtenerUsuarioYEstadoPorDniOId(dniOrId == null ? "" : dniOrId.trim());
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
        // ejemplar: id, id_publicacion, num_ejemplar, estado, ubicacion
        int idPublicacion = Integer.parseInt(ejemplar[1]);
        String[] detallesPub = publicacionDAO.obtenerPublicacionDetallesPorId(idPublicacion);
        if (detallesPub == null) {
            return null;
        }
        // detallesPub: tipo, titulo, codigo_isbn, idioma, temas, modulos, ciclos,
        // editorial, num_edicion, fecha_publicacion, autores, periodicidad, id
        String tipo = detallesPub[0];
        String titulo = detallesPub[1];
        String numEdicion = detallesPub[8];
        String[] resultado = new String[] { ejemplar[0], ejemplar[1], ejemplar[2], ejemplar[4], titulo,
                (numEdicion == null ? "" : numEdicion), (tipo == null ? "" : tipo) };
        return resultado;
    }

    /**
     * Registra un préstamo con la lógica de negocio solicitada.
     * Devuelve null en caso de éxito, o mensaje de error en caso de fallo.
     */
    public String registrarPrestamo(int idUsuario, int idEjemplar) {
        // Validaciones
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        String[] usuario = usuarioDAO.obtenerUsuarioYEstadoPorDniOId(String.valueOf(idUsuario));
        // comprobar usuario válido
        if (usuario == null) {
            return "Usuario no encontrado";
        }
        String sancion = usuario[3];
        // comprobar sanciones o baja
        if ("SANCIONADO".equalsIgnoreCase(sancion) || "BAJA".equalsIgnoreCase(sancion)) {
            return "El usuario tiene sanciones o está dado de baja";
        }

        EjemplarDAO ejemplarDAO = new EjemplarDAO();
        // comprobar si el ejemplar ya tiene préstamo activo
        if (ejemplarDAO.tienePrestamosActivosEjemplar(idEjemplar)) {
            return "El ejemplar ya tiene un préstamo activo";
        }

        // Obtener info publicación
        String[] ejemplar = ejemplarDAO.obtenerEjemplarPorId(idEjemplar);
        if (ejemplar == null) {
            return "Ejemplar no encontrado";
        }
        
        // Obtiene detalles de la publicación
        int idPublicacion = Integer.parseInt(ejemplar[1]);
        PublicacionDAO publicacionDAO = new PublicacionDAO();
        String[] detallesPub = publicacionDAO.obtenerPublicacionDetallesPorId(idPublicacion);
        if (detallesPub == null) {
            return "Publicación no encontrada";
        }
        String tipoPub = detallesPub[0]; // 'L' o 'R'

        // Ver reglas: libros -> 7 días para todos. Revistas -> solo 1 concedida (por
        // usuario) y durante el propio día,
        // pero si eres profesor son 7 días
        PrestamoDAO prestamoDAO = new PrestamoDAO();
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
        boolean inserted = prestamoDAO.insertarPrestamo(idUsuario, idEjemplar, Date.valueOf(hoy), Date.valueOf(fin));
        if (!inserted) {
            return "Error registrando el préstamo en la base de datos";
        }
        return null; // null indica éxito
    }

}
