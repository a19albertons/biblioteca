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

/**
 * Clase para la vista Barra lateral
 */
public class BarraLateral {

    Controlador controlador;

    public BarraLateral(Controlador controlador) {
        this.controlador = controlador;
    }

    public JPanel pantalla() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(200,600));
        panel.setBackground(Color.decode("#468DAE"));
        URL imgUrl = getClass().getResource("/logo.png");
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
        JButton btnInicio = new JButton("Inicio");
        btnInicio.setPreferredSize(new Dimension(200, 40));
        btnInicio.setBackground(Color.decode("#444444"));
        btnInicio.setForeground(Color.WHITE);
        btnInicio.setFocusPainted(false);
        // Borde lado izquierdo
        btnInicio.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, Color.decode("#F4791B")));
        opciones.add(btnInicio);

        // Boton de catalogo de libros
        JButton btnCatalogoLibros = new JButton("Catálogo de libros");
        btnCatalogoLibros.setPreferredSize(new Dimension(200, 40));
        btnCatalogoLibros.setForeground(Color.decode("#EDF3F6"));
        btnCatalogoLibros.setBackground(Color.decode("#468DAE"));
        btnCatalogoLibros.setFocusPainted(false);
        btnCatalogoLibros.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        opciones.add(btnCatalogoLibros);

        // Boton de prestamos
        JButton btnPrestamos = new JButton("Préstamos");
        btnPrestamos.setPreferredSize(new Dimension(200, 40));
        btnPrestamos.setForeground(Color.decode("#EDF3F6"));
        btnPrestamos.setBackground(Color.decode("#468DAE"));
        btnPrestamos.setFocusPainted(false);
        btnPrestamos.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        opciones.add(btnPrestamos);

        // Botons de socios / usuarios
        JButton btnSocios = new JButton("Socios / Usuarios");
        btnSocios.setPreferredSize(new Dimension(200, 40));
        btnSocios.setForeground(Color.decode("#EDF3F6"));
        btnSocios.setBackground(Color.decode("#468DAE"));
        btnSocios.setFocusPainted(false);
        btnSocios.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        opciones.add(btnSocios);

        // Boton de sanciones
        JButton btnSanciones = new JButton("Sanciones");
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

    // Helper para marcar un botón como activo y dejar el resto en estilo inactivo
    private void setActiveButton(javax.swing.JButton active, javax.swing.JButton... buttons) {
        for (javax.swing.JButton b : buttons) {
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
