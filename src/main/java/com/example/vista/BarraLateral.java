package com.example.vista;

import java.awt.Color;
import java.awt.Dimension;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.example.controlador.Controlador;
import com.example.utilities.AppResources;

/**
 * Clase para la vista Barra lateral
 */
public class BarraLateral {

    /**
     * Controlador de la aplicación
     */
    Controlador controlador;

    // Botones de la barra lateral expuestos para poder marcar estado activo desde
    // fuera
    /**
     * Botón de inicio
     */
    private JButton btnInicio;
    /**
     * Botón de catálogo de libros
     */
    private JButton btnCatalogoLibros;
    /**
     * Botón de préstamos
     */
    private JButton btnPrestamos;
    /**
     * Botón de socios / usuarios
     */
    private JButton btnSocios;
    /**
     * Botón de sanciones
     */
    private JButton btnSanciones;

    /**
     * Constructor de la vista BarraLateral
     * 
     * @param controlador
     */
    public BarraLateral(Controlador controlador) {
        this.controlador = controlador;
    }

    /**
     * Muestra la pantalla de la barra lateral con opciones de navegación
     * 
     * @return
     */
    public JPanel pantalla() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(200, 600));
        panel.setBackground(Color.decode("#468DAE"));
        URL imgUrl = AppResources.logoPath();
        if (imgUrl != null) {
            JLabel lblLogo = new JLabel(new ImageIcon(imgUrl));
            lblLogo.setHorizontalAlignment(JLabel.CENTER);
            panel.add(lblLogo);
        } else {
            JLabel lblAlt = new JLabel("Logo no encontrado");
            lblAlt.setHorizontalAlignment(JLabel.CENTER);
            panel.add(lblAlt);
        }

        // Un margen para separar el logo de las opciones
        JPanel gap = new JPanel();
        gap.setPreferredSize(new Dimension(200, 10));
        gap.setBackground(Color.decode("#468DAE"));
        panel.add(gap);

        // Panel de opciones
        JPanel opciones = new JPanel();
        opciones.setPreferredSize(new Dimension(200, 500));
        opciones.setBackground(Color.decode("#468DAE"));

        // Boton de inicio
        btnInicio = new JButton("Inicio");
        btnInicio.setPreferredSize(new Dimension(200, 40));
        btnInicio.setBackground(Color.decode("#444444"));
        btnInicio.setForeground(Color.WHITE);
        btnInicio.setFocusPainted(false);
        // Borde lado izquierdo
        btnInicio.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, Color.decode("#F4791B")));
        opciones.add(btnInicio);

        // Boton de catalogo de libros
        btnCatalogoLibros = new JButton("Catálogo de libros");
        btnCatalogoLibros.setPreferredSize(new Dimension(200, 40));
        btnCatalogoLibros.setForeground(Color.decode("#EDF3F6"));
        btnCatalogoLibros.setBackground(Color.decode("#468DAE"));
        btnCatalogoLibros.setFocusPainted(false);
        btnCatalogoLibros.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        opciones.add(btnCatalogoLibros);

        // Boton de prestamos
        btnPrestamos = new JButton("Préstamos");
        btnPrestamos.setPreferredSize(new Dimension(200, 40));
        btnPrestamos.setForeground(Color.decode("#EDF3F6"));
        btnPrestamos.setBackground(Color.decode("#468DAE"));
        btnPrestamos.setFocusPainted(false);
        btnPrestamos.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        opciones.add(btnPrestamos);

        // Botons de socios / usuarios
        btnSocios = new JButton("Socios / Usuarios");
        btnSocios.setPreferredSize(new Dimension(200, 40));
        btnSocios.setForeground(Color.decode("#EDF3F6"));
        btnSocios.setBackground(Color.decode("#468DAE"));
        btnSocios.setFocusPainted(false);
        btnSocios.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        opciones.add(btnSocios);

        // Boton de sanciones
        btnSanciones = new JButton("Sanciones");
        btnSanciones.setPreferredSize(new Dimension(200, 40));
        btnSanciones.setForeground(Color.decode("#EDF3F6"));
        btnSanciones.setBackground(Color.decode("#468DAE"));
        btnSanciones.setFocusPainted(false);
        btnSanciones.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        opciones.add(btnSanciones);

        panel.add(opciones);

        // Eventos de los botones (gestión de estado activo/inactivo)
        btnInicio.addActionListener(e -> {
            setActiveButton(btnInicio, btnInicio, btnCatalogoLibros, btnPrestamos, btnSocios, btnSanciones);
            controlador.getControladorNavegacion().cambiarPantallaHijo("panelControl");
        });

        btnCatalogoLibros.addActionListener(e -> {
            setActiveButton(btnCatalogoLibros, btnInicio, btnCatalogoLibros, btnPrestamos, btnSocios, btnSanciones);
            controlador.getControladorNavegacion().cambiarPantallaHijo("publicaciones");
        });

        btnPrestamos.addActionListener(e -> {
            setActiveButton(btnPrestamos, btnInicio, btnCatalogoLibros, btnPrestamos, btnSocios, btnSanciones);
            controlador.getControladorNavegacion().cambiarPantallaHijo("concederPrestamo");
        });

        btnSocios.addActionListener(e -> {
            setActiveButton(btnSocios, btnInicio, btnCatalogoLibros, btnPrestamos, btnSocios, btnSanciones);
            controlador.getControladorNavegacion().cambiarPantallaHijo("gestionUsuarios");
        });

        btnSanciones.addActionListener(e -> {
            setActiveButton(btnSanciones, btnInicio, btnCatalogoLibros, btnPrestamos, btnSocios, btnSanciones);
            controlador.getControladorNavegacion().cambiarPantallaHijo("sancionManual");
        });

        return panel;
    }

    /**
     * Marca programáticamente una pantalla como activa en la barra lateral
     *
     * @param nombrePantalla nombre del key usado en ControladorNavegacion (ej:
     *                       "panelControl", "publicaciones")
     */
    public void marcarPantallaActiva(String nombrePantalla) {
        // si es null, marcar inicio
        if (nombrePantalla == null) {
            setActiveButton(btnInicio, btnInicio, btnCatalogoLibros, btnPrestamos, btnSocios, btnSanciones);
            return;
        }
        // evaluar nombrePantalla
        switch (nombrePantalla) {
            case "panelControl":
                setActiveButton(btnInicio, btnInicio, btnCatalogoLibros, btnPrestamos, btnSocios, btnSanciones);
                break;
            case "publicaciones":
                setActiveButton(btnCatalogoLibros, btnInicio, btnCatalogoLibros, btnPrestamos, btnSocios, btnSanciones);
                break;
            case "concederPrestamo":
                setActiveButton(btnPrestamos, btnInicio, btnCatalogoLibros, btnPrestamos, btnSocios, btnSanciones);
                break;
            case "gestionUsuarios":
                setActiveButton(btnSocios, btnInicio, btnCatalogoLibros, btnPrestamos, btnSocios, btnSanciones);
                break;
            case "sancionManual":
                setActiveButton(btnSanciones, btnInicio, btnCatalogoLibros, btnPrestamos, btnSocios, btnSanciones);
                break;
            default:
                setActiveButton(btnInicio, btnInicio, btnCatalogoLibros, btnPrestamos, btnSocios, btnSanciones);
        }
    }

    /**
     * Marca un botón como activo y restablece el estilo del resto de botones a
     * inactivo.
     *
     * @param active  botón que debe quedar marcado como activo
     * @param buttons lista completa de botones a evaluar y actualizar
     */
    private void setActiveButton(JButton active, JButton... buttons) {
        for (JButton b : buttons) {
            if (b == active) {
                b.setBackground(Color.decode("#444444"));
                b.setForeground(Color.WHITE);
                b.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, Color.decode("#F4791B")));
            } else {
                b.setBackground(Color.decode("#468DAE"));
                b.setForeground(Color.decode("#EDF3F6"));
                b.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            }
        }
    }

}
