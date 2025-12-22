package com.example.vista;

import com.example.controlador.Controlador;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Clase para la vista Devolver préstamo
 */
public class DevolverPrestamo {

    Controlador controlador;

    public DevolverPrestamo(Controlador controlador) {
        this.controlador = controlador;
    }

    public JPanel pantalla() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Pantalla de Devolver Préstamo"));
        return panel;
    }

}
