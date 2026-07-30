package com.biblioteca.vista.navegación;

import java.awt.CardLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.biblioteca.conexiones.DBConnection;
import com.biblioteca.controlador.Controlador;
import com.biblioteca.dao.PublicacionDAO;
import com.biblioteca.vista.pantalla.BarraLateral;
import com.biblioteca.vista.pantalla.CatalogoLibros;
import com.biblioteca.vista.pantalla.ConcederPrestamo;
import com.biblioteca.vista.pantalla.DevolverPrestamo;
import com.biblioteca.vista.pantalla.Ejemplares;
import com.biblioteca.vista.pantalla.GestionUsuarios;
import com.biblioteca.vista.pantalla.InicioSesion;
import com.biblioteca.vista.pantalla.PanelControl;
import com.biblioteca.vista.pantalla.RecuperarCuenta;
import com.biblioteca.vista.pantalla.SancionManual;

/**
 * Controlador para la navegación entre vistas
 */
public class ControladorNavegacion {
    // Atributos
    /**
     * Ventana principal de la aplicación
     */
    private JFrame ventana;

    /**
     * Vistas panel de control
     */
    private PanelControl panelControl;
    /**
     * Vistas barra lateral
     */
    private BarraLateral barraLateral;
    /**
     * Vistas inicio de sesion
     */
    private InicioSesion inicioSesion;
    /**
     * Vistas conceder prestamo
     */
    private ConcederPrestamo concederPrestamo;
    /**
     * Vistas devolver prestamo
     */
    private DevolverPrestamo devolverPrestamo;
    /**
     * Vistas recuperar cuenta
     */
    private RecuperarCuenta recuperarCuenta;
    /**
     * Vistas ejemplares
     */
    private Ejemplares ejemplares;
    /**
     * Vistas gestion usuarios
     */
    private GestionUsuarios gestionUsuarios;
    /**
     * Vistas sancion manual
     */
    private SancionManual sancionManual;
    /**
     * Vistas publicaciones
     */
    private CatalogoLibros publicaciones;
    /**
     * Layout padre para la navegación entre vistas
     */
    private CardLayout cardPadre = new CardLayout();
    /**
     * Layout hijo para la navegación entre vistas
     */
    private CardLayout cardHijo = new CardLayout();
    /**
     * Panel contenedor principal de la aplicación
     */
    private JPanel panelPadre = new JPanel(cardPadre);
    /**
     * Panel contenedor secundario de la aplicación
     */
    private JPanel panelHijo = new JPanel(cardHijo);
    /**
     * Panel principal que contiene las vistas internas y usa `cardHijo`.
     * Se declara como campo para que `cambiarPantallaHijo` pueda usarlo.
     * Contiene la barra lateral y el área principal de contenido.
     */
    private JPanel panelPrincipal;

    /**
     * Conexión compartida para uso interno del controlador de navegación cuando
     * necesita realizar consultas/operaciones rápidas con DAOs.
     */
    private final DBConnection dbConnection;

    /**
     * Constructor del controlador de navegación
     */
    public ControladorNavegacion(Controlador controlador) {

        // guardamos la conexión inyectada desde el Controlador principal
        this.dbConnection = controlador.getDbConnection();

        // Inicializar vistas pasando el controlador
        this.panelControl = new PanelControl(controlador);
        this.barraLateral = new BarraLateral(controlador);
        this.inicioSesion = new InicioSesion(controlador);
        this.concederPrestamo = new ConcederPrestamo(controlador);
        this.devolverPrestamo = new DevolverPrestamo(controlador);
        this.recuperarCuenta = new RecuperarCuenta(controlador);
        this.ejemplares = new Ejemplares(controlador);
        this.gestionUsuarios = new GestionUsuarios(controlador);
        this.sancionManual = new SancionManual(controlador);
        this.publicaciones = new CatalogoLibros(controlador);

        ventana = new JFrame("Aplicación de Biblioteca");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(800, 600);

        configurarPanelPadre();
        configurarPanelHijo();
        ventana.setVisible(true);

    }

