package com.example.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.example.controlador.Controlador;
import com.example.modelo.TipoUsuario;

/**
 * Diálogo para crear un nuevo usuario/socio.
 * La contraseña inicial se fijará al valor del DNI suministrado.
 */
public class NuevoUsuarioDialog extends JDialog {
    /**
     * Controlador principal de la aplicación
     */
    private Controlador controlador;
    /**
     * Ventana padre
     */
    private JFrame parentFrame;

    /**
     * Constructor
     * 
     * @param parent
     * @param controlador
     */
    public NuevoUsuarioDialog(JFrame parent, Controlador controlador) {
        // Llamar al constructor de JDialog con el padre, título y modalidad
        super(parent, "Nuevo Usuario", true);
        this.controlador = controlador;
        this.parentFrame = parent;
        initUI();
        setSize(new Dimension(420, 360));
        setLocationRelativeTo(parent);

        // Añadir listener para quitar overlay al cerrar
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                removeOverlay();
            }

            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                removeOverlay();
            }
        });
    }

    /**
     * Inicializa la interfaz del diálogo
     */
    private void initUI() {
        // Configurar layout principal
        getContentPane().setLayout(new BorderLayout());
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.white);
        content.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Título
        JLabel title = new JLabel("Nuevo Usuario");
        title.setFont(title.getFont().deriveFont(java.awt.Font.BOLD, 16f));
        content.add(title, BorderLayout.NORTH);

        // Centro con formulario
        JPanel center = new JPanel(new java.awt.GridBagLayout());
        center.setBackground(Color.white);
        center.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // GridBagConstraints para el formulario
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new java.awt.Insets(6, 6, 6, 6);
        gbc.anchor = java.awt.GridBagConstraints.WEST;

        // DNI
        gbc.gridx = 0;
        gbc.weightx = 0;
        center.add(new JLabel("DNI / ID"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        JTextField dniField = new JTextField(20);
        center.add(dniField, gbc);

        // Nombre
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.fill = java.awt.GridBagConstraints.NONE;
        center.add(new JLabel("Nombre"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        JTextField nombreField = new JTextField(20);
        center.add(nombreField, gbc);

        // Apellidos
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.fill = java.awt.GridBagConstraints.NONE;
        center.add(new JLabel("Apellidos"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        JTextField apellidosField = new JTextField(20);
        center.add(apellidosField, gbc);

        // Email
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.fill = java.awt.GridBagConstraints.NONE;
        center.add(new JLabel("Email"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        JTextField emailField = new JTextField(20);
        center.add(emailField, gbc);

        // Tipo
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.fill = java.awt.GridBagConstraints.NONE;
        center.add(new JLabel("Tipo"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
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

    /**
     * Muestra u oculta el diálogo, instalando o quitando el overlay en el frame
     * padre
     */
    @Override
    public void setVisible(boolean b) {
        if (b)
            installOverlay();
        super.setVisible(b);
        if (!b)
            removeOverlay();
    }

    /**
     * Instala un overlay translúcido en el frame padre para deshabilitar
     * interacciones
     */
    private void installOverlay() {
        // Comprobar que el parentFrame existe
        if (parentFrame == null)
            return;
        try {
            // Obtener el RootPaneContainer del frame padre
            javax.swing.RootPaneContainer rpc = (javax.swing.RootPaneContainer) parentFrame;
            java.awt.Component current = rpc.getRootPane().getGlassPane();
            java.awt.Component previousGlassPane = current;
            javax.swing.JPanel overlay = new javax.swing.JPanel();
            overlay.setOpaque(true);
            overlay.setBackground(new java.awt.Color(217, 217, 217, 153));
            overlay.addMouseListener(new java.awt.event.MouseAdapter() {
            });
            // Asignar el overlay como glass pane
            rpc.getRootPane().setGlassPane(overlay);
            overlay.setVisible(true);
        } catch (Exception e) {
            System.out.println("No se pudo instalar overlay: " + e.getMessage());
        }
    }

    /**
     * Quita el overlay del frame padre
     */
    private void removeOverlay() {
        // Comprobar que el parentFrame existe
        if (parentFrame == null)
            return;
        try {
            // Obtener el RootPaneContainer del frame padre
            javax.swing.RootPaneContainer rpc = (javax.swing.RootPaneContainer) parentFrame;
            rpc.getRootPane().getGlassPane().setVisible(false);
        } catch (Exception e) {
            System.out.println("No se pudo quitar overlay: " + e.getMessage());
        }
    }
}
