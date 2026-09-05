package com.biblioteca.vista.pantalla;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.biblioteca.controlador.Controlador;
import com.biblioteca.utilities.AppResources;
import com.biblioteca.vista.dialogo.EditarEjemplarDialog;
import com.biblioteca.vista.dialogo.EditarPublicacionDialog;
import com.biblioteca.vista.dialogo.EliminarEjemplarDialog;
import com.biblioteca.vista.dialogo.EliminarPublicacionDialog;
import com.biblioteca.vista.dialogo.NuevoEjemplarDialog;

/**
 * Clase para la vista Ejemplares
 */
public class Ejemplares {

    /**
     * Controlador de la aplicación
     */
    private Controlador controlador;

    // Componentes que se actualizan cuando se selecciona una publicación
    /**
     * Panel de la publicación seleccionada
     */
    private JPanel publicacionPanel;
    /**
     * Etiqueta del título de la publicación seleccionada
     */
    private JLabel tituloPublicacion;
    /**
     * Etiqueta del ISBN de la publicación seleccionada
     */
    private JLabel isbnPublicacion;
    /**
     * Etiqueta de los autores de la publicación seleccionada
     */
    private JLabel autoresPublicacion;

    // Tabla de ejemplares (modelo y tabla como campos para actualizar
    // dinámicamente)
    /**
     * Modelo de la tabla de ejemplares
     */
    private DefaultTableModel ejemplaresModel;
    /**
     * Tabla de ejemplares
     */
    private JTable ejemplaresTable;

    /**
     * Id de la publicación actualmente cargada en la vista (para acciones como
     * editar)
     */
    private int currentPublicacionId = -1;

    /**
     * Constructor de la vista Ejemplares
     *
     * @param controlador controlador principal
     */
    public Ejemplares(final Controlador controlador) {
        this.controlador = controlador;
    }

    /**
     * Muestra la pantalla de ejemplares
     *
     * @return JPanel con la vista de ejemplares
     */
    public JPanel pantalla() {
        // Panel principal
        JPanel panel = crearPanelPrincipal();
        panel.add(crearEncabezado());
        panel.add(crearPanelPublicacion());
        JPanel ejemplares = crearPanelEjemplares();
        panel.add(ejemplares);
        // Pasar panel a crearBotonNuevoEjemplar
        crearBotonNuevoEjemplar(ejemplares);

        return panel;
    }

