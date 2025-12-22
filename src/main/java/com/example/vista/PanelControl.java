package com.example.vista;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.example.controlador.Controlador;

/**
 * Clase para la vista Panel de control
 */
public class PanelControl {

    /**
     * Controlador de la aplicación
     */
    Controlador controlador;

    /**
     * Constructor de la vista PanelControl
     *
     * @param controlador controlador principal
     */
    public PanelControl(Controlador controlador) {
        this.controlador = controlador;
    }

    /**
     * Muestra la pantalla del panel de control con tarjetas y resúmenes
     *
     * @return JPanel con el panel de control
     */
    public JPanel pantalla() {
        // Panel principal
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

        // Card 1
        JPanel card1 = new JPanel();
        card1.setSize(170, 100);
        card1.setLayout(null);
        card1.setBackground(Color.white);
        card1.setBounds(30, 100, 170, 100);
        card1.setBorder(BorderFactory.createMatteBorder(5, 0, 0, 0, Color.decode("#468DAE")));

        // Card 1 - tema
        JLabel tema1 = new JLabel("Prestamos hoy");
        tema1.setBounds(10, 10, 150, 30);
        tema1.setFont(new Font("Open Sans", Font.PLAIN, 12));
        card1.add(tema1);

        // Valor -- provisional
        JLabel valor1 = new JLabel("25");
        valor1.setFont(valor1.getFont().deriveFont(36f));
        valor1.setBounds(10, 40, 150, 50);
        card1.add(valor1);

        // Card 2 (central) - Pendientes (fondo blanco, borde superior naranja)
        JPanel card2 = new JPanel();
        card2.setSize(170, 100);
        card2.setLayout(null);
        card2.setBackground(Color.white);
        card2.setBounds(215, 100, 170, 100);
        card2.setBorder(BorderFactory.createMatteBorder(5, 0, 0, 0, Color.decode("#F4791B")));

        JLabel tema2 = new JLabel("Pendientes");
        tema2.setBounds(10, 10, 150, 30);
        // Fuente Open Sans, texto normal
        tema2.setFont(com.example.utilities.Fonts.openSans(12f));
        card2.add(tema2);

        JLabel valor2 = new JLabel("8");
        valor2.setFont(valor2.getFont().deriveFont(36f));
        valor2.setBounds(10, 40, 150, 50);
        valor2.setForeground(Color.decode("#F4791B"));

        // dejar el valor en color oscuro por defecto para contraste
        card2.add(valor2);

        // Card 3 (última) - Socios activos (fondo blanco, borde superior verde)
        JPanel card3 = new JPanel();
        card3.setSize(170, 100);
        card3.setLayout(null);
        card3.setBackground(Color.white);
        card3.setBounds(400, 100, 170, 100);
        card3.setBorder(BorderFactory.createMatteBorder(5, 0, 0, 0, Color.decode("#2BC187")));

        JLabel tema3 = new JLabel("Socios activos");
        tema3.setBounds(10, 10, 150, 30);
        // Fuente Open Sans, texto normal
        tema3.setFont(com.example.utilities.Fonts.openSans(12f));
        card3.add(tema3);

        JLabel valor3 = new JLabel("102");
        valor3.setFont(valor3.getFont().deriveFont(36f));
        valor3.setBounds(10, 40, 150, 50);
        // dejar valor en color por defecto (oscuro)
        card3.add(valor3);

        // Ultimos movimientos
        JLabel ultimos = new JLabel("Últimos movimientos");
        ultimos.setFont(ultimos.getFont().deriveFont(18f));
        ultimos.setBounds(30, 220, 200, 30);
        panel.add(ultimos);

        JPanel movimientosPanel = new JPanel();
        movimientosPanel.setBackground(Color.white);
        movimientosPanel.setLayout(null);
        movimientosPanel.setBounds(30, 260, 540, 300);
        movimientosPanel.setBorder(BorderFactory.createLineBorder(Color.decode("#E6ECEF")));

        // Encabezados de columna
        JLabel col1 = new JLabel("ID EJEMPLAR");
        col1.setBounds(15, 10, 100, 20);
        col1.setFont(col1.getFont().deriveFont(Font.BOLD, 12f));
        col1.setForeground(Color.decode("#468DAE"));
        movimientosPanel.add(col1);

        JLabel col2 = new JLabel("LIBRO");
        col2.setBounds(120, 10, 300, 20);
        col2.setFont(col2.getFont().deriveFont(Font.BOLD, 12f));
        col2.setForeground(Color.decode("#468DAE"));
        movimientosPanel.add(col2);

        JLabel col3 = new JLabel("ESTADO");
        col3.setBounds(440, 10, 80, 20);
        col3.setFont(col3.getFont().deriveFont(Font.BOLD, 12f));
        col3.setForeground(Color.decode("#468DAE"));
        movimientosPanel.add(col3);

        // Filas de ejemplo (serán removidas cuando se implemente la lógica)
        JLabel id1 = new JLabel("#9821");
        id1.setBounds(15, 40, 100, 20);
        id1.setForeground(Color.decode("#666666"));
        movimientosPanel.add(id1);

        JLabel libro1 = new JLabel("Intro a Bases de Datos");
        libro1.setBounds(120, 40, 300, 20);
        libro1.setForeground(Color.decode("#666666"));
        movimientosPanel.add(libro1);

        JLabel estado1 = new JLabel("DEVUELTO", JLabel.CENTER);
        estado1.setBounds(440, 40, 80, 20);
        estado1.setOpaque(true);
        estado1.setBackground(Color.decode("#E6FFF0"));
        estado1.setForeground(Color.decode("#2BC187"));
        estado1.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
        movimientosPanel.add(estado1);

        JLabel id2 = new JLabel("#1102");
        id2.setBounds(15, 70, 100, 20);
        id2.setForeground(Color.decode("#666666"));
        movimientosPanel.add(id2);

        JLabel libro2 = new JLabel("Física Vol. II");
        libro2.setBounds(120, 70, 300, 20);
        libro2.setForeground(Color.decode("#666666"));
        movimientosPanel.add(libro2);

        JLabel estado2 = new JLabel("PRESTADO", JLabel.CENTER);
        estado2.setBounds(440, 70, 80, 20);
        estado2.setOpaque(true);
        estado2.setBackground(Color.decode("#FFF4E6"));
        estado2.setForeground(Color.decode("#F4791B"));
        estado2.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
        movimientosPanel.add(estado2);

        
        panel.add(encabezado);
        panel.add(card1);
        panel.add(card2);
        panel.add(card3);
        panel.add(movimientosPanel);
        return panel;
    }

}
