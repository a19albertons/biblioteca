package com.biblioteca.vista.dialogo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.RootPaneContainer;

import com.biblioteca.controlador.Controlador;
import com.biblioteca.dto.EstadoEjemplarDTO;

/**
 * Diálogo de confirmación para eliminar (marcar como baja) un ejemplar.
 * Comprueba que no haya préstamos activos antes de marcar la baja.
 */
public class EliminarEjemplarDialog extends JDialog {
    /**
     * Controlador de la aplicación
     */
    private Controlador controlador;
    /**
     * Frame padre (para overlay)
     */
    private JFrame parentFrame;
    /**
     * ID del ejemplar a eliminar
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
    public EliminarEjemplarDialog(JFrame parent, Controlador controlador, int idEjemplar) {
        super(parent, "Eliminar Ejemplar", true);
        this.controlador = controlador;
        this.parentFrame = parent;
        this.idEjemplar = idEjemplar;
        initUI();
        setSize(new Dimension(420, 160));
        setLocationRelativeTo(parent);

        // Cierra el overlay al cerrar el diálogo
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
        // Layout y contenido
        getContentPane().setLayout(new BorderLayout());
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.white);
        content.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Título y mensaje
        JLabel title = new JLabel("Eliminar Ejemplar");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        content.add(title, BorderLayout.NORTH);

        // Mensaje de confirmación
        JLabel texto = new JLabel("Seguro que quieres eliminar el ejemplar?");
        texto.setBorder(BorderFactory.createEmptyBorder(12, 6, 12, 6));
        content.add(texto, BorderLayout.CENTER);

        // Botones
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.setBackground(Color.white);

        // Botones Cancelar y Eliminar
        JButton cancelar = new JButton("Cancelar");
        cancelar.setBackground(Color.white);
        cancelar.setForeground(Color.decode("#000000"));
        cancelar.setBorder(null);
        cancelar.addActionListener(e -> dispose());

        JButton eliminar = new JButton("Eliminar");
        eliminar.setBackground(Color.decode("#E53935"));
        eliminar.setForeground(Color.white);
        eliminar.setBorder(null);
        eliminar.addActionListener(e -> {
            // Antes de eliminar, obtener id_publicacion para refrescar la vista tras la
            // operación
            EstadoEjemplarDTO detalles = controlador.getControladorEjemplares().obtenerDetallesEjemplar(idEjemplar);
            int idPublicacion = -1;
            // extraer id_publicacion de los detalles obtenidos
            if (detalles != null) {
                idPublicacion = detalles.getIdPublicacion();
            }

            // Intentar eliminar el ejemplar
            boolean ok = controlador.getControladorEliminarEjemplarDialog().eliminarEjemplar(idEjemplar);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Ejemplar marcado como baja", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                // refrescar lista de ejemplares para la publicación asociada (si se pudo
                // obtener)
                if (idPublicacion > 0) {
                    controlador.getControladorNavegacion().mostrarEjemplaresParaPublicacion(idPublicacion);
                } else {
                    controlador.getControladorNavegacion().refrescarPanelControl();
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No es posible eliminar el ejemplar: existen préstamos activos o error",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        botones.add(cancelar);
        botones.add(eliminar);

        getContentPane().add(content, BorderLayout.CENTER);
        getContentPane().add(botones, BorderLayout.SOUTH);
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
        // Controla si el frame padre es nulo
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
