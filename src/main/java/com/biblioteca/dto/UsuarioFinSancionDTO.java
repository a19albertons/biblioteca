package com.biblioteca.dto;

/**
 * DTO para transferir información de la sanción activa de un usuario
 */
public class UsuarioFinSancionDTO {
    private int idSancion;
    private String finSancion;

    /**
     * Constructor de UsuarioFinSancionDTO
     * 
     * @param idSancion  id de la sanción
     * @param finSancion fecha de fin de la sanción
     */
    public UsuarioFinSancionDTO(int idSancion, String finSancion) {
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
