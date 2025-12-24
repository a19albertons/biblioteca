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
    /**
     * Controlador de ejemplares
     */
    ControladorEjemplares controladorEjemplares;
    /**
     * Controlador de nueva publicación (encapsula la lógica de persistencia de publicaciones)
     */
    ControladorNuevaPublicacionDialog controladorNuevaPublicacionDialog;
    /**
     * Controlador para editar publicaciones (diálogo de edición)
     */
    ControladorEditarPublicacionDialog controladorEditarPublicacionDialog;    /**
     * Controlador para eliminar publicaciones (diálogo de eliminación)
     */
    ControladorEliminarPublicacion controladorEliminarPublicacion;
    /**
     * Controlador para el diálogo de nuevo ejemplar
     */
    ControladorNuevoEjemplarDialog controladorNuevoEjemplarDialog;
    /**
     * Controlador de gestión de usuarios
     */
    ControladorGestionUsuarios controladorGestionUsuarios = new ControladorGestionUsuarios();

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
    /**
     * Obtiene el controlador de ejemplares
     * 
     * @return
     */ 
    public ControladorEjemplares getControladorEjemplares() {
        return controladorEjemplares;
    }
    /**
     * Establece el controlador de ejemplares
     * 
     * @param controladorEjemplares
     */
    public void setControladorEjemplares(ControladorEjemplares controladorEjemplares) {
        this.controladorEjemplares = controladorEjemplares;
    }
    /**
     * Obtiene el controlador de gestión de usuarios
     * 
     * @return
     */
    public ControladorGestionUsuarios getControladorGestionUsuarios() {
        return controladorGestionUsuarios;
    }
    /**
     * Establece el controlador de gestión de usuarios
     * 
     * @param controladorGestionUsuarios
     */
    public void setControladorGestionUsuarios(ControladorGestionUsuarios controladorGestionUsuarios) {
        this.controladorGestionUsuarios = controladorGestionUsuarios;
    }

    /**
     * Obtiene el controlador de nueva publicación
     *
     * @return controlador de nueva publicación
     */
    public ControladorNuevaPublicacionDialog getControladorNuevaPublicacionDialog() {
        return controladorNuevaPublicacionDialog;
    }

    /**
     * Establece el controlador de nueva publicación
     *
     * @param controladorNuevaPublicacionDialog
     */
    public void setControladorNuevaPublicacionDialog(ControladorNuevaPublicacionDialog controladorNuevaPublicacionDialog) {
        this.controladorNuevaPublicacionDialog = controladorNuevaPublicacionDialog;
    }

    /**
     * Obtiene el controlador encargado de la edición de publicaciones.
     *
     * @return controlador de edición de publicaciones
     */
    public ControladorEditarPublicacionDialog getControladorEditarPublicacionDialog() {
        return controladorEditarPublicacionDialog;
    }

    /**
     * Establece el controlador encargado de la edición de publicaciones.
     *
     * @param controladorEditarPublicacionDialog controlador de edición
     */
    public void setControladorEditarPublicacionDialog(ControladorEditarPublicacionDialog controladorEditarPublicacionDialog) {
        this.controladorEditarPublicacionDialog = controladorEditarPublicacionDialog;
    }

    /**
     * Obtiene el controlador encargado de eliminar publicaciones.
     *
     * @return controlador de eliminación de publicaciones
     */
    public ControladorEliminarPublicacion getControladorEliminarPublicacion() {
        return controladorEliminarPublicacion;
    }

    /**
     * Establece el controlador encargado de eliminar publicaciones.
     *
     * @param controladorEliminarPublicacion controlador de eliminación
     */
    public void setControladorEliminarPublicacion(ControladorEliminarPublicacion controladorEliminarPublicacion) {
        this.controladorEliminarPublicacion = controladorEliminarPublicacion;
    }

    /**
     * Obtiene el controlador encargado de crear nuevos ejemplares desde el diálogo.
     *
     * @return controlador de nuevo ejemplar
     */
    public ControladorNuevoEjemplarDialog getControladorNuevoEjemplarDialog() {
        return controladorNuevoEjemplarDialog;
    }

    /**
     * Establece el controlador encargado de crear nuevos ejemplares.
     *
     * @param controladorNuevoEjemplarDialog
     */
    public void setControladorNuevoEjemplarDialog(ControladorNuevoEjemplarDialog controladorNuevoEjemplarDialog) {
        this.controladorNuevoEjemplarDialog = controladorNuevoEjemplarDialog;
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
        this.controladorEjemplares = new ControladorEjemplares();
        this.controladorGestionUsuarios = new ControladorGestionUsuarios();
        this.controladorNuevaPublicacionDialog = new ControladorNuevaPublicacionDialog(this);
        this.controladorEditarPublicacionDialog = new ControladorEditarPublicacionDialog(this);
        this.controladorEliminarPublicacion = new ControladorEliminarPublicacion(this);
        this.controladorNuevoEjemplarDialog = new ControladorNuevoEjemplarDialog(this);
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
