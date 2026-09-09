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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.RootPaneContainer;

import com.biblioteca.controlador.Controlador;

/**
 * Diálogo de confirmación para eliminar (dar de baja) una publicación.
 * Comprueba que no haya préstamos activos antes de borrar y muestra mensajes
 * al usuario.
 */
public class EliminarPublicacionDialog extends JDialog {
    /**
     * Controlador de la aplicación
     */
    private Controlador controlador;
    /**
     * Frame padre (para overlay)
     */
    private JFrame parentFrame;
    /**
     * ID de la publicación a eliminar
     */
    private int idPublicacion;
    /**
     * Componente previo del glass pane (para restaurar al cerrar el diálogo)
     */
    private Component previousGlassPane;

    /**
     * Constructor del diálogo
     * 
     * @param parent
     * @param controlador
     * @param idPublicacion
     */
    public EliminarPublicacionDialog(final JFrame parent, final Controlador controlador, final int idPublicacion) {
        super(parent, "Eliminar Publicación", true);
        this.controlador = controlador;
        this.parentFrame = parent;
        this.idPublicacion = idPublicacion;
        initUI();
        setSize(new Dimension(360, 160));
        setLocationRelativeTo(parent);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(final WindowEvent e) {
                removeOverlay();
            }

            @Override
            public void windowClosing(final WindowEvent e) {
                removeOverlay();
            }
        });
    }

    /**
     * Inicializa la interfaz de usuario del diálogo
     */
    private void initUI() {
        // Configuración del diálogo
        getContentPane().setLayout(new BorderLayout());
        JPanel contenido = new JPanel(new BorderLayout());
        contenido.setBackground(Color.white);
        contenido.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Título y mensaje
        JLabel titulo = new JLabel("Eliminar Publicación");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 16f));
        contenido.add(titulo, BorderLayout.NORTH);

        // Mensaje de confirmación
        JLabel texto = new JLabel("Seguro que quieres eliminar la publicación?");
        texto.setBorder(BorderFactory.createEmptyBorder(12, 6, 12, 6));
        contenido.add(texto, BorderLayout.CENTER);

        // Botones
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.setBackground(Color.white);

        // Cancelar y Eliminar
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
            // Pedir al controlador que elimine la publicación
            boolean ok = controlador.getControladorEliminarPublicacion().eliminarPublicacion(idPublicacion);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Publicación eliminada (marcada como baja)", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                // Refresh views
                controlador.getControladorNavegacion().refrescarPublicaciones();
                controlador.getControladorNavegacion().refrescarPanelControl();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No es posible eliminar la publicación: existen préstamos activos o ocurrió un error",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        botones.add(cancelar);
        botones.add(eliminar);

        getContentPane().add(contenido, BorderLayout.CENTER);
        getContentPane().add(botones, BorderLayout.SOUTH);
    }

    /**
     * Sobrescribe setVisible para instalar un overlay semitransparente en el frame
     * padre mientras el diálogo está abierto.
     */
    @Override
    public void setVisible(final boolean b) {
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
        if (parentFrame == null) {
            return;
        }
        // Guardar el componente previo del glass pane para restaurarlo después
        RootPaneContainer rpc = (RootPaneContainer) parentFrame;
        Component current = rpc.getRootPane().getGlassPane();
        previousGlassPane = current;

        // Crear overlay semitransparente
        JPanel overlay = new JPanel();
        overlay.setOpaque(true);
        overlay.setBackground(new Color(217, 217, 217, 153));
        overlay.addMouseListener(new MouseAdapter() {
        });

        // Asignar overlay como glass pane
        rpc.getRootPane().setGlassPane(overlay);
        overlay.setVisible(true);
    }

    /**
     * Quita el overlay del frame padre
     */
    private void removeOverlay() {
        if (parentFrame == null) {
            return;
        }
        // Restaurar el componente previo del glass pane
        RootPaneContainer rpc = (RootPaneContainer) parentFrame;
        if (previousGlassPane != null) {
            rpc.getRootPane().setGlassPane(previousGlassPane);
            previousGlassPane.setVisible(false);
            previousGlassPane = null;
        } else {
            rpc.getRootPane().getGlassPane().setVisible(false);
        }
    }

}
