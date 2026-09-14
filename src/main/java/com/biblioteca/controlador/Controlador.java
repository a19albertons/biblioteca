package com.biblioteca.controlador;

import javax.annotation.Nonnull;

import com.biblioteca.conexiones.DBConnection;
import com.biblioteca.vista.navegacion.ControladorNavegacion;

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
     *
     * @return controlador de navegación
     */
    private final ControladorNavegacion controladorNavegacion;
    /**
     * Controlador de inicio de sesión
     *
     * @return controlador de inicio de sesión
     */
    private final ControladorInicioSesion controladorInicioSesion;
    /**
     * Controlador de recuperación de cuenta
     *
     * @return controlador de recuperación de cuenta
     */
    private final ControladorRecuperarCuenta controladorRecuperarCuenta;
    /**
     * Controlador de login
     *
     * @return controlador de login
     */
    private final ControladorLogin controladorLogin;
    /**
     * Controlador de panel de control
     *
     * @return controlador de panel de control
     */
    private final ControladorPanelControl controladorPanelControl;

    /**
     * Controlador de concesión de préstamos
     *
     * @return controlador de concesión de préstamos
     */
    private final ControladorConcederPrestamo controladorConcederPrestamo;
    /**
     * Controlador de devolución de préstamos
     *
     * @return controlador de devolución de préstamos
     */
    private final ControladorDevolverPrestamo controladorDevolverPrestamo;
    /**
     * Controlador de ejemplares
     *
     * @return controlador de ejemplares
     */
    private final ControladorEjemplares controladorEjemplares;
    /**
     * Controlador de nueva publicación (encapsula la lógica de persistencia de
     * publicaciones)
     *
     * @return controlador de nueva publicación
     */
    private final ControladorNuevaPublicacionDialog controladorNuevaPublicacionDialog;
    /**
     * Controlador para editar publicaciones (diálogo de edición)
     *
     * @return controlador para editar publicaciones
     */
    private final ControladorEditarPublicacionDialog controladorEditarPublicacionDialog;
    /**
     * Controlador para eliminar publicaciones (diálogo de eliminación)
     *
     * @return controlador para eliminar publicaciones
     */
    private final ControladorEliminarPublicacion controladorEliminarPublicacion;
    /**
     * Controlador para el diálogo de nuevo ejemplar
     *
     * @return controlador para el diálogo de nuevo ejemplar
     */
    private final ControladorNuevoEjemplarDialog controladorNuevoEjemplarDialog;
    /**
     * Controlador de edición de ejemplares (diálogo)
     *
     * @return controlador de edición de ejemplares
     */
    private final ControladorEditarEjemplarDialog controladorEditarEjemplarDialog;
    /**
     * Controlador para eliminar ejemplares (diálogo)
     *
     * @return controlador para eliminar ejemplares
     */
    private final ControladorEliminarEjemplarDialog controladorEliminarEjemplarDialog;
    /**
     * Controlador de gestión de usuarios
     *
     * @return controlador de gestión de usuarios
     */
    private final ControladorGestionUsuarios controladorGestionUsuarios;
    /**
     * Controlador para el diálogo de nuevo usuario
     *
     * @return controlador para el diálogo de nuevo usuario
     */
    private final ControladorNuevoUsuarioDialog controladorNuevoUsuarioDialog;

    /**
     * Controlador para editar usuarios
     *
     * @return controlador para editar usuarios
     */
    private final ControladorEditarUsuarioDialog controladorEditarUsuarioDialog;

    /**
     * Controlador para eliminar (desactivar) usuarios
     *
     * @return controlador para eliminar usuarios
     */
    private final ControladorEliminarUsuario controladorEliminarUsuario;

    /**
     * Controlador para sanciones manuales
     *
     * @return controlador para sanciones manuales
     */
    private final ControladorSancionManual controladorSancionManual;

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
     * @return controlador de navegación
     */
    public final ControladorNavegacion getControladorNavegacion() {
        return controladorNavegacion;
    }

    /**
     * Obtiene el controlador de inicio de sesión
     *
     * @return controlador de inicio de sesión
     */
    public final ControladorInicioSesion getControladorInicioSesion() {
        return controladorInicioSesion;
    }

    /**
     * Obtiene el controlador de recuperación de cuenta
     *
     * @return controlador de recuperación de cuenta
     */
    public final ControladorRecuperarCuenta getControladorRecuperarCuenta() {
        return controladorRecuperarCuenta;
    }

    /**
     * Obtiene el controlador de login
     *
     * @return controlador de login
     */
    public final ControladorLogin getControladorLogin() {
        return controladorLogin;
    }

    /**
     * Obtiene el controlador de panel de control
     *
     * @return controlador de panel de control
     */
    public final ControladorPanelControl getControladorPanelControl() {
        return controladorPanelControl;
    }

    /**
     * Obtiene el controlador de concesión de préstamos
     *
     * @return controlador de conceder préstamo
     */
    public final ControladorConcederPrestamo getControladorConcederPrestamo() {
        return controladorConcederPrestamo;
    }

    /**
     * Obtiene el controlador de devolución de préstamos
     *
     * @return controlador de devolución
     */
    public final ControladorDevolverPrestamo getControladorDevolverPrestamo() {
        return controladorDevolverPrestamo;
    }

    /**
     * Obtiene el controlador de ejemplares
     *
     * @return controlador de ejemplares
     */
    public final ControladorEjemplares getControladorEjemplares() {
        return controladorEjemplares;
    }

    /**
     * Obtiene el controlador de gestión de usuarios
     *
     * @return controlador de gestión de usuarios
     */
    public final ControladorGestionUsuarios getControladorGestionUsuarios() {
        return controladorGestionUsuarios;
    }

    /**
     * Obtiene el controlador de nueva publicación
     *
     * @return controlador de nueva publicación
     */
    public final ControladorNuevaPublicacionDialog getControladorNuevaPublicacionDialog() {
        return controladorNuevaPublicacionDialog;
    }

    /**
     * Obtiene el controlador encargado de la edición de publicaciones.
     *
     * @return controlador de edición de publicaciones
     */
    public final ControladorEditarPublicacionDialog getControladorEditarPublicacionDialog() {
        return controladorEditarPublicacionDialog;
    }

    /**
     * Obtiene el controlador encargado de eliminar publicaciones.
     *
     * @return controlador de eliminación de publicaciones
     */
    public final ControladorEliminarPublicacion getControladorEliminarPublicacion() {
        return controladorEliminarPublicacion;
    }

    /**
     * Obtiene el controlador encargado de crear nuevos ejemplares desde el diálogo.
     *
     * @return controlador de nuevo ejemplar
     */
    public final ControladorNuevoEjemplarDialog getControladorNuevoEjemplarDialog() {
        return controladorNuevoEjemplarDialog;
    }

    /**
     * Obtiene el controlador encargado de crear nuevos usuarios desde el diálogo.
     *
     * @return controlador de nuevo usuario
     */
    public final ControladorNuevoUsuarioDialog getControladorNuevoUsuarioDialog() {
        return controladorNuevoUsuarioDialog;
    }

    /**
     * Obtiene el controlador de edición de usuarios
     *
     * @return controlador de edición de usuarios
     */
    public final ControladorEditarUsuarioDialog getControladorEditarUsuarioDialog() {
        return controladorEditarUsuarioDialog;
    }

    /**
     * Obtiene el controlador de eliminación (desactivar) de usuarios
     *
     * @return controlador de eliminación de usuarios
     */
    public final ControladorEliminarUsuario getControladorEliminarUsuario() {
        return controladorEliminarUsuario;
    }

    /**
     * Obtiene el controlador encargado de editar ejemplares desde el diálogo.
     *
     * @return controlador de edición de ejemplares
     */
    public final ControladorEditarEjemplarDialog getControladorEditarEjemplarDialog() {
        return controladorEditarEjemplarDialog;
    }

    /**
     * Obtiene el controlador encargado de eliminar ejemplares desde el diálogo.
     *
     * @return controlador de eliminar ejemplares
     */
    public final ControladorEliminarEjemplarDialog getControladorEliminarEjemplarDialog() {
        return controladorEliminarEjemplarDialog;
    }

    /**
     * Obtiene el controlador encargado de la gestión de sanciones manuales
     *
     * @return controlador de gestión de sanciones manuales
     */
    public final ControladorSancionManual getControladorSancionManual() {
        return controladorSancionManual;
    }

    /**
     * Constructor que acepta una conexión (DBConnection) y la pasa a los
     * controladores que la requieren.
     *
     * @param dbConnection implementación de DBConnection (no puede ser null)
     */
    public Controlador(@Nonnull final DBConnection dbConnection) {
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
