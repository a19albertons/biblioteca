package com.biblioteca.vista.dialogo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
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
 * Diálogo para crear un nuevo usuario/socio.
 * La contraseña inicial se fijará al valor del DNI suministrado.
 */
public class NuevoUsuarioDialog extends OverlayDialog {
    /**
     * Controlador principal de la aplicación
     */
    private Controlador controlador;

    /**
     * Constructor
     * 
     * @param parent
     * @param controlador
     */
    public NuevoUsuarioDialog(final JFrame parent, final Controlador controlador) {
        super(parent, "Nuevo Usuario");
        this.controlador = controlador;
        initUI();
        setSize(new Dimension(420, 360));
        setLocationRelativeTo(parent);
    }

    /**
     * Inicializa la interfaz del diálogo
     */
    private void initUI() {
        // Configurar layout principal
        getContentPane().setLayout(new BorderLayout());
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.white);
        content.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Título
        JLabel title = new JLabel("Nuevo Usuario");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        content.add(title, BorderLayout.NORTH);

        // Centro con formulario
        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(Color.white);
        center.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // GridBagConstraints para el formulario
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        // DNI
        gbc.gridx = 0;
        gbc.weightx = 0;
        center.add(new JLabel("DNI / ID"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JTextField dniField = new JTextField(20);
        center.add(dniField, gbc);

        // Nombre
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        center.add(new JLabel("Nombre"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JTextField nombreField = new JTextField(20);
        center.add(nombreField, gbc);

        // Apellidos
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        center.add(new JLabel("Apellidos"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JTextField apellidosField = new JTextField(20);
        center.add(apellidosField, gbc);

        // Email
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        center.add(new JLabel("Email"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JTextField emailField = new JTextField(20);
        center.add(emailField, gbc);

        // Tipo
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        center.add(new JLabel("Tipo"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        // Rellenar JComboBox con descripciones de TipoUsuario
        String[] tiposDesc = new String[TipoUsuario.values().length];
        for (int i = 0; i < TipoUsuario.values().length; i++) {
            tiposDesc[i] = TipoUsuario.values()[i].getDescripcion();
        }
        JComboBox<String> tipoBox = new JComboBox<>(tiposDesc);
        center.add(tipoBox, gbc);

        content.add(center, BorderLayout.CENTER);

        // Footer con botones
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(Color.white);

        // Botón añadir
        JButton add = new JButton("Añadir");
        add.setBackground(Color.decode("#F4791B"));
        add.setForeground(Color.white);
        add.setBorder(null);
        add.setFocusPainted(false);
        add.addActionListener(e -> {
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

            // Intentar crear usuario
            boolean ok = controlador.getControladorNuevoUsuarioDialog().crearUsuario(dni.trim(), nombre.trim(),
                    apellidos.trim(), email.trim(), tipoCode);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Usuario creado y contraseña inicial igual al DNI", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                // refrescar listado usuarios
                controlador.getControladorNavegacion().refrescarUsuarios();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo crear el usuario (posible duplicado o error en BD)",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Botón cancelar
        JButton cancel = new JButton("Cancelar");
        cancel.setBackground(Color.white);
        cancel.setBorder(null);
        cancel.setFocusPainted(false);
        cancel.addActionListener(e -> dispose());

        footer.add(cancel);
        footer.add(add);

        getContentPane().add(content, BorderLayout.CENTER);
        getContentPane().add(footer, BorderLayout.SOUTH);
    }
}
