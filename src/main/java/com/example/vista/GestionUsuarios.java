package com.example.vista;

import com.example.controlador.Controlador;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Clase para la vista Gestión de usuarios
 */
public class GestionUsuarios {

    Controlador controlador;

    public GestionUsuarios(Controlador controlador) {
        this.controlador = controlador;
    }

    public JPanel pantalla() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Pantalla de Gestión de Usuarios"));
        return panel;
    }

}  
