package com.example.controlador;

import com.example.dao.UsuarioDAO;
import com.example.modelo.TipoUsuario;
import com.example.modelo.Usuario;

/**
 * Clase para el controlador de inicio de sesión
 */
public class ControladorInicioSesion {

    /**
     * Inicia sesión con las credenciales proporcionadas
     * 
     * @param usuario
     * @param contrasena
     * @return
     */
    public Usuario iniciarSesion(String usuario, String contrasena) {
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        return usuarioDAO.consultaInicioSesion(usuario, contrasena);
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
            throw new IllegalArgumentException("Compruebe el usuario con usuarioNoValido antes de llamar a este método.");
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
            throw new IllegalArgumentException("Compruebe el usuario con usuarioNoValido antes de llamar a este método.");
        }
        return usuario.getTipo() != TipoUsuario.C;
    }
}