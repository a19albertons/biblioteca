package com.biblioteca.vista.pantalla;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.biblioteca.controlador.Controlador;
import com.biblioteca.utilities.BackgroundWorker;
import com.biblioteca.utilities.Fonts;

/**
 * Clase para la vista Panel de control
 */
public class PanelControl {

    /**
     * Controlador de la aplicación
     */
    private final Controlador controlador;

    /**
     * Obtiene el controlador de la aplicación
     *
     * @return el controlador principal
     */
    public Controlador getControlador() {
        return controlador;
    }

    /**
     * Constructor de la vista PanelControl
     *
     * @param controlador controlador principal (final)
     */
    public PanelControl(final Controlador controlador) {
        this.controlador = controlador;
    }

    /**
     * Etiqueta valor 1 (préstamos hoy)
     */
    private JLabel valor1;
    /**
     * Etiqueta valor 2 (préstamos pendientes)
     */
    private JLabel valor2;
    /**
     * Etiqueta valor 3 (socios activos)
     */
    private JLabel valor3;
    /**
     * Tabla de últimos movimientos
     */
    private JTable table;
    /**
     * Modelo de la tabla de últimos movimientos
     */
    private DefaultTableModel model;

    /**
     * Nombres de las columnas de la tabla
     */
    private String[] columnNames = { "ID EJEMPLAR", "LIBRO", "ESTADO" };

    /**
     * Renderer para la columna estado
     */
    private DefaultTableCellRenderer estadoRenderer;

    /**
     * Muestra la pantalla del panel de control con tarjetas y resúmenes
     *
     * @return JPanel con el panel de control
     */
    public JPanel pantalla() {
        JPanel panel = crearPanelPrincipal();
        panel.add(crearEncabezado());
        panel.add(crearCardPrestamosHoy());
        panel.add(crearCardPendientes());
        panel.add(crearCardSociosActivos());
        panel.add(crearPanelMovimientos());

        JLabel ultimos = new JLabel("Últimos movimientos");
        ultimos.setFont(ultimos.getFont().deriveFont(18f));
        ultimos.setBounds(30, 220, 200, 30);
        panel.add(ultimos);
        return panel;
    }

    /**
     * Crea el panel principal del control
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
     * Crea el encabezado del panel
     *
     * @return JPanel con el título
     */
    private JPanel crearEncabezado() {
        JPanel encabezado = new JPanel();
        encabezado.setSize(new Dimension(600, 60));
        encabezado.setBackground(Color.white);
        encabezado.setLayout(null);
        encabezado.setBounds(0, 0, 600, 60);

        JLabel titulo = new JLabel("Panel de Control");
        titulo.setFont(titulo.getFont().deriveFont(24f));
        titulo.setBounds(10, 10, 200, 40);
        encabezado.add(titulo);
        return encabezado;
    }

    /**
     * Crea la tarjeta de préstamos hoy
     *
     * @return JPanel con la tarjeta
     */
    private JPanel crearCardPrestamosHoy() {
        JPanel card1 = new JPanel();
        card1.setSize(170, 100);
        card1.setLayout(null);
        card1.setBackground(Color.white);
        card1.setBounds(30, 100, 170, 100);
        card1.setBorder(BorderFactory.createMatteBorder(5, 0, 0, 0, Color.decode("#468DAE")));

        JLabel tema1 = new JLabel("Prestamos hoy");
        tema1.setBounds(10, 10, 150, 30);
        tema1.setFont(new Font("Open Sans", Font.PLAIN, 12));
        card1.add(tema1);

        valor1 = new JLabel("...");
        valor1.setFont(valor1.getFont().deriveFont(36f));
        valor1.setBounds(10, 40, 150, 50);
        card1.add(valor1);

        cargarCardPrestamosHoy();
        return card1;
    }