    /**
     * Configura el panel contenedor principal con las vistas
     */
    private void configurarPanelPadre() {
        panelPadre.add(inicioSesion.pantalla(), "inicioSesion");
        panelPadre.add(recuperarCuenta.pantalla(), "recuperarCuenta");
        panelPadre.add(panelHijo, "entrarSistema");
        ventana.add(panelPadre);
    }

    /**
     * Configura el panel hijo con las vistas internas
     */
    private void configurarPanelHijo() {
        panelHijo.setLayout(new BoxLayout(panelHijo, BoxLayout.X_AXIS));
        panelHijo.add(barraLateral.pantalla());

        // Asignar al campo para que se use en cambiarPantallaHijo
        panelPrincipal = new JPanel(cardHijo);
        panelPrincipal.add(panelControl.pantalla(), "panelControl");
        panelPrincipal.add(concederPrestamo.pantalla(), "concederPrestamo");
        panelPrincipal.add(devolverPrestamo.pantalla(), "devolverPrestamo");
        panelPrincipal.add(ejemplares.pantalla(), "ejemplares");
        panelPrincipal.add(gestionUsuarios.pantalla(), "gestionUsuarios");
        panelPrincipal.add(sancionManual.pantalla(), "sancionManual");
        panelPrincipal.add(publicaciones.pantalla(), "publicaciones");
        cardHijo.show(panelPrincipal, "panelControl");

        panelHijo.add(panelPrincipal);

    }

    /**
     * Muestra la ventana principal de la aplicación
     */
    public void mostrarVentana() {
        cardPadre.show(panelPadre, "inicioSesion");
    }

    /**
     * Devuelve la ventana principal (para usar como padre de diálogos/modales)
     *
     * @return JFrame principal
     */
    public JFrame getVentana() {
        return this.ventana;
    }

    /**
     * Cambia la pantalla del panel padre
     * 
     * @param nombrePantalla
     */
    public void cambiarPantallaPadre(String nombrePantalla) {
        cardPadre.show(panelPadre, nombrePantalla);
    }

    /**
     * Cambia la pantalla del panel hijo
     * 
     * @param nombrePantalla
     */
    public void cambiarPantallaHijo(String nombrePantalla) {
        // Usar el panel que contiene el CardLayout (panelPrincipal) para evitar
        // la excepción "wrong parent for CardLayout" cuando se llama a show.
        if (panelPrincipal != null) {
            cardHijo.show(panelPrincipal, nombrePantalla);
        }
    }

    /**
     * Muestra la pantalla de ejemplares cargando la publicación indicada por id
     *
     * @param idPublicacion id de la publicación a mostrar
     */
    public void mostrarEjemplaresParaPublicacion(int idPublicacion) {
        // obtener resumen de la publicación usando la conexión compartida
        PublicacionDAO publicacionDAO = new PublicacionDAO(this.dbConnection);
        String[] resumen = publicacionDAO.obtenerResumenPublicacionPorId(idPublicacion);
        if (resumen != null) {
            // cargar datos en la vista ejemplares
            if (this.ejemplares != null) {
                this.ejemplares.cargarPublicacionResumen(resumen);
            }
        }
        // mostrar pantalla de ejemplares
        cambiarPantallaHijo("ejemplares");
    }

    /**
     * Refresca la vista de publicaciones si está inicializada
     */
    public void refrescarPublicaciones() {
        if (this.publicaciones != null) {
            this.publicaciones.refrescarPublicaciones();
        }
    }

    /**
     * Marca programáticamente en la barra lateral qué sección está activa
     *
     * @param nombrePantalla key de la pantalla hija (ej: "panelControl")
     */
    public void marcarPantallaActiva(String nombrePantalla) {
        if (this.barraLateral != null) {
            this.barraLateral.marcarPantallaActiva(nombrePantalla);
        }
    }

    /**
     * Refresca la lista de usuarios si la vista está inicializada
     */
    public void refrescarUsuarios() {
        if (this.gestionUsuarios != null) {
            this.gestionUsuarios.refrescarUsuarios();
        }
    }

    /**
     * Refresca los datos del panel de control si está inicializado
     */
    public void refrescarPanelControl() {
        if (this.panelControl != null) {
            this.panelControl.refrescarPanel();
        }
    }

}