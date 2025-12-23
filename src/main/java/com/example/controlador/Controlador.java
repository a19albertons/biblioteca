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
    /**
     * Controlador de inicio de sesión
     */
    ControladorInicioSesion controladorInicioSesion;

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

    /**
     * Obtiene el controlador de inicio de sesión
     * 
     * @return
     */
    public ControladorInicioSesion getControladorInicioSesion() {
        return controladorInicioSesion;
    }

    /**
     * Establece el controlador de inicio de sesión
     * 
     * @param controladorInicioSesion
     */
    public void setControladorInicioSesion(ControladorInicioSesion controladorInicioSesion) {
        this.controladorInicioSesion = controladorInicioSesion;
    }

    // Constructores
    /**
     * Constructor por defecto
     */
    public Controlador() {
        this.controladorNavegacion = new ControladorNavegacion(this);
        this.controladorInicioSesion = new ControladorInicioSesion();
    }

    // Metodos del controlador
    /**
     * Inicia la aplicación
     */
    public void iniciarAplicacion() {
        controladorNavegacion.mostrarVentana();
    }

}
