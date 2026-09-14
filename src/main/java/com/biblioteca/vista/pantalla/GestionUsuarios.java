package com.biblioteca.vista.pantalla;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
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
import com.biblioteca.utilities.BackgroundWorker;
import com.biblioteca.vista.JSwing.JButtonBorrar;
import com.biblioteca.vista.JSwing.JButtonEditar;
import com.biblioteca.vista.dialogo.EditarUsuarioDialog;
import com.biblioteca.vista.dialogo.EliminarUsuarioDialog;
import com.biblioteca.vista.dialogo.NuevoUsuarioDialog;

/**
 * Clase para la vista Gestión de usuarios
 */
public class GestionUsuarios {

    /**
     * Controlador de la aplicación
     */
    private final Controlador controlador;

    /**
     * Renderizador para el campo estado
     */
    private class StatusRenderer extends JLabel implements TableCellRenderer {
        StatusRenderer() {
            setOpaque(true);
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected,
                final boolean hasFocus, final int row, final int column) {
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
                case "BAJA":
                    setBackground(new Color(70, 141, 174, 102));
                    setForeground(new Color(70, 141, 174));
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
     * Renderizador para los botones de acciones
     */
    private class ActionsRenderer implements TableCellRenderer {
        /**
         * Panel de la celda con los botones de acción
         */
        private final JPanel panelCell = new JPanel();

        ActionsRenderer() {
            panelCell.setOpaque(false);
            panelCell.setLayout(new FlowLayout(FlowLayout.RIGHT, 6, 6));

            final JButtonEditar editBtn = new JButtonEditar();

            final JButton delBtn = new JButtonBorrar();

            panelCell.add(editBtn);
            panelCell.add(delBtn);
        }

        @Override
        public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected,
                final boolean hasFocus, final int row, final int column) {
            return panelCell;
        }
    }

    /**
     * Getters para el controlador
     * 
     * @return controlador principal
     */
    Controlador getControlador() {
        return controlador;
    }

    /**
     * Modelo de tabla que contiene los usuarios (se guarda para permitir refrescar)
     */
    private DefaultTableModel usuariosModel;

    /**
     * Tabla que muestra los usuarios (se guarda para permitir refrescar)
     */
    private JTable usuariosTable;

    /**
     * Constructor de la vista GestionUsuarios
     *
     * @param controlador controlador principal
     */
    public GestionUsuarios(final Controlador controlador) {
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

        // Crear panel de encabezado
        JPanel encabezado = crearPanelEncabezado();

        // Crear panel de lista de usuarios
        JPanel listaUsuarios = crearPanelListaUsuarios();

        // Agregar paneles al panel principal
        panel.add(encabezado);
        panel.add(listaUsuarios);

        return panel;
    }

    private JPanel crearPanelEncabezado() {
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
            NuevoUsuarioDialog d = new NuevoUsuarioDialog(controlador.getControladorNavegacion().getVentana(),
                    controlador);
            d.setVisible(true);
            // Refrescar listado tras cerrar diálogo
            controlador.getControladorNavegacion().refrescarUsuarios();
        });

