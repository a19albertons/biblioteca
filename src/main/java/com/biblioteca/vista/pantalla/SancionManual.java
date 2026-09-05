package com.biblioteca.vista.pantalla;

import java.awt.Color;
import java.awt.Dimension;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import com.biblioteca.controlador.Controlador;

/**
 * Clase para la vista Sanción manual
 */
public class SancionManual {

    /**
     * Controlador de la aplicación
     */
    private Controlador controlador;

    /**
     * Campo para mostrar el resultado de la selección del socio
     */
    private JLabel resultadoSocio;
    /**
     * Campo para almacenar el ID del usuario seleccionado
     */
    private int[] usuarioSeleccionado;

    /**
     * Campo de la interfaz para seleccionar el motivo de la sanción
     */
    private JComboBox<String> comboMotivo;
    /**
     * Campo de la interfaz para ingresar el ID del ejemplar sancionado
     */
    private JTextField txtEjemplar;
    /**
     * Campo de la interfaz para ingresar la descripción de la sanción
     */
    private JTextField txtDescripcion;
    /**
     * Campo de la interfaz para ingresar la fecha de finalización de la sanción
     */
    private JTextField txtFechaFin;

    /**
     * Devuelve el controlador
     *
     * @return Devuelve el controlador principal de la aplicación
     */
    public Controlador getControlador() {
        return controlador;
    }

    /**
     * Constructor de la vista SancionManual
     *
     * @param controlador controlador principal
     */
    public SancionManual(final Controlador controlador) {
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

        JPanel encabezado = createHeaderPanel();
        JPanel formularioSancion = createFormPanel();

        addStepLabels(formularioSancion);
        createUserSelectionButton(formularioSancion);
        createFormFields(formularioSancion);
        createButtons(formularioSancion);

        panel.add(encabezado);
        panel.add(formularioSancion);

        return panel;
    }

