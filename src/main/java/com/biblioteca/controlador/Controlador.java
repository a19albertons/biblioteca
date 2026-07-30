package com.biblioteca.controlador;

import com.biblioteca.conexiones.DBConnection;
import com.biblioteca.vista.navegación.ControladorNavegacion;

/**
 * Controlador principal de la aplicación
 */
public class Controlador {
    // Atributos del controlador
    /**
     * Conexión compartida para DAOs/controladores (inyección)
     */
    private final DBConnection dbConnection;

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
     * Controlador de devolución de préstamos
     */
    ControladorDevolverPrestamo controladorDevolverPrestamo;
    /**
     * Controlador de ejemplares
     */
    ControladorEjemplares controladorEjemplares;
    /**
     * Controlador de nueva publicación (encapsula la lógica de persistencia de
     * publicaciones)
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

    /**
     * Controlador para sanciones manuales
     */
    ControladorSancionManual controladorSancionManual;

    // Getters y Setters

    /**
     * Obtiene la conexión compartida
     *
     * @return DBConnection usada por este controlador
     */
    public DBConnection getDbConnection() {
        return this.dbConnection;
    }

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
     * Obtiene el controlador de devolución de préstamos
     *
     * @return controlador de devolución
     */
    public ControladorDevolverPrestamo getControladorDevolverPrestamo() {
        return controladorDevolverPrestamo;
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
    public void setControladorNuevaPublicacionDialog(
            ControladorNuevaPublicacionDialog controladorNuevaPublicacionDialog) {
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
    public void setControladorEditarPublicacionDialog(
            ControladorEditarPublicacionDialog controladorEditarPublicacionDialog) {
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
     * 
     * @return controlador de edición de usuarios
     */
    public ControladorEditarUsuarioDialog getControladorEditarUsuarioDialog() {
        return controladorEditarUsuarioDialog;
    }

    /**
     * Obtiene el controlador de eliminación (desactivar) de usuarios
     * 
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
    public void setControladorEliminarEjemplarDialog(
            ControladorEliminarEjemplarDialog controladorEliminarEjemplarDialog) {
        this.controladorEliminarEjemplarDialog = controladorEliminarEjemplarDialog;
    }

    /**
     * Obtiene el controlador encargado de la gestión de sanciones manuales
     *
     * @return controlador de sancion manual
     */
    public ControladorSancionManual getControladorSancionManual() {
        return controladorSancionManual;
    }

    /**
     * Establece el controlador encargado de la gestión de sanciones manuales
     *
     * @param controladorSancionManual
     */
    public void setControladorSancionManual(ControladorSancionManual controladorSancionManual) {
        this.controladorSancionManual = controladorSancionManual;
    }
    /**
     * Constructor que acepta una conexión (DBConnection) y la pasa a los
     * controladores que la requieren.
     *
     * @param dbConnection implementación de DBConnection (no puede ser null)
     */
    public Controlador(DBConnection dbConnection) {
        if (dbConnection == null) {
            throw new IllegalArgumentException("DBConnection cannot be null");
        }
        this.dbConnection = dbConnection;
        this.controladorInicioSesion = new ControladorInicioSesion(dbConnection);
        this.controladorRecuperarCuenta = new ControladorRecuperarCuenta(dbConnection);
        this.controladorLogin = new ControladorLogin();
        this.controladorPanelControl = new ControladorPanelControl(dbConnection);
        this.controladorEjemplares = new ControladorEjemplares(dbConnection);
        this.controladorGestionUsuarios = new ControladorGestionUsuarios(dbConnection);
        this.controladorNuevaPublicacionDialog = new ControladorNuevaPublicacionDialog(dbConnection);
        this.controladorEditarPublicacionDialog = new ControladorEditarPublicacionDialog(dbConnection);
        this.controladorEliminarPublicacion = new ControladorEliminarPublicacion(dbConnection);
        this.controladorNuevoEjemplarDialog = new ControladorNuevoEjemplarDialog(dbConnection);
        this.controladorEditarEjemplarDialog = new ControladorEditarEjemplarDialog(dbConnection);
        this.controladorEliminarEjemplarDialog = new ControladorEliminarEjemplarDialog(dbConnection);
        this.controladorNuevoUsuarioDialog = new ControladorNuevoUsuarioDialog(dbConnection);
        this.controladorEditarUsuarioDialog = new ControladorEditarUsuarioDialog(dbConnection);
        this.controladorEliminarUsuario = new ControladorEliminarUsuario(dbConnection);
        this.controladorSancionManual = new ControladorSancionManual(dbConnection);
        this.controladorNavegacion = new ControladorNavegacion(this);
        this.controladorConcederPrestamo = new ControladorConcederPrestamo(dbConnection);
        this.controladorDevolverPrestamo = new ControladorDevolverPrestamo(dbConnection);
    }

    // Metodos del controlador


    /**
     * Inicia la aplicación
     */
    public void iniciarAplicacion() {
        controladorNavegacion.mostrarVentana();
    }

}
