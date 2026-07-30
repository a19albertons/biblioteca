package com.example.vista.dialogo;

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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.RootPaneContainer;

import com.example.controlador.Controlador;

/**
 * Diálogo para editar un ejemplar existente.
 * Muestra número de ejemplar (readonly) y fecha de adquisición.
 */
public class EditarEjemplarDialog extends JDialog {
    /**
     * Controlador de la aplicación
     */
    private Controlador controlador;
    /**
     * Frame padre (para overlay)
     */
    private JFrame parentFrame;
    /**
     * ID del ejemplar a editar
     */
    private int idEjemplar;
    /**
     * Componente previo del glass pane (para restaurar al cerrar el diálogo)
     */
    private Component previousGlassPane;

    /**
     * Constructor del diálogo
     * 
     * @param parent
     * @param controlador
     * @param idEjemplar
     */
    public EditarEjemplarDialog(JFrame parent, Controlador controlador, int idEjemplar) {
        // Mostrar overlay en el frame padre
        super(parent, "Editar Ejemplar", true);
        this.controlador = controlador;
        this.parentFrame = parent;
        this.idEjemplar = idEjemplar;
        initUI();
        setSize(new Dimension(480, 200));
        setLocationRelativeTo(parent);

        // Guardar componente previo del glass pane y mostrar overlay
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
        JLabel title = new JLabel("Editar Ejemplar");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        content.add(title, BorderLayout.NORTH);

        // Centro con campos
        JPanel center = new JPanel(new FlowLayout(FlowLayout.LEFT));
        center.setBackground(Color.white);
        center.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // Obtener datos actuales
        String[] datos = controlador.getControladorEditarEjemplarDialog().obtenerDetallesEjemplar(idEjemplar);
        String num = "";
        String fecha = LocalDate.now().toString();
        if (datos != null) {
            num = datos.length > 2 ? datos[2] : "";
            fecha = datos.length > 3 ? datos[3] : fecha;
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
                controlador.getControladorNavegacion().mostrarEjemplaresParaPublicacion(Integer.parseInt(datos[1]));
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

    /**
     * Muestra u oculta el diálogo, instalando o quitando el overlay en el frame
     * padre.
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
        // Comprobar existencia de frame padre
        if (parentFrame == null)
            return;
        try {
            // Guardar el componente previo del glass pane para restaurarlo después
            RootPaneContainer rpc = (RootPaneContainer) parentFrame;
            Component current = rpc.getRootPane().getGlassPane();
            previousGlassPane = current;

            // Crear un panel semitransparente para el overlay
            JPanel overlay = new JPanel();
            overlay.setOpaque(true);
            overlay.setBackground(new Color(217, 217, 217, 153));
            overlay.addMouseListener(new MouseAdapter() {
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
        // Comprobar existencia de frame padre
        if (parentFrame == null)
            return;
        try {
            // Restaurar el componente previo del glass pane
            RootPaneContainer rpc = (RootPaneContainer) parentFrame;
            // Restaura el componente previo del glass pane
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