    /**
     * Crea el panel de encabezado con el título "Panel de Control".
     *
     * @return JPanel encabezado panel
     */
    private JPanel createHeaderPanel() {
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
     * Crea el panel de formulario para sanciones manuales.
     *
     * @return JPanel formularioSancion panel
     */
    private JPanel createFormPanel() {
        JPanel formularioSancion = new JPanel();
        formularioSancion.setSize(540, 450);
        formularioSancion.setBounds(30, 90, 540, 450);
        formularioSancion.setBackground(Color.white);
        formularioSancion.setLayout(null);
        return formularioSancion;
    }

    /**
     * Añade las etiquetas de paso al panel de formulario.
     *
     * @param formularioSancion el panel de formulario donde se añadirán las
     *                          etiquetas
     */
    private void addStepLabels(final JPanel formularioSancion) {
        JLabel paso1 = new JLabel("1. Usuario afectado:");
        paso1.setFont(paso1.getFont().deriveFont(16f));
        paso1.setBounds(20, 10, 300, 30);
        paso1.setForeground(Color.decode("#468DAE"));
        formularioSancion.add(paso1);

        resultadoSocio = new JLabel("");
        resultadoSocio.setBounds(20, 50, 500, 40);
        resultadoSocio.setText(
                "<html>Usuario: Estudiante) : <span style='color:#2BC187; font-weight:bold'>Sin sanciones</span></html>");
        resultadoSocio.setBackground(Color.decode("#EDF3F6"));
        resultadoSocio.setOpaque(true);
        resultadoSocio.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        formularioSancion.add(resultadoSocio);

        JLabel paso2 = new JLabel("2. Detalles de la sanción y ejemplar afectado:");
        paso2.setFont(paso2.getFont().deriveFont(16f));
        paso2.setBounds(20, 110, 400, 30);
        paso2.setForeground(Color.decode("#468DAE"));
        formularioSancion.add(paso2);
    }

    /**
     * Crea el botón de selección de usuario y su listener de acción.
     *
     * @param formularioSancion el panel de formulario padre
     */
    private void createUserSelectionButton(final JPanel formularioSancion) {
        usuarioSeleccionado = new int[] { -1 };

        JButton btnCambiarUsuario = new JButton("Cambiar");
        btnCambiarUsuario.setBounds(390, 55, 100, 30);
        btnCambiarUsuario.setBackground(Color.decode("#468DAE"));
        btnCambiarUsuario.setForeground(Color.WHITE);
        btnCambiarUsuario.setFocusPainted(false);
        btnCambiarUsuario.setBorder(null);
        formularioSancion.add(btnCambiarUsuario, 0);

        btnCambiarUsuario.addActionListener(e -> {
            String[][] usuarios = getControlador()
                    .getControladorGestionUsuarios().obtenerUsuariosSancionables();
            if (usuarios == null || usuarios.length == 0) {
                JOptionPane.showMessageDialog(null, "No hay usuarios sancionables", "Información",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            Map<String, Integer> mapa = new LinkedHashMap<>();
            DefaultListModel<String> listModel = new DefaultListModel<>();
            for (String[] u : usuarios) {
                String item = u[2] + " | DNI: " + u[1] + " | Estudiante";
                listModel.addElement(item);
                mapa.put(item, Integer.parseInt(u[0]));
            }

            JList<String> jlist = new JList<>(listModel);
            jlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane scroll = new JScrollPane(jlist);
            scroll.setPreferredSize(new java.awt.Dimension(400, 200));
            int option = JOptionPane.showConfirmDialog(null, scroll, "Seleccione usuario",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (option == JOptionPane.OK_OPTION) {
                String sel = jlist.getSelectedValue();
                if (sel != null) {
                    usuarioSeleccionado[0] = mapa.get(sel);
                    resultadoSocio.setText("<html><b>" + sel + "</b></html>");
                }
            }
        });
    }

    /**
     * Crea los campos de formulario para los detalles de la sanción.
     *
     * @param formularioSancion the parent form panel
     */
    private void createFormFields(final JPanel formularioSancion) {
        JLabel lblMotivo = new JLabel("Motivo de la sanción:");
        lblMotivo.setBounds(20, 150, 200, 25);
        formularioSancion.add(lblMotivo);

        comboMotivo = new JComboBox<>();
        comboMotivo.setBounds(20, 180, 230, 30);
        comboMotivo.addItem("Daño de material");
        comboMotivo.addItem("Pérdida del material");
        comboMotivo.addItem("Comportamiento inapropiado");
        formularioSancion.add(comboMotivo);

        JLabel lblEjemplar = new JLabel("Ejemplar:");
        lblEjemplar.setBounds(270, 150, 200, 25);
        formularioSancion.add(lblEjemplar);

        txtEjemplar = new JTextField();
        txtEjemplar.setBounds(270, 180, 230, 30);
        formularioSancion.add(txtEjemplar);

        JLabel lblDescripcion = new JLabel("Descripción / Observaciones:");
        lblDescripcion.setBounds(20, 220, 200, 25);
        formularioSancion.add(lblDescripcion);

        txtDescripcion = new JTextField();
        txtDescripcion.setBounds(20, 250, 480, 80);
        formularioSancion.add(txtDescripcion);

        JLabel lblFechaInicio = new JLabel("Fecha de inicio de la sanción:");
        lblFechaInicio.setBounds(20, 340, 200, 25);
        formularioSancion.add(lblFechaInicio);

        JTextField txtFechaInicio = new JTextField();
        txtFechaInicio.setBounds(20, 370, 200, 30);
        String hoyStr = LocalDate.now().toString();
        txtFechaInicio.setText(hoyStr);
        txtFechaInicio.setEditable(false);
        formularioSancion.add(txtFechaInicio);

        JLabel lblFechaFin = new JLabel("Fecha de fin de la sanción:");
        lblFechaFin.setBounds(270, 340, 200, 25);
        formularioSancion.add(lblFechaFin);

        txtFechaFin = new JTextField();
        txtFechaFin.setBounds(270, 370, 200, 30);
        formularioSancion.add(txtFechaFin);
    }

    /**
     * Creates and configures the action buttons.
     *
     * @param formularioSancion the parent form panel
     */
    private void createButtons(final JPanel formularioSancion) {
        JButton btnLimpiar = new JButton("Limpiar Formulario");
        btnLimpiar.setBounds(150, 410, 150, 30);
        btnLimpiar.setBackground(Color.white);
        btnLimpiar.setForeground(Color.BLACK);
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.setBorder(null);
        formularioSancion.add(btnLimpiar);

        JButton btnAplicarSancion = new JButton("Aplicar Sanción");
        btnAplicarSancion.setBounds(320, 410, 150, 30);
        btnAplicarSancion.setBackground(Color.decode("#F4791B"));
        btnAplicarSancion.setForeground(Color.WHITE);
        btnAplicarSancion.setFocusPainted(false);
        btnAplicarSancion.setBorder(null);
        formularioSancion.add(btnAplicarSancion);

        btnLimpiar.addActionListener(e -> {
            usuarioSeleccionado[0] = -1;
            resultadoSocio.setText(
                    "<html>Usuario: Estudiante) : <span style='color:#2BC187; font-weight:bold'>Sin sanciones</span></html>");
            txtEjemplar.setText("");
            txtDescripcion.setText("");
            txtFechaFin.setText("");
            comboMotivo.setSelectedIndex(0);
        });

        btnAplicarSancion.addActionListener(e -> {
            String idEjStr = txtEjemplar.getText().trim();
            if (idEjStr.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Introduzca el ID del ejemplar", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            int idEj;
            try {
                idEj = Integer.parseInt(txtEjemplar.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "ID de ejemplar inválido", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            String descripcion = comboMotivo.getSelectedItem() + " - " + txtDescripcion.getText().trim();

            String err = getControlador().getControladorSancionManual()
                    .aplicarSancionManual(usuarioSeleccionado[0], idEj, txtFechaFin.getText().trim(), descripcion);
            if (err != null) {
                JOptionPane.showMessageDialog(null, err, "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Sanción aplicada correctamente.", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            if (err == null) {
                btnLimpiar.doClick();
            }
        });
    }

}
