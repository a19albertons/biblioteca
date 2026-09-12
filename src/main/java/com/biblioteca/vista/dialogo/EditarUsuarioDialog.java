package com.biblioteca.vista.dialogo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.biblioteca.controlador.Controlador;
import com.biblioteca.modelo.TipoUsuario;

/**
 * Diálogo para editar un usuario existente
 */
public class EditarUsuarioDialog extends OverlayDialog {
    /**
     * Controlador principal de la aplicación
     */
    private Controlador controlador;
    /**
     * Id del usuario a editar
     */
    private int idUsuario;

    /**
     * Campos de texto del formulario
     */
    private JTextField dniField;
    /**
     * Otros campos del formulario
     */
    private JTextField nombreField;
    /**
     * Otros campos del formulario
     */
    private JTextField apellidosField;
    /**
     * Otros campos del formulario
     */
    private JTextField emailField;
    /**
     * Otros campos del formulario
     */
    private JComboBox<String> tipoBox;

    /**
     * Constructor
     * 
     * @param parent
     * @param controlador
     * @param idUsuario
     */
    public EditarUsuarioDialog(final JFrame parent, final Controlador controlador, final int idUsuario) {
        // Inicializar diálogo
        super(parent, "Editar Usuario");
        this.controlador = controlador;
        this.idUsuario = idUsuario;

        // Inicializar UI
        initUI();
        setSize(new Dimension(380, 320));
        setLocationRelativeTo(parent);

        // Añadir listener para manejar el overlay al cerrar
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(final WindowEvent e) {
                removeOverlay();
            }

            @Override
            public void windowClosing(final WindowEvent e) {
                removeOverlay();
            }
        });
        // Cargar datos del usuario a editar
        cargarDatos();
    }

    // Inicializar la interfaz de usuario
    private void initUI() {
        // Configurar layout principal
        getContentPane().setLayout(new BorderLayout());
        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(Color.white);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 12, 8, 12);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;

        // Título
        JLabel title = new JLabel("Editar Usuario");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        content.add(title, c);

        // Campos del formulario
        c.gridwidth = 1;
        c.gridy++;
        content.add(new JLabel("DNI / ID"), c);
        c.gridx = 1;
        dniField = new JTextField();
        configureField(dniField);
        content.add(dniField, c);

        // Nombre
        c.gridy++;
        c.gridx = 0;
        content.add(new JLabel("Nombre"), c);
        c.gridx = 1;
        nombreField = new JTextField();
        configureField(nombreField);
        content.add(nombreField, c);

        // Apellidos
        c.gridy++;
        c.gridx = 0;
        content.add(new JLabel("Apellidos"), c);
        c.gridx = 1;
        apellidosField = new JTextField();
        configureField(apellidosField);
        content.add(apellidosField, c);

        // Email
        c.gridy++;
        c.gridx = 0;
        content.add(new JLabel("Email"), c);
        c.gridx = 1;
        emailField = new JTextField();
        configureField(emailField);
        content.add(emailField, c);

        // Tipo
        c.gridy++;
        c.gridx = 0;
        content.add(new JLabel("Tipo"), c);
        c.gridx = 1;
        String[] tiposDesc = new String[TipoUsuario.values().length];
        for (int i = 0; i < TipoUsuario.values().length; i++) {
            tiposDesc[i] = TipoUsuario.values()[i].getDescripcion();
        }
        tipoBox = new JComboBox<>(tiposDesc);
        content.add(tipoBox, c);

        // Botón editar
        JButton editar = new JButton("Editar");
        editar.setBackground(Color.decode("#F4791B"));
        editar.setForeground(Color.white);
        editar.setBorder(null);
        c.gridy++;
        c.gridx = 1;
        c.anchor = GridBagConstraints.EAST;
        content.add(editar, c);

        // Acción botón editar
        editar.addActionListener(e -> {
            // Obtener datos del formulario
            String dni = dniField.getText();
            String nombre = nombreField.getText();
            String apellidos = apellidosField.getText();
            String email = emailField.getText();
            int tipoIdx = tipoBox.getSelectedIndex();
            String tipoCode = TipoUsuario.values()[tipoIdx].name();

            // Validaciones básicas
            if (dni == null || dni.trim().isEmpty() || nombre == null || nombre.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "DNI y Nombre son obligatorios", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Dividir apellidos en apellido1 y apellido2 (si existe)
            String apellido1 = "";
            String apellido2 = "";
            if (apellidos != null && !apellidos.trim().isEmpty()) {
                String[] parts = apellidos.trim().split("\\s+", 2);
                apellido1 = parts[0];
                if (parts.length > 1) {
                    apellido2 = parts[1];
                }
            }

            // Llamar al controlador para actualizar el usuario
            boolean ok = controlador.getControladorEditarUsuarioDialog().editarUsuario(idUsuario, dni.trim(),
                    nombre.trim(), apellido1.trim(), apellido2.trim(), email.trim(), tipoCode);
            // Mostrar mensaje según resultado
            if (ok) {
                JOptionPane.showMessageDialog(this, "Usuario actualizado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                controlador.getControladorNavegacion().refrescarUsuarios();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar el usuario", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Botón cancelar
        JButton cancelar = new JButton("Cancelar");
        cancelar.setBackground(Color.white);
        cancelar.setBorder(null);
        cancelar.addActionListener(e -> dispose());
        c.gridx = 0;
        c.anchor = GridBagConstraints.WEST;
        content.add(cancelar, c);

        getContentPane().add(content, BorderLayout.CENTER);
    }

    /**
     * Configura un campo de texto estándar
     * 
     * @param f
     */
    private void configureField(final JTextField f) {
        f.setPreferredSize(new Dimension(220, 28));
    }

    /**
     * Carga los datos del usuario a editar en el formulario
     */
    private void cargarDatos() {
        // Obtener datos del usuario
        String[] datos = controlador.getControladorEditarUsuarioDialog().obtenerDetallesUsuario(idUsuario);
        // No hay datos acaba el metodo
        if (datos == null) {
            return;
        }
        // Rellenar campos
        dniField.setText(datos.length > 0 ? datos[0] : "");
        nombreField.setText(datos.length > 1 ? datos[1] : "");
        // Rellenar apellidos (apellido1 [apellido2])
        if (datos.length > 2) {
            apellidosField.setText(datos[2] + (datos.length > 3 && datos[3] != null ? " " + datos[3] : ""));
        } else {
            apellidosField.setText("");
        }
        emailField.setText(datos.length > 4 ? datos[4] : "");
        String tipoCode = datos.length > 5 ? datos[5] : "";
        // Select by matching TipoUsuario name
        for (int i = 0; i < TipoUsuario.values().length; i++) {
            if (TipoUsuario.values()[i].name().equals(tipoCode)) {
                tipoBox.setSelectedIndex(i);
                break;
            }
        }
    }

}
