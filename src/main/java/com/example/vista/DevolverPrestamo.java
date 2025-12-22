package com.example.vista;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.BorderFactory;

import com.example.controlador.Controlador;

/**
 * Clase para la vista Devolver préstamo
 */
public class DevolverPrestamo {

    Controlador controlador;

    public DevolverPrestamo(Controlador controlador) {
        this.controlador = controlador;
    }

    public JPanel pantalla() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(600, 600));
        panel.setBackground(Color.decode("#EDF3F6"));
        panel.setLayout(null);

        // Panel de encabezado con título
        JPanel encabezado = new JPanel();
        encabezado.setSize(new Dimension(600, 60));
        encabezado.setBackground(Color.white);
        encabezado.setLayout(null);
        encabezado.setBounds(0, 0, 600, 60);

        // Titulo
        JLabel titulo = new JLabel("Devolución Préstamo");
        titulo.setFont(titulo.getFont().deriveFont(24f));
        titulo.setBounds(30, 10, 300, 40);
        encabezado.add(titulo);

        // Link / boton formulario registrar prestamo (estilo enlace azul)
        JButton btnFormularioRegistrar = new JButton("Formulario registrar prestamo");
        btnFormularioRegistrar.setBounds(360, 15, 220, 30);
        btnFormularioRegistrar.setBackground(Color.white);
        btnFormularioRegistrar.setForeground(Color.decode("#468DAE"));
        btnFormularioRegistrar.setFocusPainted(false);
        btnFormularioRegistrar.setBorder(null);
        encabezado.add(btnFormularioRegistrar);

        // Panel de contenido
        JPanel contenido = new JPanel();
        contenido.setSize(540, 480);
        contenido.setLayout(null);
        contenido.setBackground(Color.white);
        contenido.setBounds(30, 80, 540, 480);

        // Componentes del formulario
        // Paso 1: Identificar socio
        JLabel paso1 = new JLabel("1. Identificar Socio (Usuario)");
        paso1.setBounds(20, 20, 350, 30);
        paso1.setForeground(Color.decode("#468DAE"));
        paso1.setFont(paso1.getFont().deriveFont(16f));
        contenido.add(paso1);

        JLabel dniID = new JLabel("DNI / ID:");
        dniID.setBounds(20, 70, 100, 25);
        contenido.add(dniID);

        JTextField txtDniID = new JTextField();
        txtDniID.setBounds(20, 100, 300, 35);
        contenido.add(txtDniID);

        JButton btnBuscarSocio = new JButton("Buscar");
        btnBuscarSocio.setBounds(330, 100, 90, 35);
        btnBuscarSocio.setBackground(Color.decode("#468DAE"));
        btnBuscarSocio.setForeground(Color.WHITE);
        btnBuscarSocio.setFocusPainted(false);
        btnBuscarSocio.setBorder(null);
        contenido.add(btnBuscarSocio);

        JLabel resultadoSocio = new JLabel("");
        resultadoSocio.setBounds(20, 150, 480, 40);
        resultadoSocio.setText("<html>Usuario: (Estudiante) - <span style='color:#2BC187; font-weight:bold'>Sin Sanciones</span></html>");
        resultadoSocio.setBackground(Color.decode("#EDF3F6"));
        resultadoSocio.setOpaque(true);
        resultadoSocio.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        contenido.add(resultadoSocio);

        // Paso 2: Identificar Ejemplar
        JLabel paso2 = new JLabel("2. Identificar Ejemplar");
        paso2.setBounds(20, 200, 300, 30);
        paso2.setForeground(Color.decode("#468DAE"));
        paso2.setFont(paso2.getFont().deriveFont(16f));
        contenido.add(paso2);

        // ID Ejemplar
        JLabel idEjemplar = new JLabel("ID Ejemplar:");
        idEjemplar.setBounds(20, 250, 100, 25);
        contenido.add(idEjemplar);

        JTextField txtIdEjemplar = new JTextField();
        txtIdEjemplar.setBounds(20, 280, 200, 35);
        contenido.add(txtIdEjemplar);

        // Publicacion (mostrador de publicación detectada)
        JLabel publicacion = new JLabel("Publicación");
        publicacion.setBounds(230, 250, 300, 35);
        contenido.add(publicacion);

        JLabel txtPublicacion = new JLabel("Detectado: Estructura de Datos (Ed. 2)");
        txtPublicacion.setBounds(230, 280, 260, 35);
        txtPublicacion.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, Color.white));
        contenido.add(txtPublicacion);

        // Boton cancelar
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(250, 420, 100, 35);
        btnCancelar.setBackground(Color.white);
        btnCancelar.setForeground(Color.BLACK);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorder(null);
        contenido.add(btnCancelar);

        // Boton Devolver Prestamo
        JButton btnDevolverPrestamo = new JButton("DEVOLVER PRESTAMO");
        btnDevolverPrestamo.setBounds(370, 412, 150, 42);
        btnDevolverPrestamo.setBackground(Color.decode("#F4791B"));
        btnDevolverPrestamo.setForeground(Color.WHITE);
        btnDevolverPrestamo.setFocusPainted(false);
        btnDevolverPrestamo.setBorder(null);
        btnDevolverPrestamo.setFont(btnDevolverPrestamo.getFont().deriveFont(Font.BOLD, 13f));
        contenido.add(btnDevolverPrestamo);

        // Eventos botones
        btnBuscarSocio.addActionListener(e -> {
            // placeholder: en el futuro buscar en BD por ID/DNI
            resultadoSocio.setText("<html>Usuario: (Estudiante) - <span style='color:#2BC187; font-weight:bold'>Sin Sanciones</span></html>");
        });

        btnCancelar.addActionListener(e -> {
            controlador.getControladorNavegacion().cambiarPantallaHijo("panelControl");
        });

        btnDevolverPrestamo.addActionListener(e -> {
            // placeholder: ejecutar devolución y volver al panel de control
            controlador.getControladorNavegacion().cambiarPantallaHijo("panelControl");
        });

        // Añadir los dos subpaneles al principal
        panel.add(encabezado);
        panel.add(contenido);
        return panel;
    }

}
