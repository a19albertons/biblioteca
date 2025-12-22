package com.example.vista;

import com.example.controlador.Controlador;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Clase para la vista Sanción manual
 */
public class SancionManual {

    /**
     * Controlador de la aplicación
     */
    Controlador controlador;

    /**
     * Constructor de la vista SancionManual
     *
     * @param controlador controlador principal
     */
    public SancionManual(Controlador controlador) {
        this.controlador = controlador;
    }

    /**
     * Muestra la pantalla para crear sanciones manuales
     *
     * @return JPanel con la interfaz de sanciones
     */
    public JPanel pantalla() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Pantalla de Sanción Manual"));
        return panel;
    }

}  
