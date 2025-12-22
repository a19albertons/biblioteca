package com.example.vista;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.BorderFactory;

import com.example.controlador.Controlador;

/**
 * Clase para la vista Conceder préstamo
 */
public class ConcederPrestamo {

    Controlador controlador;

    public ConcederPrestamo(Controlador controlador) {
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
        JLabel titulo = new JLabel("Nuevo prestamo");
        titulo.setFont(titulo.getFont().deriveFont(24f));
        titulo.setBounds(30, 10, 300, 40);
        encabezado.add(titulo);

        // Boton nueva publicacion
        JButton btnNuevaPub = new JButton("Formulario dar de baja");
        btnNuevaPub.setBounds(430, 15, 150, 30);
        btnNuevaPub.setBackground(Color.white);
        btnNuevaPub.setForeground(Color.decode("#468DAE"));
        btnNuevaPub.setFocusPainted(false);
        btnNuevaPub.setBorder(null);
        encabezado.add(btnNuevaPub);

        // Panel de contenido
        JPanel contenido = new JPanel();
        contenido.setSize(540, 480);
        contenido.setLayout(null);
        contenido.setBackground(Color.white);
        contenido.setBounds(30, 80, 540, 480);

        // Componentes del formulario
        // Paso 1: Identificar socio
        JLabel paso1 = new JLabel("1. Identificar Socio (Usuario)");
        paso1.setBounds(20, 20, 300, 30);
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
        resultadoSocio.setBounds(20, 150, 400, 40);
        resultadoSocio.setText("<html>Usuario: Estudiante) : <span style='color:#2BC187; font-weight:bold'>Sin sanciones</span></html>");
        resultadoSocio.setBackground(Color.decode("#EDF3F6"));
        resultadoSocio.setOpaque(true);
        // Pequeño margen izquierdo para separar el texto del borde (10px)
        resultadoSocio.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        contenido.add(resultadoSocio);

        // Paso 2: Seleccionar libro
        JLabel paso2 = new JLabel("2. Seleccionar Libro");
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

        // Publicacion
        JLabel publicacion = new JLabel("Publicacion");
        publicacion.setBounds(230, 250, 300, 35);
        contenido.add(publicacion);

        JLabel txtPublicacion = new JLabel();
        txtPublicacion.setBounds(230, 280, 200, 35);
        txtPublicacion.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, Color.white));
        contenido.add(txtPublicacion);

        // Fecha Inicio
        JLabel fechaInicio = new JLabel("Fecha Inicio");
        fechaInicio.setBounds(20, 330, 300, 35);
        contenido.add(fechaInicio);

        JLabel txtFechaInicio = new JLabel();
        txtFechaInicio.setBounds(20, 360, 200, 35);
        txtFechaInicio.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, Color.white));
        contenido.add(txtFechaInicio);

        // Fecha Fin
        JLabel fechaFin = new JLabel("Fecha Devolucion Prevista");
        fechaFin.setBounds(230, 330, 300, 35);
        contenido.add(fechaFin);

        JLabel txtFechaFin = new JLabel();
        txtFechaFin.setBounds(230, 360, 200, 35);
        txtFechaFin.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, Color.white));
        contenido.add(txtFechaFin);

        // Boton cancelar
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(250, 420, 100, 35);
        btnCancelar.setBackground(Color.white);
        btnCancelar.setForeground(Color.BLACK);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorder(null);
        contenido.add(btnCancelar);

        // Boton Registar Prestamo
        JButton btnRegistrarPrestamo = new JButton("Registrar Préstamo");
        btnRegistrarPrestamo.setBounds(370, 420, 120, 35);
        btnRegistrarPrestamo.setBackground(Color.decode("#F4791B"));
        btnRegistrarPrestamo.setForeground(Color.WHITE);
        btnRegistrarPrestamo.setFocusPainted(false);
        btnRegistrarPrestamo.setBorder(null);
        contenido.add(btnRegistrarPrestamo);

        // Añadir los dos subpaneles al principal
        panel.add(encabezado);
        panel.add(contenido);
        return panel;
    }

}
