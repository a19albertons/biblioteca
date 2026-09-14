package com.biblioteca.controlador;

import javax.annotation.Nonnull;

import com.biblioteca.modelo.TipoUsuario;
import com.biblioteca.modelo.Usuario;

/**
 * Controlador para el login
 */
public class ControladorLogin {
    /**
     * Verifica si el usuario es no válido
     * 
     * @param usuario el usuario a verificar
     * @return true si el usuario es nulo
     */
    public boolean usuarioNoValido(final Usuario usuario) {
        return usuario == null;
    }

    /**
     * Verifica si la cuenta del usuario está desactivada
     * 
     * @param usuario el usuario a verificar
     * @return true si la cuenta está desactivada
     */
    public boolean cuentaDesactivada(@Nonnull final Usuario usuario) {
        return !usuario.getEstado();
    }

    /**
     * Verifica si el usuario no es conserje
     * 
     * @param usuario el usuario a verificar
     * @return true si el usuario no es conserje
     */
    public boolean usuarioNoConserje(final Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException(
                    "Compruebe el usuario con usuarioNoValido antes de llamar a este método.");
        }
        return usuario.getTipo() != TipoUsuario.C;
    }
}