        return encabezado;
    }

    private JPanel crearPanelListaUsuarios() {
        JPanel listaUsuarios = new JPanel();
        listaUsuarios.setSize(540, 480);
        listaUsuarios.setBackground(Color.white);
        listaUsuarios.setLayout(null);
        listaUsuarios.setBounds(20, 80, 540, 480);

        // Tabla con columnas y datos (cargados en background)
        String[] cols = new String[] { "ID", "DNI", "NOMBRE Y APELLIDO", "TIPO", "ESTADO", "ACCIONES" };
        String[][] initialData = new String[0][0];

        // Guardar modelo y tabla como campos para permitir refrescar desde fuera
        final DefaultTableModel modelRef = new DefaultTableModel(initialData, cols) {
            @Override
            public boolean isCellEditable(final int row, final int column) {
                return false;
            }
        };
        final JTable table = new JTable(modelRef);
        // Exponerlos mediante setters locales para usar en refrescarUsuarios
        this.usuariosModel = modelRef;
        this.usuariosTable = table;

        // Cargar usuarios en background
        BackgroundWorker.run(
                () -> controlador.getControladorGestionUsuarios().obtenerUsuariosYEstadoSancionActiva(),
                rawData -> cargarUsuariosEnTabla(rawData),
                ex -> mostrarErrorCargaUsuarios(ex));

        table.setRowHeight(48);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        // Ocultar columna ID (columna 0)
        if (table.getColumnModel().getColumnCount() > 0) {
            table.getColumnModel().getColumn(0).setMinWidth(0);
            table.getColumnModel().getColumn(0).setMaxWidth(0);
            table.getColumnModel().getColumn(0).setPreferredWidth(0);
        }

        // Asignar renderers a las columnas correspondientes
        // Columna ESTADO ahora es la 4 y ACCIONES es la 5
        table.getColumnModel().getColumn(4).setCellRenderer(new StatusRenderer());
        table.getColumnModel().getColumn(5).setCellRenderer(new ActionsRenderer());

        // Implementar click en ACCIONES para editar/eliminar (similar a Ejemplares)
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                handleActionsClick(table, usuariosModel, e);
            }
        });

        // Scroll que contiene la tabla (ocupa la parte superior del panel ahora que no
        // hay detalle)
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(10, 10, 520, 460);
        scroll.setBorder(null);
        listaUsuarios.add(scroll);

        return listaUsuarios;
    }

    private void handleActionsClick(final JTable table, final DefaultTableModel model, final MouseEvent e) {
        final int row = table.rowAtPoint(e.getPoint());
        final int col = table.columnAtPoint(e.getPoint());
        // Asegurarse de que es la columna ACCIONES
        if (row >= 0 && col == 5) {
            Object idObj = model.getValueAt(row, 0);
            // Obtener ID usuario de la fila
            if (idObj != null) {
                String idStr = idObj.toString();
                try {
                    final int idUsuario = Integer.parseInt(idStr.trim());

                    // Determinar si se hizo click en editar o eliminar
                    Rectangle cellRect = table.getCellRect(row, col, true);
                    final int clickX = e.getX() - cellRect.x;
                    final int deleteThreshold = cellRect.width - 32; // 24px botón + padding
                    // Determinar si se hizo click en editar o eliminar
                    if (clickX >= deleteThreshold) {
                        // Parte derecha -> eliminar (desactivar)
                        final EliminarUsuarioDialog del = new EliminarUsuarioDialog(
                                controlador.getControladorNavegacion().getVentana(), controlador, idUsuario);
                        del.setVisible(true);
                    } else {
                        // Parte izquierda -> editar
                        final EditarUsuarioDialog d = new EditarUsuarioDialog(
                                controlador.getControladorNavegacion().getVentana(), controlador, idUsuario);
                        d.setVisible(true);
                    }
                    // Refrescar listado tras cerrar diálogo
                    controlador.getControladorNavegacion().refrescarUsuarios();
                } catch (NumberFormatException ex) {
                    System.out.println("ID de usuario inválido: " + idStr);
                }
            }
        }
    }

    private void cargarUsuariosEnTabla(final String[][] rawData) {
        // Manejo de errores y mensajes
        if (rawData == null) {
            JOptionPane.showMessageDialog(null,
                    "Error cargando la lista de usuarios. Compruebe la conexión a la base de datos.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        } else if (rawData.length == 0) {
            JOptionPane.showMessageDialog(null,
                    "No hay usuarios registrados para mostrar.", "Información",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        // Usar método del controlador
        controlador.getControladorGestionUsuarios().cargarDatosEnTabla(rawData, usuariosModel);
        
        // Asegurarse de refrescar la tabla si está disponible
        if (this.usuariosTable != null) {
            this.usuariosTable.revalidate();
            this.usuariosTable.repaint();
        }
    }

    private void mostrarErrorCargaUsuarios(final Exception ex) {
        JOptionPane.showMessageDialog(null,
                "Error cargando la lista de usuarios. Compruebe la conexión a la base de datos.", "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Refresca los datos de la tabla de usuarios recargando la consulta.
     */
    public void refrescarUsuarios() {
        // Verificar que el modelo existe
        if (this.usuariosModel == null) {
            return;
        }

        // Cargar en background
        BackgroundWorker.run(
                () -> controlador.getControladorGestionUsuarios().obtenerUsuariosYEstadoSancionActiva(),
                // Actualizar UI con resultados
                rawData -> {
                    // Manejo de errores y mensajes
                    if (rawData == null) {
                        rawData = new String[0][0];
                    }

                    // Usar método del controlador
                    controlador.getControladorGestionUsuarios().cargarDatosEnTabla(rawData, usuariosModel);
                    
                    // Asegurarse de refrescar la tabla si está disponible
                    if (this.usuariosTable != null) {
                        this.usuariosTable.revalidate();
                        this.usuariosTable.repaint();
                    }
                },
                ex -> JOptionPane.showMessageDialog(null,
                        "Error cargando la lista de usuarios. Compruebe la conexión a la base de datos.", "Error",
                        JOptionPane.ERROR_MESSAGE));
    }

}
