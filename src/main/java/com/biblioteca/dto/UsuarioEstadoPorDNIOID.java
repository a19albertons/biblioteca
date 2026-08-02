package com.biblioteca.dto;

import com.biblioteca.modelo.TipoUsuario;

/**
 * DTO para representar el estado de un usuario por DNI o ID
 */
public class UsuarioEstadoPorDNIOID {
    private int id;
    private String dni;
    private String nombreCompleto;
    private String SancionActiva;
    private TipoUsuario tipoUsuario;

    /**
     * Constructor para UsuarioEstadoPorDNIOID
     * @param id identificador del usuario
     * @param dni DNI del usuario
     * @param nombreCompleto nombre completo del usuario
     * @param sancionActiva estado de sanción del usuario
     * @param tipoUsuario tipo de usuario (ej. Estudiante, Profesor, etc.)
     */
    public UsuarioEstadoPorDNIOID(int id, String dni, String nombreCompleto, String sancionActiva,
            TipoUsuario tipoUsuario) {
        this.id = id;
        this.dni = dni;
        this.nombreCompleto = nombreCompleto;
        SancionActiva = sancionActiva;
        this.tipoUsuario = tipoUsuario;
    }

    /**
     * Getter para el ID del usuario
     * @return id del usuario
     */
    public int getId() {
        return id;
    }

    /**
     * Getter para el DNI del usuario
     * @return DNI del usuario
     */
    public String getDni() {
        return dni;
    }

    /**
     * Getter para el nombre completo del usuario
     * @return nombre completo del usuario
     */
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    /**
     * Getter para el estado de sanción del usuario
     * @return estado de sanción del usuario (SANCIONADO/ACTIVO/BAJA)
     */
    public String getSancionActiva() {
        return SancionActiva;
    }

    /**
     * Getter para el tipo de usuario
     * @return tipo de usuario (ej. Estudiante, Profesor, etc.)
     */
    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    

}
