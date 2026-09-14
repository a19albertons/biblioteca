package com.biblioteca.modelo;

public enum TipoUsuario {
    /**
     * Tipo de usuario Estudiante
     */
    E("Estudiante"), // Estudiante
    /**
     * Tipo de usuario Profesor
     */
    P("Profesor"), // Profesor
    /**
     * Tipo de usuario Administrativo
     */
    A("Administrativo"), // Administrativo
    /**
     * Tipo de usuario Conserje
     */
    C("Conserje"), // Conserje
    /**
     * Tipo de usuario Limpiador
     */
    L("Limpiador"); // Limpiador

    /**
     * Descripción del tipo de usuario
     */
    private final String descripcion;

    /**
     * Constructor del tipo de usuario
     * 
     * @param descripcion descripción del tipo de usuario
     */
    TipoUsuario(final String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene la descripción del tipo de usuario
     * 
     * @return la descripción del tipo de usuario
     */
    public String getDescripcion() {
        return descripcion;
    }
}
