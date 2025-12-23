package com.example.modelo;

public enum TipoUsuario {
    E("Estudiante"), // Estudiante
    P("Profesor"), // Profesor
    A("Administrativo"), // Administrativo
    C("Conserje"), // Conserje
    L("Limpiador"); // Limpiador

    /**
     * Descripción del tipo de usuario
     */
    private final String descripcion;

    /**
     * Constructor del tipo de usuario
     * 
     * @param descripcion
     */
    private TipoUsuario(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene la descripción del tipo de usuario
     * 
     * @return
     */
    public String getDescripcion() {
        return descripcion;
    }
}
