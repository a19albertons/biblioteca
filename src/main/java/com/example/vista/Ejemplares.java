package com.example.vista;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Insets;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.example.controlador.Controlador;

/**
 * Clase para la vista Ejemplares
 */
public class Ejemplares {

    /**
     * Controlador de la aplicación
     */
    Controlador controlador;

    /**
     * Constructor de la vista Ejemplares
     *
     * @param controlador controlador principal
     */
    public Ejemplares(Controlador controlador) {
        this.controlador = controlador;
    }

    /**
     * Muestra la pantalla de ejemplares
     *
     * @return JPanel con la vista de ejemplares
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
        JLabel titulo = new JLabel("Gestion de Ejemplares");
        titulo.setFont(titulo.getFont().deriveFont(24f));
        titulo.setBounds(30, 10, 300, 40);
        encabezado.add(titulo);

        // Link / boton formulario registrar prestamo (estilo enlace azul)
        JButton btnFormularioRegistrar = new JButton("< Volver");
        btnFormularioRegistrar.setBounds(410, 15, 220, 30);
        btnFormularioRegistrar.setBackground(Color.white);
        btnFormularioRegistrar.setForeground(Color.decode("#468DAE"));
        btnFormularioRegistrar.setFocusPainted(false);
        btnFormularioRegistrar.setBorder(null);
        encabezado.add(btnFormularioRegistrar);

        // Panel de publicacion
        JPanel publicacion = new JPanel();
        publicacion.setSize(new Dimension(560, 100));
        publicacion.setBackground(Color.white);
        publicacion.setLayout(null);
        publicacion.setBounds(20, 70, 560, 100);

        // Placeholder lo que va aqui aún no ha sido definido
        JPanel placeholder = new JPanel();
        placeholder.setBackground(Color.decode("#EEEEEE"));
        placeholder.setSize(50,70);
        placeholder.setBounds(10,15,50,70);
        publicacion.add(placeholder);

        // Datos publicacion
        JLabel tituloPublicacion = new JLabel("Ingeniería de Software (7ª Ed.)");
        tituloPublicacion.setBounds(80, 15, 300, 25);
        publicacion.add(tituloPublicacion);

        JLabel isbnPublicacion = new JLabel("ISBN: 978-0073375977");
        isbnPublicacion.setBounds(80, 45, 200, 20);
        publicacion.add(isbnPublicacion);

        // Autores solo aplica a libros si es revista dejar en blanco
        JLabel autoresPublicacion = new JLabel("Autor: Roger Pressman");
        autoresPublicacion.setBounds(80, 70, 300, 20);
        publicacion.add(autoresPublicacion);

        // Botones
        JButton btnEditarEjemplar = new JButton() {
            // Asegura que el color de la opacidad sea el debido
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                // paint custom background with alpha and keep icon/text on top
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setComposite(java.awt.AlphaComposite.SrcOver);
                // fill with background color (includes alpha)
                g2.setColor(getBackground());
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
                // let UI draw icon/text/etc
                super.paintComponent(g);
            }
        };
        // Sitúan a la altura del título (y=15) y tamaño 24x24
        btnEditarEjemplar.setBounds(440, 15, 24, 24);
        // Icono escalado a 16x16
        java.net.URL editarIconUrl = getClass().getResource("/editar.png");
        if (editarIconUrl != null) {
            Image img = new ImageIcon(editarIconUrl).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            btnEditarEjemplar.setIcon(new ImageIcon(img));
        }
        Color editarColor = new Color(70, 141, 174, 102);
        btnEditarEjemplar.setBackground(editarColor);
        btnEditarEjemplar.setContentAreaFilled(false);
        btnEditarEjemplar.setOpaque(false);
        btnEditarEjemplar.setRolloverEnabled(false);
        btnEditarEjemplar.setFocusPainted(false);
        btnEditarEjemplar.setBorder(null);
        btnEditarEjemplar.setToolTipText("Editar ejemplar");
        btnEditarEjemplar.setMargin(new Insets(0, 0, 0, 0));
        publicacion.add(btnEditarEjemplar);

        JButton btnEliminarEjemplar = new JButton() {
            // Asegura que el color de la opacidad sea el debido
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setComposite(java.awt.AlphaComposite.SrcOver);
                g2.setColor(getBackground());
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        // Ubicado a la derecha del botón editar (24px + 4px gap)
        btnEliminarEjemplar.setBounds(480, 15, 24, 24);
        // Icono escalado a 16x16
        java.net.URL borrarIconUrl = getClass().getResource("/borrar.png");
        if (borrarIconUrl != null) {
            Image img = new ImageIcon(borrarIconUrl).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            btnEliminarEjemplar.setIcon(new ImageIcon(img));
        }
        Color eliminarColor = new Color(192, 57, 43, 102);
        btnEliminarEjemplar.setBackground(eliminarColor);
        btnEliminarEjemplar.setContentAreaFilled(false);
        btnEliminarEjemplar.setOpaque(false);
        btnEliminarEjemplar.setRolloverEnabled(false);
        btnEliminarEjemplar.setFocusPainted(false);
        btnEliminarEjemplar.setBorder(null);
        btnEliminarEjemplar.setToolTipText("Eliminar ejemplar");
        btnEliminarEjemplar.setMargin(new Insets(0, 0, 0, 0));
        publicacion.add(btnEliminarEjemplar);


        // Panel de ejemplares
        JPanel ejemplares = new JPanel();
        ejemplares.setSize(new Dimension(560, 380));
        ejemplares.setBackground(Color.white);
        ejemplares.setLayout(null);
        ejemplares.setBounds(20, 180, 560, 380);

        // Título y botón + NUEVO EJEMPLAR
        JLabel listadoTitulo = new JLabel("Listado de Ejemplares");
        listadoTitulo.setBounds(10, 12, 300, 24);
        listadoTitulo.setFont(listadoTitulo.getFont().deriveFont(java.awt.Font.BOLD, 14f));
        ejemplares.add(listadoTitulo);

        JButton btnNuevoEjemplar = new JButton("+ NUEVO EJEMPLAR");
        btnNuevoEjemplar.setBounds(380, 8, 160, 28);
        btnNuevoEjemplar.setBackground(Color.decode("#F4791B"));
        btnNuevoEjemplar.setForeground(Color.white);
        btnNuevoEjemplar.setFocusPainted(false);
        btnNuevoEjemplar.setBorder(null);
        ejemplares.add(btnNuevoEjemplar);

        // Tabla con datos de ejemplo (sin backend)
        String[] cols = new String[] {"ID", "num_ejemplar", "FECHA", "ESTADO", "ACCIONES"};
        Object[][] data = new Object[][] {
                {"#9821", "1", "12/09/23", "DISPONIBLE", null},
                {"#1102", "2", "01/02/24", "PRESTADO", null},
                {"#4401", "3", "15/05/22", "BAJA", null}
        };

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new javax.swing.JTable(model);
        table.setRowHeight(36);
        table.setShowGrid(false);
        table.setIntercellSpacing(new java.awt.Dimension(0, 0));

        // Render para estado (badges)
        class StatusRenderer extends JLabel implements TableCellRenderer {
            public StatusRenderer() {
                setOpaque(true);
                setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            }

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                String s = (value != null) ? value.toString() : "";
                setText(s);
                switch (s) {
                    case "DISPONIBLE":
                        setBackground(Color.decode("#E6FFF0"));
                        setForeground(Color.decode("#2BC187"));
                        break;
                    case "PRESTADO":
                        setBackground(Color.decode("#FFF4E6"));
                        setForeground(Color.decode("#F4791B"));
                        break;
                    case "BAJA":
                        setBackground(Color.decode("#EAF6FF"));
                        setForeground(Color.decode("#5FAEC7"));
                        break;
                    default:
                        setBackground(Color.white);
                        setForeground(Color.black);
                }
                setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
                return this;
            }
        }

        // Render para acciones (no funcionales, solo apariencia)
        class ActionsRenderer implements TableCellRenderer {
            private final JPanel panelCell = new JPanel();
            private final JButton edit = new JButton();
            private final JButton del = new JButton();

            public ActionsRenderer() {
                panelCell.setOpaque(false);
                panelCell.setLayout(new FlowLayout(FlowLayout.RIGHT, 6, 6));

                // Botón editar con fondo translúcido pintado manualmente
                java.net.URL editarIconUrl = getClass().getResource("/editar.png");
                final JButton editBtn = new JButton() {
                    @Override
                    protected void paintComponent(java.awt.Graphics g) {
                        java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                        g2.setComposite(java.awt.AlphaComposite.SrcOver);
                        g2.setColor(new Color(70, 141, 174, 102));
                        g2.fillRect(0, 0, getWidth(), getHeight());
                        g2.dispose();
                        super.paintComponent(g);
                    }
                };
                if (editarIconUrl != null) {
                    Image img = new ImageIcon(editarIconUrl).getImage().getScaledInstance(12, 12, Image.SCALE_SMOOTH);
                    editBtn.setIcon(new ImageIcon(img));
                }
                editBtn.setPreferredSize(new Dimension(24, 24));
                editBtn.setToolTipText("Editar");
                editBtn.setBorder(null);
                editBtn.setFocusPainted(false);
                editBtn.setContentAreaFilled(false);
                editBtn.setOpaque(false);
                editBtn.setMargin(new Insets(0,0,0,0));

                // Botón eliminar con fondo translúcido pintado manualmente
                URL borrarIconUrl = getClass().getResource("/borrar.png");
                final JButton delBtn = new JButton() {
                    @Override
                    protected void paintComponent(java.awt.Graphics g) {
                        java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                        g2.setComposite(java.awt.AlphaComposite.SrcOver);
                        g2.setColor(new Color(192, 57, 43, 102));
                        g2.fillRect(0, 0, getWidth(), getHeight());
                        g2.dispose();
                        super.paintComponent(g);
                    }
                };
                if (borrarIconUrl != null) {
                    Image img = new ImageIcon(borrarIconUrl).getImage().getScaledInstance(12, 12, Image.SCALE_SMOOTH);
                    delBtn.setIcon(new ImageIcon(img));
                }
                delBtn.setPreferredSize(new Dimension(24, 24));
                delBtn.setToolTipText("Eliminar");
                delBtn.setBorder(null);
                delBtn.setFocusPainted(false);
                delBtn.setContentAreaFilled(false);
                delBtn.setOpaque(false);
                delBtn.setMargin(new Insets(0,0,0,0));

                panelCell.add(editBtn);
                panelCell.add(delBtn);
            }

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                return panelCell;
            }
        }

        table.getColumnModel().getColumn(3).setCellRenderer(new StatusRenderer());
        table.getColumnModel().getColumn(4).setCellRenderer(new ActionsRenderer());

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(10, 50, 540, 320);
        scroll.setBorder(null);
        ejemplares.add(scroll);


        // Añadir paneles principales al panel principal
        panel.add(encabezado);
        panel.add(publicacion);
        panel.add(ejemplares);


        
        return panel;
    }

}  
