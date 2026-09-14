package com.biblioteca.vista.dialogo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.biblioteca.controlador.Controlador;

import javax.swing.JOptionPane;

/**
 * Diálogo para crear un nuevo ejemplar asociado a una publicación.
 * Muestra un campo de fecha de adquisición (YYYY-MM-DD) y un botón Añadir.
 */
public final class NuevoEjemplarDialog extends OverlayDialog {
    /**
     * Controlador de la aplicación
     */
    private final Controlador controlador;
    /**
     * ID de la publicación a la que se añade el ejemplar
     */
    private final int idPublicacion;

    /**
     * Constructor del diálogo
     * 
     * @param parent
     * @param controlador
     * @param idPublicacion
     */
    public NuevoEjemplarDialog(final JFrame parent, final Controlador controlador, final int idPublicacion) {
        super(parent, "Nuevo Ejemplar");
        this.controlador = controlador;
        this.idPublicacion = idPublicacion;
        initUI();
        setSize(new Dimension(420, 160));
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
}
