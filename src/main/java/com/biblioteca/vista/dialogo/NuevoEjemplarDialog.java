package com.biblioteca.vista.dialogo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.RootPaneContainer;

import com.biblioteca.controlador.Controlador;

import javax.swing.JOptionPane;

/**
 * Diálogo para crear un nuevo ejemplar asociado a una publicación.
 * Muestra un campo de fecha de adquisición (YYYY-MM-DD) y un botón Añadir.
 */
public class NuevoEjemplarDialog extends JDialog {
    /**
     * Controlador de la aplicación
     */
    private Controlador controlador;
    /**
     * Frame padre (para overlay)
     */
    private JFrame parentFrame;
    /**
     * ID de la publicación a la que se añade el ejemplar
     */
    private int idPublicacion;
    /**
     * Componente previo del glass pane (para restaurar al cerrar el diálogo)
     */
    private java.awt.Component previousGlassPane;

    /**
     * Constructor del diálogo
     * 
     * @param parent
     * @param controlador
     * @param idPublicacion
     */
    public NuevoEjemplarDialog(JFrame parent, Controlador controlador, int idPublicacion) {
        super(parent, "Nuevo Ejemplar", true);
        this.controlador = controlador;
        this.parentFrame = parent;
        this.idPublicacion = idPublicacion;
        initUI();
        setSize(new Dimension(420, 160));
        setLocationRelativeTo(parent);

        // Cierra el overlay al cerrar el diálogo
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
        JLabel title = new JLabel("Nuevo Ejemplar");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        content.add(title, BorderLayout.NORTH);

        // Centro con campo de fecha
        JPanel center = new JPanel(new FlowLayout(FlowLayout.LEFT));
        center.setBackground(Color.white);
        center.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // Campo fecha de adquisición
        JLabel fechaLabel = new JLabel("Fecha adquisición (YYYY-MM-DD)");
        center.add(fechaLabel);
        JTextField fechaField = new JTextField(16);
        fechaField.setText(LocalDate.now().toString());
        center.add(fechaField);

        content.add(center, BorderLayout.CENTER);

        // Footer con botones
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(Color.white);

        // Botones Añadir y Cancelar
        JButton add = new JButton("Añadir");
        add.setBackground(Color.decode("#F4791B"));
        add.setForeground(Color.white);
        add.setBorder(null);
        add.setFocusPainted(false);
        add.addActionListener(e -> {
            // Validar fecha
            String fechaStr = fechaField.getText();
            LocalDate fecha;
            try {
                fecha = (fechaStr == null || fechaStr.trim().isEmpty()) ? LocalDate.now()
                        : LocalDate.parse(fechaStr.trim());
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use YYYY-MM-DD", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            boolean ok = controlador.getControladorNuevoEjemplarDialog().crearEjemplar(idPublicacion, fecha);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Ejemplar añadido correctamente", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                // refrescar vista de ejemplares
                controlador.getControladorNavegacion().mostrarEjemplaresParaPublicacion(idPublicacion);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo crear el ejemplar (error en BD)", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Boton Cancelar
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
     * Controla la visibilidad del diálogo y el overlay
     */
    @Override
    public void setVisible(boolean b) {
        if (b) {
            installOverlay();
        }
        super.setVisible(b);
        if (!b) {
            removeOverlay();
        }
    }

    /**
     * Instala un overlay semitransparente en el frame padre
     */
    private void installOverlay() {
        // Controla si el frame padre es nulo
        if (parentFrame == null)
            return;
        try {
            // Guardar el componente previo del glass pane para restaurarlo después
            RootPaneContainer rpc = (RootPaneContainer) parentFrame;
            Component current = rpc.getRootPane().getGlassPane();
            previousGlassPane = current;

            // Crear panel semitransparente
            JPanel overlay = new JPanel();
            overlay.setOpaque(true);
            overlay.setBackground(new Color(217, 217, 217, 153));
            overlay.addMouseListener(new MouseAdapter() {
            });

            // Asignar overlay como glass pane
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
        // Controla si el frame padre es nulo
        if (parentFrame == null)
            return;
        try {
            // Restaurar el componente previo del glass pane
            RootPaneContainer rpc = (RootPaneContainer) parentFrame;
            if (previousGlassPane != null) {
                rpc.getRootPane().setGlassPane(previousGlassPane);
                previousGlassPane.setVisible(false);
                previousGlassPane = null;
            } else {
                rpc.getRootPane().getGlassPane().setVisible(false);
            }
        } catch (Exception e) {
            System.out.println("No se pudo quitar overlay: " + e.getMessage());
        }
    }
}
