package com.example.utilities;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.UIManager;

/**
 * Utilidades para fuente Open Sans en la UI.
 */
public final class Fonts {
    /**
     * Logger para la clase Fonts
     */
    private static final Logger LOGGER = Logger.getLogger(Fonts.class.getName());
    /**
     * Fuente Open Sans (texto normal)
     */
    private static Font openSansRegular = null;

    /**
     * Constructor privado para evitar instanciación
     */
    private Fonts() {
    }

    /**
     * Carga y registra una fuente desde recursos embebidos
     * 
     * @param resourceName
     * @return
     */
    private static Font loadAndRegister(String resourceName) {
        // Intentar cargar la fuente desde recursos
        try (InputStream is = Fonts.class.getResourceAsStream("/fonts/" + resourceName)) {
            // Si no se encuentra el recurso, devolver null
            if (is == null) {
                LOGGER.log(Level.FINE, "Fuente no encontrada en recursos: {0}", resourceName);
                return null;
            }
            // Crear la fuente y registrarla
            Font f = Font.createFont(Font.TRUETYPE_FONT, is);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(f);
            LOGGER.log(Level.INFO, "Fuente registrada: {0}", resourceName);
            return f;
        } catch (IOException | FontFormatException e) {
            LOGGER.log(Level.WARNING, "No se pudo cargar la fuente {0}: {1}",
                    new Object[] { resourceName, e.getMessage() });
            return null;
        }
    }

    /**
     * Obtiene la fuente Open Sans en tamaño específico (texto normal).
     * Si la fuente no está cargada, devuelve una fuente por defecto con familia
     * "Open Sans".
     * 
     * @param size
     * @return
     */
    public static Font openSans(float size) {
        // Devuelve la fuente Open Sans en tamaño específico
        if (openSansRegular != null) {
            return openSansRegular.deriveFont(Font.PLAIN, Math.round(size));
        }
        return new Font("Open Sans", Font.PLAIN, Math.round(size));
    }

    /**
     * Aplica Open Sans (texto normal) como fuente por defecto para JLabel y
     * JButton.
     * Intentará cargar las TTF desde `resources/fonts/` si existen y registrar las
     * fuentes.
     */
    public static void applyDefaultOpenSans() {
        // Intentar cargar las fuentes embebidas (si existen en recursos)
        openSansRegular = loadAndRegister("OpenSans-Regular.ttf");

        // Obtener tamaño por defecto de JLabel para mantener coherencia
        Font labelFont = UIManager.getFont("Label.font");
        int size = (labelFont != null) ? labelFont.getSize() : 12;

        // Configurar la fuente base
        Font base;
        if (openSansRegular != null) {
            base = openSansRegular.deriveFont(Font.PLAIN, size);
        } else {
            // Fallback: intentar usar familia por nombre (si está instalada)
            base = new Font("Open Sans", Font.PLAIN, size);
            LOGGER.log(Level.INFO, "Usando fallback de familia 'Open Sans' o la fuente por defecto del sistema");
        }

        // Aplicar la fuente a componentes UI comunes
        UIManager.put("Label.font", base);
        UIManager.put("Button.font", base);
        UIManager.put("TextField.font", base);
        UIManager.put("TextArea.font", base);
        UIManager.put("ComboBox.font", base);
        UIManager.put("CheckBox.font", base);
    }
}
