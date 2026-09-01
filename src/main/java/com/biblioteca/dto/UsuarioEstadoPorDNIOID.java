package com.biblioteca.dto;

import com.biblioteca.modelo.TipoUsuario;

/**
 * DTO para representar el estado de un usuario por DNI o ID
 */
public class UsuarioEstadoPorDNIOID {
    /**
     * Identificador del usuario
     */
    private final int id;
    /**
     * DNI del usuario
     */
    private final String dni;
    /**
     * Nombre completo del usuario
     */
    private final String nombreCompleto;
    /**
     * Estado de sanción del usuario
     */
    private final String sancionactiva;
    /**
     * Tipo de usuario (ej. Estudiante, Profesor, etc.)
     */
    private final TipoUsuario tipoUsuario;

    /**
     * Constructor para UsuarioEstadoPorDNIOID
     * 
     * @param id             identificador del usuario
     * @param dni            DNI del usuario
     * @param nombreCompleto nombre completo del usuario
     * @param sancionActiva  estado de sanción del usuario
     * @param tipoUsuario    tipo de usuario (ej. Estudiante, Profesor, etc.)
     */
    public UsuarioEstadoPorDNIOID(final int id, final String dni, final String nombreCompleto,
            final String sancionActiva,
            final TipoUsuario tipoUsuario) {
        this.id = id;
        this.dni = dni;
        this.nombreCompleto = nombreCompleto;
        this.sancionactiva = sancionActiva;
        this.tipoUsuario = tipoUsuario;
    }

    /**
     * Getter para el ID del usuario
     * 
     * @return id del usuario
     */
    public int getId() {
        return id;
    }

    /**
     * Getter para el DNI del usuario
     * 
     * @return DNI del usuario
     */
    public String getDni() {
        return dni;
    }

    /**
     * Getter para el nombre completo del usuario
     * 
     * @return nombre completo del usuario
     */
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    /**
     * Getter para el estado de sanción del usuario
     * 
     * @return estado de sanción del usuario (SANCIONADO/ACTIVO/BAJA)
     */
    public String getSancionactiva() {
        return sancionactiva;
    }

    /**
     * Getter para el tipo de usuario
     * 
     * @return tipo de usuario (ej. Estudiante, Profesor, etc.)
     */
    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }
}
