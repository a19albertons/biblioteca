package com.biblioteca.vista.dialogo;

import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.RootPaneContainer;

/**
 * Clase base para diálogos que requieren overlay en el frame padre.
 */
public abstract class OverlayDialog extends JDialog {
    /**
     * Frame padre para overlays
     */
    private final JFrame parentFrame;
    /**
     * Glass pane previo del frame padre
     */
    private Component previousGlassPane;

    /**
     * Constructor
     * 
     * @param parent
     * @param titulo
     */
    public OverlayDialog(final JFrame parent, final String titulo) {
        super(parent, titulo, true);
        this.parentFrame = parent;
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
     * Instala un overlay translúcido en el frame padre
     */
    protected void installOverlay() {
        if (parentFrame == null) {
            return;
        }
        // Recordar el glass pane previo
        RootPaneContainer rpc = (RootPaneContainer) parentFrame;
        Component current = rpc.getRootPane().getGlassPane();
        previousGlassPane = current;
        JPanel overlay = new JPanel();

        // Panel translúcido gris
        overlay.setOpaque(true);
        overlay.setBackground(new java.awt.Color(217, 217, 217, 153));
        rpc.getRootPane().setGlassPane(overlay);
        overlay.setVisible(true);
    }

    /**
     * Quita el overlay del frame padre
     */
    protected void removeOverlay() {
        if (parentFrame == null) {
            return;
        }
        // Restaurar el glass pane previo (si lo teníamos)
        RootPaneContainer rpc = (RootPaneContainer) parentFrame;
        if (previousGlassPane != null) {
            rpc.getRootPane().setGlassPane(previousGlassPane);
            previousGlassPane.setVisible(false);
            previousGlassPane = null;
        } else {
            rpc.getRootPane().getGlassPane().setVisible(false);
        }
    }

    /**
     * Muestra u oculta el diálogo, instalando o quitando el overlay en el frame
     * padre
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
}
