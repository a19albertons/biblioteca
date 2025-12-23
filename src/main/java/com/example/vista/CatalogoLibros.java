package com.example.vista;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.example.controlador.Controlador;

/**
 * Clase para la vista Publicaciones
 */
public class CatalogoLibros {

    /**
     * Controlador de la aplicación
     */
    Controlador controlador;

    // Componentes que se mantienen como campo para permitir refresco dinámico
    private JPanel panel;
    private JTextField buscadorField;
    private JComboBox<String> comboCiclosField;
    private JComboBox<String> comboEditorialField;
    private JPanel cardsContainer;
    private JScrollPane scrollPublicaciones;
    private String[][] resumenPublicacionesField;

    /**
     * Constructor de la vista CatalogoLibros
     *
     * @param controlador controlador principal
     */
    public CatalogoLibros(Controlador controlador) {
        this.controlador = controlador;
    }

    /**
     * Muestra la pantalla del catálogo de publicaciones/libros
     *
     * @return JPanel con la lista y filtros del catálogo
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

        // Abrir modal para nueva publicación
        btnNuevaPub.addActionListener(e -> {
            NuevaPublicacionDialog dialog = new NuevaPublicacionDialog(controlador.getControladorNavegacion().getVentana(), controlador);
            dialog.setVisible(true);
        });

        // Buscador con hint y padding izquierdo
        buscadorField = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(Color.decode("#000000"));
                    g2.setFont(getFont().deriveFont(Font.PLAIN, getFont().getSize()));
                    Insets insets = getInsets();
                    int y = (getHeight() - g2.getFontMetrics().getHeight()) / 2 + g2.getFontMetrics().getAscent();
                    g2.drawString("Buscar por Título, ISBN o Autor...", insets.left + 5, y);
                    g2.dispose();
                }
            }
        };
        buscadorField.setBounds(30, 80, 350, 40);
        buscadorField.setFont(com.example.utilities.Fonts.openSans(12f));
        buscadorField.setBackground(Color.white);
        buscadorField.setForeground(Color.decode("#000000"));
        // padding izquierdo 8px y borde para el input
        buscadorField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.decode("#ffffff")), BorderFactory.createEmptyBorder(0, 8, 0, 0)));

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
        comboCiclosField = new JComboBox<>();
        comboCiclosField.setBounds(110, 130, 100, 20);
        comboCiclosField.addItem("Ciclos");
        String[] listaCiclos = controlador.getControladorPanelControl().listaCiclos();
        for (String ciclo : listaCiclos) {
            comboCiclosField.addItem(ciclo);
        }

        comboEditorialField = new JComboBox<>();
        comboEditorialField.setBounds(230, 130, 100, 20);
        comboEditorialField.addItem("Editorial");
        String[] listaEditoriales = controlador.getControladorPanelControl().listaEditoriales();
        for (String editorial : listaEditoriales) {
            comboEditorialField.addItem(editorial);
        }

        // Seccion de las cards de publicaciones
        // Cards dinámicos: contenedor con grid de 3 columnas y scroll
        cardsContainer = new JPanel(new java.awt.GridLayout(0, 3, 15, 15));
        cardsContainer.setBackground(Color.decode("#EDF3F6"));

        // Obtener resumen de publicaciones (datos sin filtrar)
        resumenPublicacionesField = controlador.getControladorPanelControl().listaPublicacionesResumen();

        // Scroll que contiene las cards
        scrollPublicaciones = new JScrollPane(cardsContainer);
        scrollPublicaciones.setBounds(20, 170, 570, 380);
        scrollPublicaciones.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        // Función para poblar las cards aplicando filtros de búsqueda, ciclo y editorial
        poblarCards(cardsContainer, resumenPublicacionesField, "", "Ciclos", "Editorial");

        // Eventos para aplicar filtros
        btnBuscar.addActionListener(e -> {
            String criterio = buscadorField.getText().trim();
            String cicloSel = (String) comboCiclosField.getSelectedItem();
            String editorialSel = (String) comboEditorialField.getSelectedItem();
            poblarCards(cardsContainer, resumenPublicacionesField, criterio, cicloSel, editorialSel);
            scrollPublicaciones.revalidate();
            scrollPublicaciones.repaint();
        });

        comboCiclosField.addActionListener(e -> {
            String criterio = buscadorField.getText().trim();
            String cicloSel = (String) comboCiclosField.getSelectedItem();
            String editorialSel = (String) comboEditorialField.getSelectedItem();
            poblarCards(cardsContainer, resumenPublicacionesField, criterio, cicloSel, editorialSel);
            scrollPublicaciones.revalidate();
            scrollPublicaciones.repaint();
        });

        comboEditorialField.addActionListener(e -> {
            String criterio = buscadorField.getText().trim();
            String cicloSel = (String) comboCiclosField.getSelectedItem();
            String editorialSel = (String) comboEditorialField.getSelectedItem();
            poblarCards(cardsContainer, resumenPublicacionesField, criterio, cicloSel, editorialSel);
            scrollPublicaciones.revalidate();
            scrollPublicaciones.repaint();
        });
        
        // Agregar componentes al panel principal
        panel.add(encabezado);
        panel.add(buscadorField);
        panel.add(btnBuscar);
        panel.add(filtros);
        panel.add(comboCiclosField);
        panel.add(comboEditorialField);
        panel.add(scrollPublicaciones);

        return panel;
    }

    /**
     * Poblador de cards: aplica filtros y reconstruye el contenedor de cards
     *
     * @param cardsContainer contenedor donde se añaden las cards
     * @param publicaciones datos sin filtrar (resumen)
     * @param search texto de búsqueda (título/isbn/autores)
     * @param cicloFilter filtro de ciclo ("Ciclos" indica sin filtro)
     * @param editorialFilter filtro de editorial ("Editorial" indica sin filtro)
     */
    private void poblarCards(JPanel cardsContainer, String[][] publicaciones, String search, String cicloFilter, String editorialFilter) {
        cardsContainer.removeAll();

        if (publicaciones != null) {
            for (String[] fila : publicaciones) {
                String tituloTxt = fila[0] != null ? fila[0] : "";
                String isbnTxt = fila[1] != null ? fila[1] : "";
                String autoresTxt = fila[2] != null ? fila[2] : "";
                String ciclosTxt = fila[3] != null ? fila[3] : "";
                String editorialTxt = fila[4] != null ? fila[4] : "";
                int disponiblesNum = 0;
                try {
                    disponiblesNum = Integer.parseInt(fila[5]);
                } catch (Exception ex) {
                    disponiblesNum = 0;
                }
                String idPub = fila[6] != null ? fila[6] : "";

                // Aplicar filtros
                boolean matches = true;
                if (search != null && !search.isEmpty()) {
                    String s = search.toLowerCase();
                    if (!(tituloTxt.toLowerCase().contains(s) || isbnTxt.toLowerCase().contains(s) || autoresTxt.toLowerCase().contains(s))) {
                        matches = false;
                    }
                }
                if (cicloFilter != null && !cicloFilter.equals("Ciclos") && !cicloFilter.isEmpty()) {
                    if (!ciclosTxt.toLowerCase().contains(cicloFilter.toLowerCase())) {
                        matches = false;
                    }
                }
                if (editorialFilter != null && !editorialFilter.equals("Editorial") && !editorialFilter.isEmpty()) {
                    if (!editorialTxt.toLowerCase().contains(editorialFilter.toLowerCase())) {
                        matches = false;
                    }
                }

                if (!matches)
                    continue;

                // Construir card por publicación
                JPanel card = new JPanel();
                card.setPreferredSize(new Dimension(170, 220));
                card.setLayout(null);
                card.setBackground(Color.white);
                card.setBorder(null);

                JPanel placeholder = new JPanel();
                placeholder.setBackground(Color.decode("#EEEEEE"));
                placeholder.setBounds(0, 0, 170, 100);
                card.add(placeholder);

                JLabel tituloLabel = new JLabel(tituloTxt);
                tituloLabel.setBounds(10, 110, 150, 20);
                card.add(tituloLabel);

                JLabel isbnLabel = new JLabel("ISBN: " + isbnTxt);
                isbnLabel.setBounds(10, 135, 150, 16);
                isbnLabel.setFont(isbnLabel.getFont().deriveFont(11f));
                card.add(isbnLabel);

                JLabel autoresLabel = new JLabel("Autor(es): " + autoresTxt);
                autoresLabel.setBounds(10, 150, 150, 18);
                autoresLabel.setFont(autoresLabel.getFont().deriveFont(10f));
                autoresLabel.setForeground(Color.decode("#666666"));
                card.add(autoresLabel);

                JLabel ciclosLabel = new JLabel("Ciclos: " + ciclosTxt);
                ciclosLabel.setBounds(10, 165, 150, 18);
                ciclosLabel.setFont(ciclosLabel.getFont().deriveFont(10f));
                ciclosLabel.setForeground(Color.decode("#666666"));
                card.add(ciclosLabel);

                JLabel editorialLabel = new JLabel("Ed: " + editorialTxt);
                editorialLabel.setBounds(10, 180, 150, 18);
                card.add(editorialLabel);

                JLabel disponibilidadLabel = new JLabel();
                disponibilidadLabel.setBounds(10, 198, 150, 18);
                disponibilidadLabel.setHorizontalAlignment(JLabel.CENTER);
                disponibilidadLabel.setVerticalAlignment(JLabel.CENTER);
                disponibilidadLabel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.decode("#2BC187")));
                // Establecer texto y color según disponibilidad
                if (disponiblesNum > 0) {
                    disponibilidadLabel.setText(disponiblesNum + " disponibles");
                    disponibilidadLabel.setForeground(Color.decode("#2BC187"));
                } else {
                    disponibilidadLabel.setText("AGOTADO");
                    disponibilidadLabel.setForeground(Color.decode("#F4791B"));
                    disponibilidadLabel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.decode("#F4791B")));
                }
                card.add(disponibilidadLabel);

                // Click para ir a ejemplares
                card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                card.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        // Navegar a la pantalla de ejemplares y cargar la publicación seleccionada
                        try {
                            int id = Integer.parseInt(idPub);
                            controlador.getControladorNavegacion().mostrarEjemplaresParaPublicacion(id);
                        } catch (NumberFormatException ex) {
                            // fallback: mostrar la pantalla sin contexto
                            controlador.getControladorNavegacion().cambiarPantallaHijo("ejemplares");
                        }
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        card.setBackground(Color.decode("#F6F9FB"));
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        card.setBackground(Color.white);
                    }
                });

                cardsContainer.add(card);
            }
        }

        if (cardsContainer.getComponentCount() == 0) {
            JPanel empty = new JPanel();
            empty.setBackground(Color.decode("#EDF3F6"));
            empty.setLayout(null);
            JLabel emptyLabel = new JLabel("No se encontraron resultados");
            emptyLabel.setBounds(10, 10, 250, 20);
            empty.add(emptyLabel);
            cardsContainer.add(empty);
        }
    }

    /**
     * Refresca la lista de publicaciones y filtros en la vista. Vuelve a obtener
     * los datos desde el controlador y repuebla los combos y cards.
     */
    public void refrescarPublicaciones() {
        // Guardar selecciones actuales
        String selCiclo = comboCiclosField.getSelectedItem() == null ? null : comboCiclosField.getSelectedItem().toString();
        String selEditorial = comboEditorialField.getSelectedItem() == null ? null : comboEditorialField.getSelectedItem().toString();

        // Re-popular combos
        comboCiclosField.removeAllItems();
        comboCiclosField.addItem("Ciclos");
        String[] listaCiclos = controlador.getControladorPanelControl().listaCiclos();
        for (String ciclo : listaCiclos) {
            comboCiclosField.addItem(ciclo);
        }

        comboEditorialField.removeAllItems();
        comboEditorialField.addItem("Editorial");
        String[] listaEditoriales = controlador.getControladorPanelControl().listaEditoriales();
        for (String editorial : listaEditoriales) {
            comboEditorialField.addItem(editorial);
        }

        // Restaurar selección si sigue disponible
        if (selCiclo != null) comboCiclosField.setSelectedItem(selCiclo);
        if (selEditorial != null) comboEditorialField.setSelectedItem(selEditorial);

        // Actualizar datos
        resumenPublicacionesField = controlador.getControladorPanelControl().listaPublicacionesResumen();

        // Repoblar con filtros actuales
        String criterio = buscadorField.getText().trim();
        String cicloSel = (String) comboCiclosField.getSelectedItem();
        String editorialSel = (String) comboEditorialField.getSelectedItem();
        poblarCards(cardsContainer, resumenPublicacionesField, criterio, cicloSel, editorialSel);
        scrollPublicaciones.revalidate();
        scrollPublicaciones.repaint();
    }

}
