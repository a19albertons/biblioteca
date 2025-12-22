package com.example.utilities;

import java.awt.Font;
import javax.swing.UIManager;

/**
 * Utilidades para fuente Open Sans en la UI.
 */
public final class Fonts {

    private Fonts() {}

    public static Font openSans(float size) {
        return new Font("Open Sans", Font.PLAIN, Math.round(size));
    }

    /**
     * Aplica Open Sans (texto normal) como fuente por defecto para JLabel y JButton.
     * Si la fuente no está instalada en el sistema, Swing usará la fuente por defecto
     * y la familia será ignorada, pero el estilo y tamaño se aplicarán.
     */
    public static void applyDefaultOpenSans() {
        Font labelFont = UIManager.getFont("Label.font");
        int size = (labelFont != null) ? labelFont.getSize() : 12;
        Font open = new Font("Open Sans", Font.PLAIN, size);
        UIManager.put("Label.font", open);
        UIManager.put("Button.font", open);
        UIManager.put("TextField.font", open);
        UIManager.put("TextArea.font", open);
        UIManager.put("ComboBox.font", open);
        UIManager.put("CheckBox.font", open);
    }
}
