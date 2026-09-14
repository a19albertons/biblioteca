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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.biblioteca.controlador.Controlador;
import com.biblioteca.modelo.TipoUsuario;

/**
 * Diálogo para crear un nuevo usuario/socio.
 * La contraseña inicial se fijará al valor del DNI suministrado.
 */
public class NuevoUsuarioDialog extends BaseUsuarioDialog {
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
        center.add(getDniField(), gbc);

        // Nombre
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        center.add(new JLabel("Nombre"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        center.add(getNombreField(), gbc);

        // Apellidos
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        center.add(new JLabel("Apellidos"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        center.add(getApellidosField(), gbc);

        // Email
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        center.add(new JLabel("Email"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        center.add(getEmailField(), gbc);

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
        for (int i = 0; i < TipoUsuario.values().length; i++) {
            getTipoBox().addItem(TipoUsuario.values()[i].getDescripcion());
        }
        center.add(getTipoBox(), gbc);

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


            // Validaciones básicas
            if (getDni() == null || getDni().isEmpty() || getNombre() == null || getNombre().isEmpty()) {
                JOptionPane.showMessageDialog(this, "DNI y Nombre son obligatorios", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Intentar crear usuario
            boolean ok = controlador.getControladorNuevoUsuarioDialog().crearUsuario(getDni().trim(), getNombre(),
                    getApellidos(), getEmail(), getCode());
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
