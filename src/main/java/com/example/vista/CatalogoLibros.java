package com.example.vista;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.example.controlador.Controlador;

/**
 * Clase para la vista Publicaciones
 */
public class CatalogoLibros {

    Controlador controlador;

    public CatalogoLibros(Controlador controlador) {
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
        JLabel titulo = new JLabel("Catálogo de Libros");
        titulo.setFont(titulo.getFont().deriveFont(24f));
        titulo.setBounds(10, 10, 300, 40);
        encabezado.add(titulo);

        // Boton nueva publicacion
        JButton btnNuevaPub = new JButton("+ Nueva Publicación");
        btnNuevaPub.setBounds(430, 15, 150, 30);
        btnNuevaPub.setBackground(Color.decode("#F4791B"));
        btnNuevaPub.setForeground(Color.WHITE);
        btnNuevaPub.setFocusPainted(false);
        btnNuevaPub.setBorder(null);
        encabezado.add(btnNuevaPub);

        // Buscador 
        JTextField buscador = new JTextField();
        buscador.setBounds(30, 80, 350, 40);
        buscador.setBorder(null);

        // Buscar 
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBounds(400, 80, 80, 40);
        btnBuscar.setBackground(Color.decode("#468DAE"));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setBorder(null); 

        // Filtros
        JLabel filtros = new JLabel("Filtrar por :");
        filtros.setBounds(30, 130, 80, 20);

        // JcomboBox ciclos y editorial
        JComboBox<String> comboCiclos = new JComboBox<>();
        comboCiclos.setBounds(110, 130, 100, 20);
        comboCiclos.addItem("Ciclos");

        JComboBox<String> comboEditorial = new JComboBox<>();
        comboEditorial.setBounds(230, 130, 100, 20);
        comboEditorial.addItem("Editorial");

        // Cards de ejemplo
        JPanel cardEjemplo1 = new JPanel();
        cardEjemplo1.setSize(170, 220);
        cardEjemplo1.setLayout(null);
        cardEjemplo1.setBackground(Color.white);
        cardEjemplo1.setBounds(30, 170, 170, 220);
        cardEjemplo1.setBorder(null);

        JPanel placeholder1 = new JPanel();
        placeholder1.setBackground(Color.decode("#EEEEEE"));
        placeholder1.setBounds(0, 0, 170, 100);
        cardEjemplo1.add(placeholder1);

        // JLabel con info del libro
        JLabel titulo1 = new JLabel("Ingenieria de S.");
        titulo1.setBounds(10, 110, 150, 20);
        cardEjemplo1.add(titulo1);
        JLabel isbn = new JLabel("ISBN: 1234567890");
        isbn.setBounds(10, 140, 150, 20);
        cardEjemplo1.add(isbn);

        JLabel editorial1 = new JLabel("Ed: Ejemplo");
        editorial1.setBounds(10, 170, 150, 20);
        cardEjemplo1.add(editorial1);

        // Disponibilidad
        JLabel disponibilidad1 = new JLabel("3 Disponible");
        disponibilidad1.setBounds(10, 200, 150, 20);
        disponibilidad1.setForeground(Color.decode("#2BC187"));
        disponibilidad1.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.decode("#2BC187")));
        disponibilidad1.setHorizontalAlignment(JLabel.CENTER);
        disponibilidad1.setVerticalAlignment(JLabel.CENTER);
        cardEjemplo1.add(disponibilidad1);

        // Card Ejemplo 2 (centrada)
        JPanel cardEjemplo2 = new JPanel();
        cardEjemplo2.setSize(170, 220);
        cardEjemplo2.setLayout(null);
        cardEjemplo2.setBackground(Color.white);
        cardEjemplo2.setBounds(215, 170, 170, 220);
        cardEjemplo2.setBorder(null);

        JPanel placeholder2 = new JPanel();
        placeholder2.setBackground(Color.decode("#EEEEEE"));
        placeholder2.setBounds(0, 0, 170, 100);
        cardEjemplo2.add(placeholder2);

        JLabel titulo2 = new JLabel("Historia del Arte");
        titulo2.setBounds(10, 110, 150, 20);
        cardEjemplo2.add(titulo2);
        JLabel isbn2 = new JLabel("ISBN: 978-0-00-000");
        isbn2.setBounds(10, 140, 150, 20);
        cardEjemplo2.add(isbn2);

        JLabel editorial2 = new JLabel("Ed: Cultura");
        editorial2.setBounds(10, 170, 150, 20);
        cardEjemplo2.add(editorial2);

        JLabel disponibilidad2 = new JLabel("AGOTADO");
        disponibilidad2.setBounds(10, 200, 150, 20);
        disponibilidad2.setForeground(Color.decode("#F4791B"));
        disponibilidad2.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.decode("#F4791B")));
        disponibilidad2.setHorizontalAlignment(JLabel.CENTER);
        disponibilidad2.setVerticalAlignment(JLabel.CENTER);
        cardEjemplo2.add(disponibilidad2);

        // Card Ejemplo 3 (derecha)
        JPanel cardEjemplo3 = new JPanel();
        cardEjemplo3.setSize(170, 220);
        cardEjemplo3.setLayout(null);
        cardEjemplo3.setBackground(Color.white);
        cardEjemplo3.setBounds(400, 170, 170, 220);
        cardEjemplo3.setBorder(null);

        JPanel placeholder3 = new JPanel();
        placeholder3.setBackground(Color.decode("#EEEEEE"));
        placeholder3.setBounds(0, 0, 170, 100);
        cardEjemplo3.add(placeholder3);

        JLabel titulo3 = new JLabel("Matemáticas I");
        titulo3.setBounds(10, 110, 150, 20);
        cardEjemplo3.add(titulo3);
        JLabel isbn3 = new JLabel("ISBN: 978-1-23-456");
        isbn3.setBounds(10, 140, 150, 20);
        cardEjemplo3.add(isbn3);

        JLabel editorial3 = new JLabel("Ed: Ejemplo");
        editorial3.setBounds(10, 170, 150, 20);
        cardEjemplo3.add(editorial3);

        JLabel disponibilidad3 = new JLabel("12 DISPONIBLES");
        disponibilidad3.setBounds(10, 200, 150, 20);
        disponibilidad3.setForeground(Color.decode("#2BC187"));
        disponibilidad3.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.decode("#2BC187")));
        disponibilidad3.setHorizontalAlignment(JLabel.CENTER);
        disponibilidad3.setVerticalAlignment(JLabel.CENTER);
        cardEjemplo3.add(disponibilidad3);


        panel.add(encabezado);
        panel.add(buscador);
        panel.add(btnBuscar);
        panel.add(filtros);
        panel.add(comboCiclos);
        panel.add(comboEditorial);
        panel.add(cardEjemplo1);
        panel.add(cardEjemplo2);
        panel.add(cardEjemplo3);

        return panel;
    }

}
