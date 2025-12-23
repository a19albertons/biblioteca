package com.example.controlador;

import com.example.dao.UsuarioDAO;
import com.example.modelo.TipoUsuario;
import com.example.modelo.Usuario;

/**
 * Controlador para la recuperación de cuenta
 */
public class ControladorRecuperarCuenta {

    /**
     * Recupera la cuenta del usuario basado en el identificador proporcionado
     * 
     * @param trim
     * @return
     */
    public Usuario recuperarCuenta(String trim) {
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        return usuarioDAO.consultaRecuperarCuenta(trim);
    }

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
