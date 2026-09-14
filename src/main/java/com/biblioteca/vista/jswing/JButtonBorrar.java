package com.biblioteca.vista.jswing;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import com.biblioteca.utilities.AppResources;

public class JButtonBorrar extends JButton {
    /**
     * Constructor del botón de eliminar con fondo translúcido pintado manualmente
     */
    public JButtonBorrar() {
        URL borrarIconUrl = AppResources.eliminarPath();
        if (borrarIconUrl != null) {
            final Image img = new ImageIcon(borrarIconUrl).getImage().getScaledInstance(12, 12, Image.SCALE_SMOOTH);
            setIcon(new ImageIcon(img));
        }
        setPreferredSize(new Dimension(24, 24));
        setToolTipText("Eliminar");
        setBorder(null);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setMargin(new Insets(0, 0, 0, 0));
    }

    /**
     * Pintar el botón con fondo translúcido
     */
    @Override
    protected void paintComponent(final Graphics g) {
        final Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.SrcOver);
        g2.setColor(new Color(192, 57, 43, 102));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
        super.paintComponent(g);
    }
}
