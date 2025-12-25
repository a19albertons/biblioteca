package com.example.vista;

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

import com.example.controlador.Controlador;
import com.example.utilities.Fonts;

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
    // Componentes que deben mantenerse para refresco
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

        // Card 1 - Prestamos hoy (fondo blanco, borde superior azul)
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

        // Valor de préstamos hoy + comprobación de error
        String prestamosHoyStr = controlador.getControladorPanelControl().obtenerPrestamosHoy();
        if ("-1".equals(prestamosHoyStr)) {
            JOptionPane.showMessageDialog(null,
                    "Error cargando número de préstamos de hoy. Compruebe la conexión a la base de datos.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            prestamosHoyStr = "—";
        }
        valor1 = new JLabel(prestamosHoyStr);
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
        tema2.setFont(Fonts.openSans(12f));
        card2.add(tema2);

        // Valor de préstamos pendientes + comprobación de error
        String pendientesStr = controlador.getControladorPanelControl().obtenerPrestamosPendientes();
        if ("-1".equals(pendientesStr)) {
            JOptionPane.showMessageDialog(null,
                    "Error cargando número de préstamos pendientes. Compruebe la conexión a la base de datos.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            pendientesStr = "—";
        }
        valor2 = new JLabel(pendientesStr);
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
        tema3.setFont(Fonts.openSans(12f));
        card3.add(tema3);

        // Valor de socios activos + comprobación de error
        String sociosStr = controlador.getControladorPanelControl().obtenerTotalSociosActivos();
        if ("-1".equals(sociosStr)) {
            JOptionPane.showMessageDialog(null,
                    "Error cargando número de socios activos. Compruebe la conexión a la base de datos.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            sociosStr = "—";
        }
        valor3 = new JLabel(sociosStr);
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

        // Tabla de últimos movimientos
        // Encabezado y datos
        String[] columnNames = new String[] { "ID EJEMPLAR", "LIBRO", "ESTADO" };
        String[][] data = controlador.getControladorPanelControl().obtenerUltimosMovimientos();
        // Comprobación de error en datos nulos y vacios
        if (data == null) {
            JOptionPane.showMessageDialog(null,
                    "Error cargando los últimos movimientos. Compruebe la conexión a la base de datos.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            data = new String[0][0];
        } else if (data.length == 0) {
            JOptionPane.showMessageDialog(null,
                    "No hay movimientos para mostrar.", "Información",
                    JOptionPane.INFORMATION_MESSAGE);
            data = new String[0][0];
        }

        // Modificación del modelo para que no sea editable
        model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        // Establece una serie de modificadores sobre la tabla como colores, fuentes,
        // etc
        table = new JTable(model);
        table.setRowHeight(28);
        table.setShowGrid(false);
        table.setFillsViewportHeight(true);
        table.setIntercellSpacing(new java.awt.Dimension(0, 0));
        table.setBackground(Color.white);
        table.setForeground(Color.decode("#666666"));
        table.setFont(Fonts.openSans(12f));
        // Header style
        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.white);
        header.setForeground(Color.decode("#468DAE"));
        header.setFont(header.getFont().deriveFont(Font.BOLD, 12f));
        header.setReorderingAllowed(false);

        // Ajustar anchos
        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(360);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);

        // Renderer para columna estado
        // Customiza como se ven las celdas de la columna estado
        DefaultTableCellRenderer estadoRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
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
        table.getColumnModel().getColumn(2).setCellRenderer(estadoRenderer);

        // Hace que se pueda hacer scroll en la tabla. Esto sucede si hay 10 o más filas
        // de la consutla a la bd actualmente limitado a 9 para evitarlo
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(10, 10, 520, 280);
        scroll.setBorder(null);
        movimientosPanel.add(scroll);

        // Añade todo al panel principal
        panel.add(encabezado);
        panel.add(card1);
        panel.add(card2);
        panel.add(card3);
        panel.add(movimientosPanel);
        return panel;
    }

    /**
     * Refresca los datos mostrados en el panel de control (tarjetas y tabla de
     * movimientos).
     */
    public void refrescarPanel() {
        // Actualizar tarjetas con comprobación de errores
        String pHoy = controlador.getControladorPanelControl().obtenerPrestamosHoy();
        if ("-1".equals(pHoy)) {
            JOptionPane.showMessageDialog(null,
                    "Error cargando número de préstamos de hoy. Compruebe la conexión a la base de datos.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            pHoy = "—";
        }
        valor1.setText(pHoy);

        String pPend = controlador.getControladorPanelControl().obtenerPrestamosPendientes();
        if ("-1".equals(pPend)) {
            JOptionPane.showMessageDialog(null,
                    "Error cargando número de préstamos pendientes. Compruebe la conexión a la base de datos.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            pPend = "—";
        }
        valor2.setText(pPend);

        String socios = controlador.getControladorPanelControl().obtenerTotalSociosActivos();
        if ("-1".equals(socios)) {
            JOptionPane.showMessageDialog(null,
                    "Error cargando número de socios activos. Compruebe la conexión a la base de datos.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            socios = "—";
        }
        valor3.setText(socios);

        // Actualizar tabla
        String[][] data = controlador.getControladorPanelControl().obtenerUltimosMovimientos();
        // Comprobación de error en datos nulos y vacios
        if (data == null) {
            JOptionPane.showMessageDialog(null,
                    "Error cargando los últimos movimientos. Compruebe la conexión a la base de datos.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            data = new String[0][0];
        } else if (data.length == 0) {
            JOptionPane.showMessageDialog(null,
                    "No hay movimientos para mostrar.", "Información",
                    JOptionPane.INFORMATION_MESSAGE);
            data = new String[0][0];
        }
        // Reemplazar todos los datos del modelo
        model.setDataVector(data, new String[] { "ID EJEMPLAR", "LIBRO", "ESTADO" });

        // Reaplicar el renderer a la columna estado (porque cambiar model puede resetearla en algunas LAF)
        DefaultTableCellRenderer estadoRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
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
        if (table.getColumnModel().getColumnCount() > 2) {
            table.getColumnModel().getColumn(2).setCellRenderer(estadoRenderer);
        }
    }

}
