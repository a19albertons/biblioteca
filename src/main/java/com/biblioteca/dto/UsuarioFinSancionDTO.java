package com.biblioteca.dto;

/**
 * DTO para transferir información de la sanción activa de un usuario
 */
public class UsuarioFinSancionDTO {
    /**
     * Id de la sanción
     */
    private int idSancion;
    /**
     * Fecha de fin de la sanción
     */
    private String finSancion;

    /**
     * Constructor de UsuarioFinSancionDTO
     * 
     * @param idSancion  id de la sanción
     * @param finSancion fecha de fin de la sanción
     */
    public UsuarioFinSancionDTO(final int idSancion, final String finSancion) {
        this.idSancion = idSancion;
        this.finSancion = finSancion;
    }

    /**
     * Obtiene el id de la sanción
     * 
     * @return id de la sanción
     */
    public int getIdSancion() {
        return idSancion;
    }

    /**
     * Obtiene la fecha de fin de la sanción
     * 
     * @return fecha de fin de la sanción
     */
    public String getFinSancion() {
        return finSancion;
    }

}
