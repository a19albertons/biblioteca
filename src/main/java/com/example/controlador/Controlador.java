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
    /**
     * Controlador de recuperación de cuenta
     */
    ControladorRecuperarCuenta controladorRecuperarCuenta;
    /**
     * Controlador de login
     */
    ControladorLogin controladorLogin;
    /**
     * Controlador de panel de control
     */
    ControladorPanelControl controladorPanelControl;

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

    /**
     * Obtiene el controlador de recuperación de cuenta
     * 
     * @return
     */
    public ControladorRecuperarCuenta getControladorRecuperarCuenta() {
        return controladorRecuperarCuenta;
    }

    /**
     * Establece el controlador de recuperación de cuenta
     * 
     * @param controladorRecuperarCuenta
     */
    public void setControladorRecuperarCuenta(ControladorRecuperarCuenta controladorRecuperarCuenta) {
        this.controladorRecuperarCuenta = controladorRecuperarCuenta;
    }

    /**
     * Obtiene el controlador de login
     * 
     * @return
     */
    public ControladorLogin getControladorLogin() {
        return controladorLogin;
    }

    /**
     * Establece el controlador de login
     * 
     * @param controladorLogin
     */
    public void setControladorLogin(ControladorLogin controladorLogin) {
        this.controladorLogin = controladorLogin;
    }

    /**
     * Obtiene el controlador de panel de control
     * 
     * @return
     */
    public ControladorPanelControl getControladorPanelControl() {
        return controladorPanelControl;
    }

    /**
     * Establece el controlador de panel de control
     * 
     * @param controladorPanelControl
     */
    public void setControladorPanelControl(ControladorPanelControl controladorPanelControl) {
        this.controladorPanelControl = controladorPanelControl;
    }

    // Constructores
    /**
     * Constructor por defecto
     */
    public Controlador() {
        this.controladorInicioSesion = new ControladorInicioSesion();
        this.controladorRecuperarCuenta = new ControladorRecuperarCuenta();
        this.controladorLogin = new ControladorLogin();
        this.controladorPanelControl = new ControladorPanelControl();
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
