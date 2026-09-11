package com.biblioteca.vista.pantalla;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * Clase base para las vistas de la aplicación
 */
public class VistaBase {

    /**
     * Valida usuario y ejemplar
     * @param usuarioSeleccionado array holder con el ID de usuario seleccionado
     * @param txtIdEjemplar JTextField con el ID del ejemplar
     * @return true si validación exitosa, false si hay error
     */
    public boolean validarUsuarioYEjemplar(final int[] usuarioSeleccionado, final JTextField txtIdEjemplar) {
        // Validar usuario seleccionado
        if (usuarioSeleccionado[0] == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione primero un usuario válido", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // Validar ID de ejemplar
        String idEjStr = txtIdEjemplar.getText().trim();
        if (idEjStr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Introduzca el ID del ejemplar", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // Validar que sea entero
        
        try {
            Integer.parseInt(idEjStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "ID de ejemplar inválido", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
