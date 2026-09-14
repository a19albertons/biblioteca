package com.biblioteca.vista.JSwing;

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

public class JButtonEditar extends JButton {
    public JButtonEditar() {
        // Botón editar con fondo translúcido pintado manualmente
        URL editarIconUrl = AppResources.editarPath();
        if (editarIconUrl != null) {
            Image img = new ImageIcon(editarIconUrl).getImage().getScaledInstance(12, 12, Image.SCALE_SMOOTH);
            setIcon(new ImageIcon(img));
        }
        setPreferredSize(new Dimension(24, 24));
        setToolTipText("Editar");
        setBorder(null);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setMargin(new Insets(0, 0, 0, 0));
    }

    @Override
    protected void paintComponent(final Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.SrcOver);
        g2.setColor(new Color(70, 141, 174, 102));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
        super.paintComponent(g);
    }
}
