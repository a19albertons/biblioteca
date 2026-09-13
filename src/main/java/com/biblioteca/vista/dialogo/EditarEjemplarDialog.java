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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.biblioteca.controlador.Controlador;
import com.biblioteca.dto.EstadoEjemplarDTO;

/**
 * Diálogo para editar un ejemplar existente.
 * Muestra número de ejemplar (readonly) y fecha de adquisición.
 */
public class EditarEjemplarDialog extends OverlayDialog {
    /**
     * Controlador de la aplicación
     */
    private Controlador controlador;
    /**
     * ID del ejemplar a editar
     */
    private int idEjemplar;

    /**
     * Constructor del diálogo
     * 
     * @param parent
     * @param controlador
     * @param idEjemplar
     */
    public EditarEjemplarDialog(final JFrame parent, final Controlador controlador, final int idEjemplar) {
        super(parent, "Editar Ejemplar");
        this.controlador = controlador;
        this.idEjemplar = idEjemplar;
        initUI();
        setSize(new Dimension(480, 200));
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
        JLabel title = new JLabel("Editar Ejemplar");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        content.add(title, BorderLayout.NORTH);

        // Centro con campos
        JPanel center = new JPanel(new FlowLayout(FlowLayout.LEFT));
        center.setBackground(Color.white);
        center.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // Obtener datos actuales
        EstadoEjemplarDTO datos = controlador.getControladorEditarEjemplarDialog().obtenerDetallesEjemplar(idEjemplar);
        String num = "";
        String fecha = LocalDate.now().toString();
        if (datos != null) {
            num = String.valueOf(datos.getNumEjemplar());
            fecha = datos.getFechaAdquisicion() != null ? datos.getFechaAdquisicion().toString() : fecha;
        }

        // Campos
        center.add(new JLabel("Número de ejemplar:"));
        JLabel numLabel = new JLabel(num);
        center.add(numLabel);

        // Fecha adquisición
        center.add(new JLabel("Fecha adquisición (YYYY-MM-DD):"));
        JTextField fechaField = new JTextField(12);
        fechaField.setText(fecha);
        center.add(fechaField);

        // El estado no puede cambiarse desde aquí; queda para otro diálogo específico.
        // Mantener el campo de fecha para edición solamente.

        content.add(center, BorderLayout.CENTER);

        // Footer con botones
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(Color.white);

        // Botones Guardar y Cancelar
        JButton save = new JButton("Guardar");
        save.setBackground(Color.decode("#F4791B"));
        save.setForeground(Color.white);
        save.setBorder(null);
        save.setFocusPainted(false);
        save.addActionListener(e -> {
            // Validar y guardar
            String fechaStr = fechaField.getText();
            LocalDate fechaParsed;
            // Validar fecha
            try {
                fechaParsed = (fechaStr == null || fechaStr.trim().isEmpty()) ? LocalDate.now()
                        : LocalDate.parse(fechaStr.trim());
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use YYYY-MM-DD", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Llamar al controlador para actualizar (el controlador conservará el estado actual)
            boolean ok = controlador.getControladorEditarEjemplarDialog().editarEjemplar(idEjemplar, fechaParsed);
            // Mostrar resultado
            if (ok) {
                JOptionPane.showMessageDialog(this, "Ejemplar actualizado correctamente", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                controlador.getControladorNavegacion().mostrarEjemplaresParaPublicacion(datos.getIdPublicacion());
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar el ejemplar (error en BD)", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Cancelar edición
        JButton cancel = new JButton("Cancelar");
        cancel.setBackground(Color.white);
        cancel.setBorder(null);
        cancel.setFocusPainted(false);
        cancel.addActionListener(e -> dispose());

        footer.add(cancel);
        footer.add(save);

        getContentPane().add(content, BorderLayout.CENTER);
        getContentPane().add(footer, BorderLayout.SOUTH);
    }
}
