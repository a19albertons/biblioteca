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
     * Controlador de concesión de préstamos
     */
    ControladorConcederPrestamo controladorConcederPrestamo;
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
    ControladorEditarPublicacionDialog controladorEditarPublicacionDialog;    
    /**
     * Controlador para eliminar publicaciones (diálogo de eliminación)
     */
    ControladorEliminarPublicacion controladorEliminarPublicacion;
    /**
     * Controlador para el diálogo de nuevo ejemplar
     */
    ControladorNuevoEjemplarDialog controladorNuevoEjemplarDialog;
    /**
     * Controlador de edición de ejemplares (diálogo)
     */
    ControladorEditarEjemplarDialog controladorEditarEjemplarDialog;
    /**
     * Controlador para eliminar ejemplares (diálogo)
     */
    ControladorEliminarEjemplarDialog controladorEliminarEjemplarDialog;
    /**
     * Controlador de gestión de usuarios
     */
    ControladorGestionUsuarios controladorGestionUsuarios;
    /**
     * Controlador para el diálogo de nuevo usuario
     */
    ControladorNuevoUsuarioDialog controladorNuevoUsuarioDialog;

    /**
     * Controlador para editar usuarios
     */
    ControladorEditarUsuarioDialog controladorEditarUsuarioDialog;

    /**
     * Controlador para eliminar (desactivar) usuarios
     */
    ControladorEliminarUsuario controladorEliminarUsuario;

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
     * Obtiene el controlador de concesión de préstamos
     *
     * @return controlador de conceder préstamo
     */
    public ControladorConcederPrestamo getControladorConcederPrestamo() {
        return controladorConcederPrestamo;
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
     * Obtiene el controlador encargado de crear nuevos usuarios desde el diálogo.
     *
     * @return controlador de nuevo usuario
     */
    public ControladorNuevoUsuarioDialog getControladorNuevoUsuarioDialog() {
        return controladorNuevoUsuarioDialog;
    }

    /**
     * Obtiene el controlador de edición de usuarios
     * @return controlador de edición de usuarios
     */
    public ControladorEditarUsuarioDialog getControladorEditarUsuarioDialog() {
        return controladorEditarUsuarioDialog;
    }

    /**
     * Obtiene el controlador de eliminación (desactivar) de usuarios
     * @return controlador de eliminación de usuarios
     */
    public ControladorEliminarUsuario getControladorEliminarUsuario() {
        return controladorEliminarUsuario;
    }

    /**
     * Establece el controlador encargado de crear nuevos ejemplares.
     *
     * @param controladorNuevoEjemplarDialog
     */
    public void setControladorNuevoEjemplarDialog(ControladorNuevoEjemplarDialog controladorNuevoEjemplarDialog) {
        this.controladorNuevoEjemplarDialog = controladorNuevoEjemplarDialog;
    }

    /**
     * Obtiene el controlador encargado de editar ejemplares desde el diálogo.
     *
     * @return controlador de edición de ejemplares
     */
    public ControladorEditarEjemplarDialog getControladorEditarEjemplarDialog() {
        return controladorEditarEjemplarDialog;
    }

    /**
     * Establece el controlador encargado de editar ejemplares.
     *
     * @param controladorEditarEjemplarDialog
     */
    public void setControladorEditarEjemplarDialog(ControladorEditarEjemplarDialog controladorEditarEjemplarDialog) {
        this.controladorEditarEjemplarDialog = controladorEditarEjemplarDialog;
    }

    /**
     * Obtiene el controlador encargado de eliminar ejemplares desde el diálogo.
     *
     * @return controlador de eliminar ejemplar
     */
    public ControladorEliminarEjemplarDialog getControladorEliminarEjemplarDialog() {
        return controladorEliminarEjemplarDialog;
    }

    /**
     * Establece el controlador encargado de eliminar ejemplares.
     *
     * @param controladorEliminarEjemplarDialog
     */
    public void setControladorEliminarEjemplarDialog(ControladorEliminarEjemplarDialog controladorEliminarEjemplarDialog) {
        this.controladorEliminarEjemplarDialog = controladorEliminarEjemplarDialog;
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
        this.controladorEditarEjemplarDialog = new ControladorEditarEjemplarDialog(this);
        this.controladorEliminarEjemplarDialog = new ControladorEliminarEjemplarDialog(this);
        this.controladorNuevoUsuarioDialog = new ControladorNuevoUsuarioDialog(this);
        this.controladorEditarUsuarioDialog = new ControladorEditarUsuarioDialog(this);
        this.controladorEliminarUsuario = new ControladorEliminarUsuario(this);
        this.controladorNavegacion = new ControladorNavegacion(this);
        this.controladorConcederPrestamo = new ControladorConcederPrestamo(this);
        
    }

    // Metodos del controlador
    /**
     * Inicia la aplicación
     */
    public void iniciarAplicacion() {
        controladorNavegacion.mostrarVentana();
    }

}
