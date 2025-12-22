package com.example.vista;

import com.example.controlador.Controlador;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Clase para la vista Gestión de usuarios
 */
public class GestionUsuarios {

    /**
     * Controlador de la aplicación
     */
    Controlador controlador;

    /**
     * Constructor de la vista GestionUsuarios
     *
     * @param controlador controlador principal
     */
    public GestionUsuarios(Controlador controlador) {
        this.controlador = controlador;
    }

    /**
     * Muestra la pantalla de gestión de usuarios
     *
     * @return JPanel con la interfaz de gestión de usuarios
     */
    public JPanel pantalla() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Pantalla de Gestión de Usuarios"));
        return panel;
    }

}  