    /**
     * Carga los datos de la tarjeta de préstamos hoy
     */
    private void cargarCardPrestamosHoy() {
        BackgroundWorker.run(
                () -> controlador.getControladorPanelControl().obtenerPrestamosHoy(),
                result -> {
                    if ("-1".equals(result)) {
                        JOptionPane.showMessageDialog(null,
                                "Error cargando número de préstamos de hoy. Compruebe la conexión a la base de datos.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        valor1.setText("—");
                    } else {
                        valor1.setText(result);
                    }
                },
                ex -> {
                    JOptionPane.showMessageDialog(null,
                            "Error cargando número de préstamos de hoy. Compruebe la conexión a la base de datos.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    valor1.setText("—");
                });
    }

    /**
     * Crea la tarjeta de pendientes
     *
     * @return JPanel con la tarjeta
     */
    private JPanel crearCardPendientes() {
        JPanel card2 = new JPanel();
        card2.setSize(170, 100);
        card2.setLayout(null);
        card2.setBackground(Color.white);
        card2.setBounds(215, 100, 170, 100);
        card2.setBorder(BorderFactory.createMatteBorder(5, 0, 0, 0, Color.decode("#F4791B")));

        JLabel tema2 = new JLabel("Pendientes");
        tema2.setBounds(10, 10, 150, 30);
        tema2.setFont(Fonts.openSans(12f));
        card2.add(tema2);

        valor2 = new JLabel("...");
        valor2.setFont(valor2.getFont().deriveFont(36f));
        valor2.setBounds(10, 40, 150, 50);
        valor2.setForeground(Color.decode("#F4791B"));
        card2.add(valor2);

        cargarCardPendientes();
        return card2;
    }

    /**
     * Carga los datos de la tarjeta de pendientes
     */
    private void cargarCardPendientes() {
        BackgroundWorker.run(
                () -> controlador.getControladorPanelControl().obtenerPrestamosPendientes(),
                result -> {
                    if ("-1".equals(result)) {
                        JOptionPane.showMessageDialog(null,
                                "Error cargando número de préstamos pendientes. Compruebe la conexión a la base de datos.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        valor2.setText("—");
                    } else {
                        valor2.setText(result);
                    }
                },
                ex -> {
                    JOptionPane.showMessageDialog(null,
                            "Error cargando número de préstamos pendientes. Compruebe la conexión a la base de datos.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    valor2.setText("—");
                });
    }

    /**
     * Crea la tarjeta de socios activos
     *
     * @return JPanel con la tarjeta
     */
    private JPanel crearCardSociosActivos() {
        JPanel card3 = new JPanel();
        card3.setSize(170, 100);
        card3.setLayout(null);
        card3.setBackground(Color.white);
        card3.setBounds(400, 100, 170, 100);
        card3.setBorder(BorderFactory.createMatteBorder(5, 0, 0, 0, Color.decode("#2BC187")));

        JLabel tema3 = new JLabel("Socios activos");
        tema3.setBounds(10, 10, 150, 30);
        tema3.setFont(Fonts.openSans(12f));
        card3.add(tema3);

        valor3 = new JLabel("...");
        valor3.setFont(valor3.getFont().deriveFont(36f));
        valor3.setBounds(10, 40, 150, 50);
        card3.add(valor3);

        cargarCardSociosActivos();
        return card3;
    }

    /**
     * Carga los datos de la tarjeta de socios activos
     */
    private void cargarCardSociosActivos() {
        BackgroundWorker.run(
                () -> controlador.getControladorPanelControl().obtenerTotalSociosActivos(),
                result -> {
                    if ("-1".equals(result)) {
                        JOptionPane.showMessageDialog(null,
                                "Error cargando número de socios activos. Compruebe la conexión a la base de datos.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        valor3.setText("—");
                    } else {
                        valor3.setText(result);
                    }
                },
                ex -> {
                    JOptionPane.showMessageDialog(null,
                            "Error cargando número de socios activos. Compruebe la conexión a la base de datos.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    valor3.setText("—");
                });
    }

    /**
     * Crea el panel de últimos movimientos con tabla
     *
     * @return JPanel con la tabla
     */
    private JPanel crearPanelMovimientos() {
        JPanel movimientosPanel = new JPanel();
        movimientosPanel.setBackground(Color.white);
        movimientosPanel.setLayout(null);
        movimientosPanel.setBounds(30, 260, 540, 300);
        movimientosPanel.setBorder(BorderFactory.createLineBorder(Color.decode("#E6ECEF")));

        // Datos vacios de inicio para la tabla
        String[][] initialData = new String[0][0];
        model = new DefaultTableModel(initialData, columnNames) {
            @Override
            public boolean isCellEditable(final int row, final int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(28);
        table.setShowGrid(false);
        table.setFillsViewportHeight(true);
        table.setIntercellSpacing(new java.awt.Dimension(0, 0));
        table.setBackground(Color.white);
        table.setForeground(Color.decode("#666666"));
        table.setFont(Fonts.openSans(12f));

        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.white);
        header.setForeground(Color.decode("#468DAE"));
        header.setFont(header.getFont().deriveFont(Font.BOLD, 12f));
        header.setReorderingAllowed(false);

        table.setModel(model);
        if (table.getColumnModel().getColumnCount() > 0) {
            table.getColumnModel().getColumn(0).setPreferredWidth(80);
            table.getColumnModel().getColumn(1).setPreferredWidth(360);
            table.getColumnModel().getColumn(2).setPreferredWidth(80);
        }

        estadoRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(final JTable table, final Object value,
                    final boolean isSelected,
                    final boolean hasFocus, final int row, final int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String s = (value != null) ? value.toString().toUpperCase() : "";
                setHorizontalAlignment(SwingConstants.CENTER);
                setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
                if ("DEVUELTO".equals(s)) {
                    setBackground(Color.decode("#E6FFF0"));
                    setForeground(Color.decode("#2BC187"));
                } else if ("PRESTADO".equals(s)) {
                    setBackground(Color.decode("#FFF4E6"));
                    setForeground(Color.decode("#F4791B"));
                } else {
                    setBackground(Color.white);
                    setForeground(Color.decode("#666666"));
                }
                setOpaque(true);
                return this;
            }
        };

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(10, 10, 520, 280);
        scroll.setBorder(null);
        movimientosPanel.add(scroll);

        cargarTablaMovimientos();
        return movimientosPanel;
    }

    /**
     * Carga los datos de la tabla de movimientos
     */
    private void cargarTablaMovimientos() {
        BackgroundWorker.run(
                () -> controlador.getControladorPanelControl().obtenerUltimosMovimientos(),
                result -> {
                    String[][] data = result;
                    if (data == null) {
                        JOptionPane.showMessageDialog(null,
                                "Error cargando los últimos movimientos. Compruebe la conexión a la base de datos.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        data = new String[0][0];
                    } else if (data.length == 0) {
                        JOptionPane.showMessageDialog(null,
                                "No hay movimientos para mostrar.", "Información",
                                JOptionPane.INFORMATION_MESSAGE);
                        data = new String[0][0];
                    }
                    model.setDataVector(data, columnNames);
                    // Reaplicar renderer a la columna estado
                    if (table.getColumnModel().getColumnCount() > 2) {
                        table.getColumnModel().getColumn(2).setCellRenderer(estadoRenderer);
                    }
                },
                ex -> {
                    JOptionPane.showMessageDialog(null,
                            "Error cargando los últimos movimientos. Compruebe la conexión a la base de datos.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                });
    }

    /**
     * Refresca los datos mostrados en el panel de control (tarjetas y tabla de
     * movimientos).
     */
    public void refrescarPanel() {
        // Cargar tabla de movimientos
        cargarTablaMovimientos();

        // Actualizar tarjetas asíncronamente
        valor1.setText("...");
        BackgroundWorker.run(
                () -> controlador.getControladorPanelControl().obtenerPrestamosHoy(),
                // Actualizar UI con resultados
                result -> {
                    // Manejo de errores y actualización del label
                    if ("-1".equals(result)) {
                        JOptionPane.showMessageDialog(null,
                                "Error cargando número de préstamos de hoy. Compruebe la conexión a la base de datos.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        valor1.setText("—");
                    } else {
                        valor1.setText(result);
                    }
                },
                ex -> {
                    JOptionPane.showMessageDialog(null,
                            "Error cargando número de préstamos de hoy. Compruebe la conexión a la base de datos.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    valor1.setText("—");
                });

        valor2.setText("...");
        BackgroundWorker.run(
                () -> controlador.getControladorPanelControl().obtenerPrestamosPendientes(),
                // Actualizar UI con resultados
                result -> {
                    // Manejo de errores y actualización del label
                    if ("-1".equals(result)) {
                        JOptionPane.showMessageDialog(null,
                                "Error cargando número de préstamos pendientes. Compruebe la conexión a la base de datos.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        valor2.setText("—");
                    } else {
                        valor2.setText(result);
                    }
                },
                ex -> {
                    JOptionPane.showMessageDialog(null,
                            "Error cargando número de préstamos pendientes. Compruebe la conexión a la base de datos.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    valor2.setText("—");
                });

        valor3.setText("...");
        com.biblioteca.utilities.BackgroundWorker.run(
                () -> controlador.getControladorPanelControl().obtenerTotalSociosActivos(),
                // Actualizar UI con resultados
                result -> {
                    // Manejo de errores y actualización del label
                    if ("-1".equals(result)) {
                        JOptionPane.showMessageDialog(null,
                                "Error cargando número de socios activos. Compruebe la conexión a la base de datos.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        valor3.setText("—");
                    } else {
                        valor3.setText(result);
                    }
                },
                ex -> {
                    JOptionPane.showMessageDialog(null,
                            "Error cargando número de socios activos. Compruebe la conexión a la base de datos.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    valor3.setText("—");
                });

    }

}
