package com.example.vista;

import com.example.controlador.Controlador;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Clase para la vista Sanción manual
 */
public class SancionManual {

    /**
     * Controlador de la aplicación
     */
    Controlador controlador;

    /**
     * Constructor de la vista SancionManual
     *
     * @param controlador controlador principal
     */
    public SancionManual(Controlador controlador) {
        this.controlador = controlador;
    }

    /**
     * Muestra la pantalla para crear sanciones manuales
     *
     * @return JPanel con la interfaz de sanciones
     */
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
        JLabel titulo = new JLabel("Panel de Control");
        titulo.setFont(titulo.getFont().deriveFont(24f));
        titulo.setBounds(10, 10, 200, 40);
        encabezado.add(titulo);

        // Formulario sancion manual
        JPanel formularioSancion = new JPanel();
        formularioSancion.setSize(540, 450);
        formularioSancion.setBounds(30, 90, 540, 450);
        formularioSancion.setBackground(Color.white);
        formularioSancion.setLayout(null);

        // Paso 1: Usuario afectado
        JLabel paso1 = new JLabel("1. Usuario afectado:");
        paso1.setFont(paso1.getFont().deriveFont(16f));
        paso1.setBounds(20, 10, 300, 30);
        paso1.setForeground(Color.decode("#468DAE"));
        formularioSancion.add(paso1);

        // Resultado elegir socio
        JLabel resultadoSocio = new JLabel("");
        resultadoSocio.setBounds(20, 50, 500, 40);
        resultadoSocio.setText(
                "<html>Usuario: Estudiante) : <span style='color:#2BC187; font-weight:bold'>Sin sanciones</span></html>");
        resultadoSocio.setBackground(Color.decode("#EDF3F6"));
        resultadoSocio.setOpaque(true);
        // Pequeño margen izquierdo para separar el texto del borde (10px)
        resultadoSocio.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        formularioSancion.add(resultadoSocio);

        JButton btnCambiarUsuario = new JButton("Cambiar");
        btnCambiarUsuario.setBounds(390, 55, 100, 30);
        btnCambiarUsuario.setBackground(Color.decode("#468DAE"));
        btnCambiarUsuario.setForeground(Color.WHITE);
        btnCambiarUsuario.setFocusPainted(false);
        btnCambiarUsuario.setBorder(null);
        // Para que el botón quede encima del JLabel
        formularioSancion.add(btnCambiarUsuario, 0);

        // Paso 2: Detalles de la sancion y ejemplar afectado
        JLabel paso2 = new JLabel("2. Detalles de la sanción y ejemplar afectado:");
        paso2.setFont(paso2.getFont().deriveFont(16f));
        paso2.setBounds(20, 110, 400, 30);
        paso2.setForeground(Color.decode("#468DAE"));
        formularioSancion.add(paso2);

        // Motivo de la sancion
        JLabel lblMotivo = new JLabel("Motivo de la sanción:");
        lblMotivo.setBounds(20, 150, 200, 25);
        formularioSancion.add(lblMotivo);

        // JcomboBox motivo (opciones predefinidas)
        JComboBox<String> comboMotivo = new JComboBox<>();
        comboMotivo.setBounds(20, 180, 230, 30);
        comboMotivo.addItem("Daño de material");
        comboMotivo.addItem("Pérdida del material");
        comboMotivo.addItem("Comportamiento inapropiado");
        formularioSancion.add(comboMotivo);

        // Ejemplar afectado (alineado a la derecha del motivo)
        JLabel lblEjemplar = new JLabel("Ejemplar:");
        lblEjemplar.setBounds(270, 150, 200, 25);
        formularioSancion.add(lblEjemplar);

        JTextField txtEjemplar = new JTextField();
        txtEjemplar.setBounds(270, 180, 230, 30);
        formularioSancion.add(txtEjemplar);

        // Descripción de la sanción
        JLabel lblDescripcion = new JLabel("Descripción / Observaciones:");
        lblDescripcion.setBounds(20, 220, 200, 25);
        formularioSancion.add(lblDescripcion);
        
        JTextField txtDescripcion = new JTextField();
        txtDescripcion.setBounds(20, 250, 480, 80);
        formularioSancion.add(txtDescripcion);

        // Fecha de inicio de la sanción
        JLabel lblFechaInicio = new JLabel("Fecha de inicio de la sanción:");
        lblFechaInicio.setBounds(20, 340, 200, 25);
        formularioSancion.add(lblFechaInicio);

        JTextField txtFechaInicio = new JTextField();
        txtFechaInicio.setBounds(20, 370, 200, 30);
        formularioSancion.add(txtFechaInicio);

        // Fecha de fin de la sanción
        JLabel lblFechaFin = new JLabel("Fecha de fin de la sanción:");
        lblFechaFin.setBounds(270, 340, 200, 25);
        formularioSancion.add(lblFechaFin); 

        JTextField txtFechaFin = new JTextField();
        txtFechaFin.setBounds(270, 370, 200, 30);
        formularioSancion.add(txtFechaFin);

        // Limpiar Formulario
        JButton btnLimpiar = new JButton("Limpiar Formulario");
        btnLimpiar.setBounds(150, 410, 150, 30);
        btnLimpiar.setBackground(Color.white);
        btnLimpiar.setForeground(Color.BLACK);
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.setBorder(null);
        formularioSancion.add(btnLimpiar);

        // Aplicar Sanción
        JButton btnAplicarSancion = new JButton("Aplicar Sanción");
        btnAplicarSancion.setBounds(320, 410, 150, 30);
        btnAplicarSancion.setBackground(Color.decode("#F4791B"));
        btnAplicarSancion.setForeground(Color.WHITE);
        btnAplicarSancion.setFocusPainted(false);
        btnAplicarSancion.setBorder(null);
        formularioSancion.add(btnAplicarSancion);



        // Agregar paneles al panel principal
        panel.add(encabezado);
        panel.add(formularioSancion);

        return panel;
    }

}
