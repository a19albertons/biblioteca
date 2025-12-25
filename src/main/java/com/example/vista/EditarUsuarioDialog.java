package com.example.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.RootPaneContainer;

import com.example.controlador.Controlador;
import com.example.modelo.TipoUsuario;

/**
 * Diálogo para editar un usuario existente
 */
public class EditarUsuarioDialog extends JDialog {
    /**
     * Controlador principal de la aplicación
     */
    private Controlador controlador;
    /**
     * Frame padre (para el overlay)
     */
    private JFrame parentFrame;
    /**
     * Id del usuario a editar
     */
    private int idUsuario;
    /**
     * Componente glass pane previo (para restaurar al cerrar el diálogo)
     */
    private java.awt.Component previousGlassPane;

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
    public EditarUsuarioDialog(JFrame parent, Controlador controlador, int idUsuario) {
        // Inicializar diálogo
        super(parent, "Editar Usuario", true);
        this.controlador = controlador;
        this.parentFrame = parent;
        this.idUsuario = idUsuario;

        // Inicializar UI
        initUI();
        setSize(new Dimension(380, 320));
        setLocationRelativeTo(parent);

        // Añadir listener para manejar el overlay al cerrar
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                removeOverlay();
            }

            @Override
            public void windowClosing(WindowEvent e) {
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
     * @param f
     */
    private void configureField(JTextField f) {
        f.setPreferredSize(new Dimension(220, 28));
    }

    /**
     * Carga los datos del usuario a editar en el formulario
     */
    private void cargarDatos() {
        try {
            // Obtener datos del usuario
            String[] datos = controlador.getControladorEditarUsuarioDialog().obtenerDetallesUsuario(idUsuario);
            // No hay datos acaba el metodo
            if (datos == null)
                return;
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
        } catch (Exception e) {
            System.out.println("Error cargando datos usuario: " + e.getMessage());
        }
    }

    /**
     * Muestra u oculta el diálogo, gestionando el overlay
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
     * Instala un overlay translúcido en el frame padre
     */
    private void installOverlay() {
        // Instala un panel translúcido sobre el frame padre para deshabilitar la interacción
        if (parentFrame == null)
            return;
        try {
            // Recordar el glass pane previo
            RootPaneContainer rpc = (RootPaneContainer) parentFrame;
            Component current = rpc.getRootPane().getGlassPane();
            previousGlassPane = current;
            JPanel overlay = new JPanel();

            // Hacerlo translúcido
            overlay.setOpaque(true);
            overlay.setBackground(new java.awt.Color(217, 217, 217, 153));
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
        // Restaura el glass pane previo
        if (parentFrame == null)
            return;
        try {
            // Restaurar el glass pane previo (si lo teníamos)
            RootPaneContainer rpc = (RootPaneContainer) parentFrame;
            if (previousGlassPane != null) {
                rpc.getRootPane().setGlassPane(previousGlassPane);
                previousGlassPane.setVisible(false);
                previousGlassPane = null;
            } else {
                // Si no tenemos referencia previa, simplemente ocultar el glass pane actual
                rpc.getRootPane().getGlassPane().setVisible(false);
            }
        } catch (Exception e) {
            System.out.println("No se pudo quitar overlay: " + e.getMessage());
        }
    }
}