    /**
     * Crea el panel principal de la pantalla.
     *
     * @return JPanel principal
     */
    private JPanel crearPanelPrincipal() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(600, 600));
        panel.setBackground(Color.decode("#EDF3F6"));
        panel.setLayout(null);

        return panel;
    }

    /**
     * Crea el panel de encabezado con título.
     *
     * @return JPanel encabezado
     */
    private JPanel crearEncabezado() {
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
        // Navegar de vuelta al catálogo de publicaciones previo refresco
        btnFormularioRegistrar.addActionListener(evt -> {
            controlador.getControladorNavegacion().refrescarPublicaciones();
            controlador.getControladorNavegacion().cambiarPantallaHijo("publicaciones");
        });
        btnFormularioRegistrar.setToolTipText("Volver al catálogo");
        encabezado.add(btnFormularioRegistrar);

        return encabezado;
    }

    /**
     * Crea el panel de publicación seleccionada.
     *
     * @return JPanel publicacionPanel
     */
    private JPanel crearPanelPublicacion() {
        publicacionPanel = new JPanel();
        publicacionPanel.setSize(new Dimension(560, 100));
        publicacionPanel.setBackground(Color.white);
        publicacionPanel.setLayout(null);
        publicacionPanel.setBounds(20, 70, 560, 100);

        // Placeholder lo que va aqui aún no ha sido definido
        JPanel placeholder = new JPanel();
        placeholder.setBackground(Color.decode("#EEEEEE"));
        placeholder.setSize(50, 70);
        placeholder.setBounds(10, 15, 50, 70);
        publicacionPanel.add(placeholder);

        // Datos publicacion
        tituloPublicacion = new JLabel("Ingeniería de Software (7ª Ed.)");
        tituloPublicacion.setBounds(80, 15, 300, 25);
        publicacionPanel.add(tituloPublicacion);

        isbnPublicacion = new JLabel("ISBN: 978-0073375977");
        isbnPublicacion.setBounds(80, 45, 200, 20);
        publicacionPanel.add(isbnPublicacion);

        // Autores solo aplica a libros si es revista dejar en blanco
        autoresPublicacion = new JLabel("Autor: Roger Pressman");
        autoresPublicacion.setBounds(80, 70, 300, 20);
        publicacionPanel.add(autoresPublicacion);

        // Botones
        JButton btnEditarEjemplar = crearBotonEditar();
        publicacionPanel.add(btnEditarEjemplar);

        JButton btnEliminarEjemplar = crearBotonEliminar();
        publicacionPanel.add(btnEliminarEjemplar);

        return publicacionPanel;
    }

    /**
     * Crea el botón de editar ejemplar con su listener.
     *
     * @return JButton btnEditarEjemplar
     */
    private JButton crearBotonEditar() {
        JButton btnEditarEjemplar = new JButton() {
            @Override
            protected void paintComponent(final Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setComposite(AlphaComposite.SrcOver);
                g2.setColor(getBackground());
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        // Sitúan a la altura del título (y=15) y tamaño 24x24
        btnEditarEjemplar.setBounds(440, 15, 24, 24);
        // Icono escalado a 16x16
        URL editarIconUrl = AppResources.editarPath();
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

        // Abrir diálogo de edición al pulsar editar (si hay una publicación cargada)
        btnEditarEjemplar.addActionListener(evt -> {
            if (currentPublicacionId > 0) {
                EditarPublicacionDialog dialog = new EditarPublicacionDialog(
                        controlador.getControladorNavegacion().getVentana(), controlador, currentPublicacionId);
                dialog.setVisible(true);
                // refrescar la vista de ejemplares para mostrar cambios
                controlador.getControladorNavegacion().mostrarEjemplaresParaPublicacion(currentPublicacionId);
            } else {
                JOptionPane.showMessageDialog(publicacionPanel,
                        "No hay publicación seleccionada para editar", "Aviso",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        return btnEditarEjemplar;
    }

    /**
     * Crea el botón de eliminar ejemplar con su listener.
     *
     * @return JButton btnEliminarEjemplar
     */
    private JButton crearBotonEliminar() {
        JButton btnEliminarEjemplar = new JButton() {
            @Override
            protected void paintComponent(final Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setComposite(AlphaComposite.SrcOver);
                g2.setColor(getBackground());
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        // Ubicado a la derecha del botón editar (24px + 4px gap)
        btnEliminarEjemplar.setBounds(480, 15, 24, 24);
        // Icono escalado a 16x16
        URL borrarIconUrl = AppResources.eliminarPath();
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

        btnEliminarEjemplar.addActionListener(evt -> {
            if (currentPublicacionId > 0) {
                EliminarPublicacionDialog d = new EliminarPublicacionDialog(
                        controlador.getControladorNavegacion().getVentana(), controlador, currentPublicacionId);
                d.setVisible(true);
                // refrescar para mostrar posibles cambios
                controlador.getControladorNavegacion().mostrarEjemplaresParaPublicacion(currentPublicacionId);
            } else {
                JOptionPane.showMessageDialog(publicacionPanel,
                        "No hay publicación seleccionada para eliminar", "Aviso",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        return btnEliminarEjemplar;
    }

    /**
     * Crea el panel de ejemplares con tabla y controles.
     *
     * @return JPanel ejemplares
     */
    private JPanel crearPanelEjemplares() {
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

        JButton btnNuevoEjemplar = crearBotonNuevoEjemplar(ejemplares);
        ejemplares.add(btnNuevoEjemplar);

        // Tabla de ejemplares (modelo dinámico, se actualizará cuando se cargue una
        // publicación)
        String[] cols = new String[] { "ID", "num_ejemplar", "FECHA", "ESTADO", "ACCIONES" };

        ejemplaresModel = new DefaultTableModel(new Object[0][0], cols) {
            @Override
            public boolean isCellEditable(final int row, final int column) {
                return false;
            }
        };

        ejemplaresTable = new JTable(ejemplaresModel);
        ejemplaresTable.setRowHeight(36);
        ejemplaresTable.setShowGrid(false);
        ejemplaresTable.setIntercellSpacing(new Dimension(0, 0));

        // Render para estado (badges)
        ejemplaresTable.getColumnModel().getColumn(3).setCellRenderer(new StatusRenderer());

        // Render para acciones (no funcionales, solo apariencia)
        ejemplaresTable.getColumnModel().getColumn(4).setCellRenderer(new ActionsRenderer());

        // Hacer que al hacer click en la columna ACCIONES se abra el diálogo de editar
        // ejemplar
        ejemplaresTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                int row = ejemplaresTable.rowAtPoint(e.getPoint());
                int col = ejemplaresTable.columnAtPoint(e.getPoint());
                // Si se hizo click en la columna ACCIONES (4)
                if (row >= 0 && col == 4) {
                    Object idObj = ejemplaresModel.getValueAt(row, 0);
                    // Abrir diálogo de edición o eliminar según la posición del click dentro de la
                    // celda
                    if (idObj != null) {
                        String idStr = idObj.toString().replace("#", "");
                        try {
                            int idEjemplar = Integer.parseInt(idStr.trim());
                            // Determinar la X relativa dentro de la celda para distinguir botones
                            Rectangle cellRect = ejemplaresTable.getCellRect(row, col, true);
                            int clickX = e.getX() - cellRect.x;
                            // Definir la zona de 'eliminar' como los ~32 píxeles finales (botón + margen)
                            int deleteThreshold = cellRect.width - 32; // 24px botón + padding
                            if (clickX >= deleteThreshold) {
                                // Parte derecha -> eliminar (diálogo de confirmación)
                                EliminarEjemplarDialog del = new EliminarEjemplarDialog(
                                        controlador.getControladorNavegacion().getVentana(), controlador, idEjemplar);
                                del.setVisible(true);
                            } else {
                                // Resto -> editar
                                EditarEjemplarDialog d = new EditarEjemplarDialog(
                                        controlador.getControladorNavegacion().getVentana(), controlador, idEjemplar);
                                d.setVisible(true);
                            }
                            // refrescar la vista tras cerrar cualquiera de los diálogos
                            controlador.getControladorNavegacion()
                                    .mostrarEjemplaresParaPublicacion(currentPublicacionId);
                        } catch (NumberFormatException ex) {
                            System.out.println("ID de ejemplar inválido: " + idStr);
                        }
                    }
                }
            }
        });

        // Scroll pane para la tabla
        JScrollPane scroll = new JScrollPane(ejemplaresTable);
        scroll.setBounds(10, 50, 540, 320);
        scroll.setBorder(null);
        ejemplares.add(scroll);

        return ejemplares;
    }

    /**
     * Crea el botón de nuevo ejemplar con su listener.
     *
     * @param panelPanel panel donde mostrar el mensaje de error
     * @return JButton btnNuevoEjemplar
     */
    private JButton crearBotonNuevoEjemplar(final JPanel panelPanel) {
        JButton btnNuevoEjemplar = new JButton("+ NUEVO EJEMPLAR");
        btnNuevoEjemplar.setBounds(380, 8, 160, 28);
        btnNuevoEjemplar.setBackground(Color.decode("#F4791B"));
        btnNuevoEjemplar.setForeground(Color.white);
        btnNuevoEjemplar.setFocusPainted(false);
        btnNuevoEjemplar.setBorder(null);

        // Abrir diálogo de nuevo ejemplar
        btnNuevoEjemplar.addActionListener(evt -> {
            if (currentPublicacionId > 0) {
                NuevoEjemplarDialog d = new NuevoEjemplarDialog(controlador.getControladorNavegacion().getVentana(),
                        controlador, currentPublicacionId);
                d.setVisible(true);
                // refrescar la vista tras cerrar
                controlador.getControladorNavegacion().mostrarEjemplaresParaPublicacion(currentPublicacionId);
            } else {
                JOptionPane.showMessageDialog(panelPanel,
                        "No hay publicación seleccionada para añadir ejemplares", "Aviso",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        return btnNuevoEjemplar;
    }

    /**
     * Renderizador para el estado de ejemplares (badges con colores).
     */
    private class StatusRenderer extends JLabel implements TableCellRenderer {
        /**
         * Constructor de StatusRenderer.
         */
        StatusRenderer() {
            setOpaque(true);
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(final JTable table, final Object value,
                final boolean isSelected, final boolean hasFocus, final int row, final int column) {
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

    /**
     * Renderizador para las acciones de ejemplares (botones editar/eliminar).
     */
    private class ActionsRenderer implements TableCellRenderer {
        /**
         * Panel de celda para los botones de acción.
         */
        private final JPanel panelCell = new JPanel();
        /**
         * Botón de editar dentro del renderizador.
         */
        private final JButton edit = new JButton();
        /**
         * Botón de eliminar dentro del renderizador.
         */
        private final JButton del = new JButton();

        ActionsRenderer() {
            panelCell.setOpaque(false);
            panelCell.setLayout(new FlowLayout(FlowLayout.RIGHT, 6, 6));

            // Botón editar con fondo translúcido pintado manualmente
            java.net.URL editarIconUrl = AppResources.editarPath();
            final JButton editBtn = new JButton() {
                @Override
                protected void paintComponent(final Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setComposite(AlphaComposite.SrcOver);
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
            editBtn.setMargin(new Insets(0, 0, 0, 0));

            // Abrir diálogo de edición de la publicación actual (usa
            // `currentPublicacionId`)
            editBtn.addActionListener(evt -> {
                if (currentPublicacionId > 0) {
                    EditarPublicacionDialog d = new EditarPublicacionDialog(
                            controlador.getControladorNavegacion().getVentana(), controlador, currentPublicacionId);
                    d.setVisible(true);
                    // refrescar para mostrar posibles cambios
                    controlador.getControladorNavegacion().mostrarEjemplaresParaPublicacion(currentPublicacionId);
                } else {
                    JOptionPane.showMessageDialog(panelCell, "No hay publicación seleccionada para editar", "Aviso",
                            JOptionPane.WARNING_MESSAGE);
                }
            });

            // Botón eliminar con fondo translúcido pintado manualmente
            URL borrarIconUrl = AppResources.eliminarPath();
            final JButton delBtn = new JButton() {
                @Override
                protected void paintComponent(final Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setComposite(AlphaComposite.SrcOver);
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
            delBtn.setMargin(new Insets(0, 0, 0, 0));

            panelCell.add(editBtn);
            panelCell.add(delBtn);
        }

        @Override
        public Component getTableCellRendererComponent(final JTable table, final Object value,
                final boolean isSelected, final boolean hasFocus, final int row, final int column) {
            return panelCell;
        }
    }

    /**
     * Carga los datos del resumen de la publicación en la vista.
     * También almacena el id de la publicación en `currentPublicacionId` para
     * que acciones como "Editar" puedan abrir el diálogo apropiado.
     *
     * @param resumen arreglo: titulo,isbn,autores,ciclos,editorial,disponibles,id
     */
    public void cargarPublicacionResumen(final String[] resumen) {
        // Comprobación básica
        if (resumen == null) {
            return;
        }
        // Extraer datos con comprobación de longitud
        String titulo = resumen.length > 0 ? resumen[0] : "";
        String isbn = resumen.length > 1 ? resumen[1] : "";
        String autores = resumen.length > 2 ? resumen[2] : "";
        String editorial = resumen.length > 4 ? resumen[4] : "";
        String disponibles = resumen.length > 5 ? resumen[5] : "";
        String idStr = resumen.length > 6 ? resumen[6] : "";

        // Cargar datos en etiquetas
        if (tituloPublicacion != null) {
            tituloPublicacion.setText(titulo);
        }
        if (isbnPublicacion != null) {
            isbnPublicacion.setText("ISBN: " + isbn);
        }
        if (autoresPublicacion != null) {
            autoresPublicacion.setText(autores == null || autores.isEmpty() ? "" : "Autor: " + autores);
        }

        // Cargar ejemplares reales para la publicación
        int id = -1;
        try {
            id = Integer.parseInt(idStr);
        } catch (Exception e) {
            id = -1;
        }
        if (id > 0) {
            // almacenar el id cargado para acciones posteriores (editar)
            this.currentPublicacionId = id;

            String[][] ejemplaresData = controlador.getControladorEjemplares().obtenerEjemplaresPorPublicacion(id);
            // Limpiar modelo
            for (int i = ejemplaresModel.getRowCount() - 1; i >= 0; i--) {
                ejemplaresModel.removeRow(i);
            }
            // Cargar datos y comprobacion de nulos y vacios
            if (ejemplaresData == null) {
                JOptionPane.showMessageDialog(null,
                        "Error cargando ejemplares de la publicación. Compruebe la conexión a la base de datos.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } else if (ejemplaresData.length == 0) {
                JOptionPane.showMessageDialog(null,
                        "No hay ejemplares disponibles para esta publicación.", "Información",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                for (String[] row : ejemplaresData) {
                    // row: id, num_ejemplar, fecha, estado
                    Object[] fila = new Object[] { "#" + row[0], row[1], row[2], row[3], null };
                    ejemplaresModel.addRow(fila);
                }
            }
        }

    }

}
