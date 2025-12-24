package com.example.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.example.controlador.Controlador;

/**
 * Diálogo para confirmar la eliminación (desactivación) de un usuario
 */
public class EliminarUsuarioDialog extends JDialog {
    /**
     * Controlador principal de la aplicación
     */
    private Controlador controlador;
    /**
     * Frame padre para overlays
     */
    private JFrame parentFrame;
    /**
     * ID del usuario a eliminar
     */
    private int idUsuario;
    /**
     * Glass pane previo del frame padre
     */
    private java.awt.Component previousGlassPane;

    /**
     * Constructor
     * 
     * @param parent
     * @param controlador
     * @param idUsuario
     */
    public EliminarUsuarioDialog(JFrame parent, Controlador controlador, int idUsuario) {
        // Diálogo modal
        super(parent, "Eliminar Usuario", true);
        this.controlador = controlador;
        this.parentFrame = parent;
        this.idUsuario = idUsuario;

        // Inicializar interfaz de usuario
        initUI();
        setSize(new Dimension(360, 140));
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
     * Inicializa la interfaz de usuario
     */
    private void initUI() {
        // Layout principal
        getContentPane().setLayout(new BorderLayout());
        JPanel contenido = new JPanel(new BorderLayout());
        contenido.setBackground(Color.white);
        contenido.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Título y texto
        JLabel titulo = new JLabel("Eliminar Usuario");
        titulo.setFont(titulo.getFont().deriveFont(java.awt.Font.BOLD, 16f));
        contenido.add(titulo, BorderLayout.NORTH);

        // Texto de confirmación
        JLabel texto = new JLabel("Seguro que quieres eliminar el usuario?");
        texto.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 6, 12, 6));
        contenido.add(texto, BorderLayout.CENTER);

        // Botones
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.setBackground(Color.white);

        // Botón Cancelar
        JButton cancelar = new JButton("Cancelar");
        cancelar.setBackground(Color.white);
        cancelar.setForeground(Color.decode("#000000"));
        cancelar.setBorder(null);
        cancelar.addActionListener(e -> dispose());

        // Botón Eliminar
        JButton eliminar = new JButton("Eliminar");
        eliminar.setBackground(Color.decode("#E53935"));
        eliminar.setForeground(Color.white);
        eliminar.setBorder(null);
        eliminar.addActionListener(e -> {
            // Intentar eliminar usuario
            boolean ok = controlador.getControladorEliminarUsuario().eliminarUsuario(idUsuario);
            // Mostrar resultado
            if (ok) {
                JOptionPane.showMessageDialog(this, "Usuario desactivado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                controlador.getControladorNavegacion().refrescarUsuarios();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No es posible eliminar el usuario: existen préstamos activos o ocurrió un error", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        botones.add(cancelar);
        botones.add(eliminar);

        getContentPane().add(contenido, BorderLayout.CENTER);
        getContentPane().add(botones, BorderLayout.SOUTH);
    }

    /**
     * Muestra u oculta el diálogo, instalando o quitando el overlay en el frame padre
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
        // Añadir listener para quitar overlay al cerrar
        if (parentFrame == null)
            return;
        try {
            // Recordar el glass pane previo
            javax.swing.RootPaneContainer rpc = (javax.swing.RootPaneContainer) parentFrame;
            java.awt.Component current = rpc.getRootPane().getGlassPane();
            previousGlassPane = current;
            javax.swing.JPanel overlay = new javax.swing.JPanel();

            // Panel translúcido gris
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
        // Quitar overlay
        if (parentFrame == null)
            return;
        try {
            // Restaurar el glass pane previo
            javax.swing.RootPaneContainer rpc = (javax.swing.RootPaneContainer) parentFrame;
            rpc.getRootPane().getGlassPane().setVisible(false);
        } catch (Exception e) {
            System.out.println("No se pudo quitar overlay: " + e.getMessage());
        }
    }
}
