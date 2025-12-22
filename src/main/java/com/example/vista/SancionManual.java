package com.example.vista;

import com.example.controlador.Controlador;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Clase para la vista Sanción manual
 */
public class SancionManual {

    Controlador controlador;

    public SancionManual(Controlador controlador) {
        this.controlador = controlador;
    }

    public JPanel pantalla() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Pantalla de Sanción Manual"));
        return panel;
    }

}  
