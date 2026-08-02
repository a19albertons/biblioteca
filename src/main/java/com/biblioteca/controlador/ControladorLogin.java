package com.biblioteca.controlador;

import com.biblioteca.modelo.TipoUsuario;
import com.biblioteca.modelo.Usuario;

/**
 * Controlador para el login
 */
public class ControladorLogin {
    /**
     * Verifica si el usuario es no válido
     * 
     * @param usuario
     */
    public boolean usuarioNoValido(Usuario usuario) {
        return usuario == null;
    }

    /**
     * Verifica si la cuenta del usuario está desactivada
     * 
     * @param usuario
     */
    public boolean cuentaDesactivada(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException(
                    "Compruebe el usuario con usuarioNoValido antes de llamar a este método.");
        }
        return !usuario.getEstado();
    }

    /**
     * Verifica si el usuario no es conserje
     * 
     * @param usuario
     * @return
     */
    public boolean usuarioNoConserje(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException(
                    "Compruebe el usuario con usuarioNoValido antes de llamar a este método.");
        }
        return usuario.getTipo() != TipoUsuario.C;
    }
}
