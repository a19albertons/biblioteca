package com.example.vista;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.example.controlador.Controlador;

/**
 * Clase para la vista Gestión de usuarios
 */
public class GestionUsuarios {

    /**
     * Controlador de la aplicación
     */
    Controlador controlador;

    /**
     * Modelo de tabla que contiene los usuarios (se guarda para permitir refrescar)
     */
    private javax.swing.table.DefaultTableModel usuariosModel;

    /**
     * Tabla que muestra los usuarios (se guarda para permitir refrescar)
     */
    private javax.swing.JTable usuariosTable;

    /**
     * Constructor de la vista GestionUsuarios
     *
     * @param controlador controlador principal
     */
    public GestionUsuarios(Controlador controlador) {
        this.controlador = controlador;
    }

    /**
     * Muestra la pantalla de gestión de usuarios
     *
     * @return JPanel con la interfaz de gestión de usuarios
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
        JLabel titulo = new JLabel("Gestión de Usuarios");
        titulo.setFont(titulo.getFont().deriveFont(24f));
        titulo.setBounds(30, 10, 300, 40);
        encabezado.add(titulo);

        // Botón nuevo socio
        JButton btnNuevoPub = new JButton("+ NUEVO SOCIO");
        btnNuevoPub.setBounds(420, 15, 140, 30);
        btnNuevoPub.setBackground(Color.decode("#F4791B"));
        btnNuevoPub.setForeground(Color.WHITE);
        btnNuevoPub.setFocusPainted(false);
        btnNuevoPub.setBorder(null);
        encabezado.add(btnNuevoPub);

        // Abrir diálogo de nuevo usuario
        btnNuevoPub.addActionListener(evt -> {
            NuevoUsuarioDialog d = new NuevoUsuarioDialog(controlador.getControladorNavegacion().getVentana(), controlador);
            d.setVisible(true);
            // Refrescar listado tras cerrar diálogo
            controlador.getControladorNavegacion().refrescarUsuarios();
        });

        // Listado usuarios
        JPanel listaUsuarios = new JPanel();
        listaUsuarios.setSize(540, 480);
        listaUsuarios.setBackground(Color.white);
        listaUsuarios.setLayout(null);
        listaUsuarios.setBounds(20, 80, 540, 480);



        // Tabla con columnas y datos de ejemplo
        String[] cols = new String[] {"DNI", "NOMBRE Y APELLIDO", "TIPO", "ESTADO", "ACCIONES"};
        // La DAO devuelve filas en formato: [id, dni, nombre_completo, sancion_activa, tipo]
        // Reservamos el campo `id` para los botones de acciones (columna invisible para ahora)
        String[][] rawData = controlador.getControladorGestionUsuarios().obtenerUsuariosYEstadoSancionActiva();
        if (rawData == null) {
            rawData = new String[0][0];
        }
        // Construir la matriz visible (omitimos el id y colocamos un placeholder para ACCIONES)
        String[][] data = new String[rawData.length][5];
        for (int i = 0; i < rawData.length; i++) {
            String[] r = rawData[i];
            String dni = (r.length > 1 && r[1] != null) ? r[1] : "";
            String nombre = (r.length > 2 && r[2] != null) ? r[2] : "";
            String sancion = (r.length > 3 && r[3] != null) ? r[3] : "";
            String tipo = (r.length > 4 && r[4] != null) ? r[4] : "";
            data[i][0] = dni;            // DNI
            data[i][1] = nombre;         // NOMBRE Y APELLIDO
            data[i][2] = tipo;           // TIPO (mostrar en columna 3)
            data[i][3] = sancion;        // ESTADO
            data[i][4] = "";           // ACCIONES (placeholder, renderizado con botones en ActionsRenderer)
        }

        // Guardar modelo y tabla como campos para permitir refrescar desde fuera
        final DefaultTableModel modelRef = new DefaultTableModel(data, cols) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        final JTable table = new JTable(modelRef);
        // Exponerlos mediante setters locales para usar en refrescarUsuarios
        this.usuariosModel = modelRef;
        this.usuariosTable = table;

        table.setRowHeight(48);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        // Renderers personalizados
        class StatusRenderer extends JLabel implements TableCellRenderer {
            public StatusRenderer() {
                setOpaque(true);
                setHorizontalAlignment(SwingConstants.CENTER);
            }

            // Define los colores del campo estado
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                String s = (value != null) ? value.toString() : "";
                setText(s);
                switch (s) {
                    case "ACTIVO":
                        setBackground(Color.decode("#E6FFF0"));
                        setForeground(Color.decode("#2BC187"));
                        break;
                    case "SANCIONADO":
                        setBackground(Color.decode("#FFF4E6"));
                        setForeground(Color.decode("#F4791B"));
                        break;
                    default:
                        setBackground(Color.white);
                        setForeground(Color.black);
                }
                setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
                return this;
            }
        }

        // Renderer para los botones de acciones
        class ActionsRenderer implements TableCellRenderer {
            private final JPanel panelCell = new JPanel();

            public ActionsRenderer() {
                panelCell.setOpaque(false);
                panelCell.setLayout(new FlowLayout(FlowLayout.RIGHT, 6, 6));

                // Edit button (translucent)
                java.net.URL editarIconUrl = getClass().getResource("/editar.png");
                final JButton editBtn = new JButton() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setComposite(AlphaComposite.SrcOver);
                        g2.setColor(new Color(70, 141, 174, 102));
                        g2.fillRect(0, 0, getWidth(), getHeight());
                        g2.dispose();
                        super.paintComponent(g);
                    }
                };
                if (editarIconUrl != null) {
                    Image img = new ImageIcon(editarIconUrl).getImage().getScaledInstance(12, 12,
                            Image.SCALE_SMOOTH);
                    editBtn.setIcon(new ImageIcon(img));
                }
                editBtn.setPreferredSize(new Dimension(24, 24));
                editBtn.setToolTipText("Editar");
                editBtn.setBorder(null);
                editBtn.setFocusPainted(false);
                editBtn.setContentAreaFilled(false);
                editBtn.setOpaque(false);
                editBtn.setMargin(new Insets(0, 0, 0, 0));

                // Delete button (translucent)
                java.net.URL borrarIconUrl = getClass().getResource("/borrar.png");
                final JButton delBtn = new JButton() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setComposite(AlphaComposite.SrcOver);
                        g2.setColor(new Color(192, 57, 43, 102));
                        g2.fillRect(0, 0, getWidth(), getHeight());
                        g2.dispose();
                        super.paintComponent(g);
                    }
                };
                if (borrarIconUrl != null) {
                    Image img = new ImageIcon(borrarIconUrl).getImage().getScaledInstance(12, 12,
                            Image.SCALE_SMOOTH);
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

            // Devuelve el panel con los botones
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                return panelCell;
            }
        }

        // Asignar renderers a las columnas correspondientes
        table.getColumnModel().getColumn(3).setCellRenderer(new StatusRenderer());
        table.getColumnModel().getColumn(4).setCellRenderer(new ActionsRenderer());

        // Implementar click en ACCIONES para editar/eliminar (similar a Ejemplares)
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if (row >= 0 && col == 4) {
                    // Para ahora solo mostrar mensaje
                    javax.swing.JOptionPane.showMessageDialog(table, "Acción sobre usuario seleccionada (pendiente implementación)", "Info", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        // Scroll que contiene la tabla (ocupa la parte superior del panel ahora que no hay detalle)
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(10, 10, 520, 460);
        scroll.setBorder(null);
        listaUsuarios.add(scroll);

        // Guardar referencias para refrescar posteriormente (ya guardadas arriba)
        // Agregar paneles al panel principal
        panel.add(encabezado);
        panel.add(listaUsuarios);

        return panel;
    }

    /**
     * Refresca los datos de la tabla de usuarios recargando la consulta.
     */
    public void refrescarUsuarios() {
        // Verificar que el modelo existe
        if (this.usuariosModel == null)
            return;
        String[][] rawData = controlador.getControladorGestionUsuarios().obtenerUsuariosYEstadoSancionActiva();
        // Comprobar null
        if (rawData == null)
            rawData = new String[0][0];
        // Limpiar modelo
        for (int i = usuariosModel.getRowCount() - 1; i >= 0; i--) {
            usuariosModel.removeRow(i);
        }
        // Rellenar con datos nuevos
        for (String[] r : rawData) {
            String dni = (r.length > 1 && r[1] != null) ? r[1] : "";
            String nombre = (r.length > 2 && r[2] != null) ? r[2] : "";
            String sancion = (r.length > 3 && r[3] != null) ? r[3] : "";
            String tipo = (r.length > 4 && r[4] != null) ? r[4] : "";
            usuariosModel.addRow(new Object[] { dni, nombre, tipo, sancion, "" });
        }
    }

}
