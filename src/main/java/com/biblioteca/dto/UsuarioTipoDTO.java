package com.biblioteca.dto;

import com.biblioteca.modelo.TipoUsuario;

/**
 * DTO para transferir información del tipo de usuario
 */
public class UsuarioTipoDTO {
    private int idUsuario;
    private TipoUsuario tipoUsuario;

    /**
     * Constructor de UsuarioTipoDTO
     * @param idUsuario id del usuario
     * @param tipoUsuario tipo de usuario
     */
    public UsuarioTipoDTO(int idUsuario, TipoUsuario tipoUsuario) {
        this.idUsuario = idUsuario;
        this.tipoUsuario = tipoUsuario;
    }

    /**
     * Obtiene el id del usuario
     * @return obtiene el id del usuario
     */
    public int getIdUsuario() {
        return idUsuario;
    }

    /**
     * Obtiene el tipo de usuario
     * @return obtiene el tipo de usuario
     */
    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    
}
