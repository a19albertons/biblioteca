package com.biblioteca.utilities;

public enum RelacionPublicacionHelperEnum {
    /**
     * Relación de publicación con módulos
     */
    MODULO("M"),
    /**
     * Relación de publicación con ciclos
     */
    CICLO("C"),
    /**
     * Relación de publicación con temas
     */
    TEMA("T");

    /**
     * Código de la relación
     */
    private final String codigo;

    /**
     * Constructor de la relación
     *
     * @param codigo código de la relación
     */
    RelacionPublicacionHelperEnum(final String codigo) {
        this.codigo = codigo;
    }

    /**
     * Obtiene el código de la relación
     *
     * @return el código de la relación
     */
    public String getCodigo() {
        return codigo;
    }
}
