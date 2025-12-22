package com.example.vista;

import com.example.controlador.Controlador;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Clase para la vista Ejemplares
 */
public class Ejemplares {

    Controlador controlador;

    public Ejemplares(Controlador controlador) {
        this.controlador = controlador;
    }

    public JPanel pantalla() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Pantalla de Ejemplares"));
        return panel;
    }

}  
