package com.example.controlador;

/**
 * Controlador principal de la aplicación
 */
public class Controlador {
    // Atributos del controlador
    /**
     * Controlador de navegación
     */
    ControladorNavegacion controladorNavegacion;

    // Getters y Setters
    /**
     * Obtiene el controlador de navegación
     * 
     * @return
     */
    public ControladorNavegacion getControladorNavegacion() {
        return controladorNavegacion;
    }

    /**
     * Establece el controlador de navegación
     * 
     * @param controladorNavegacion
     */
    public void setControladorNavegacion(ControladorNavegacion controladorNavegacion) {
        this.controladorNavegacion = controladorNavegacion;
    }

    // Constructores
    /**
     * Constructor por defecto
     */
    public Controlador() {
        this.controladorNavegacion = new ControladorNavegacion(this);
    }

    // Metodos del controlador
    /**
     * Inicia la aplicación
     */
    public void iniciarAplicacion() {
        controladorNavegacion.mostrarVentana();
    }

}
