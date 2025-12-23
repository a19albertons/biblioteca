package com.example.controlador;

import com.example.dao.UsuarioDAO;
import com.example.modelo.Usuario;

/**
 * Clase para el controlador de inicio de sesión
 */
public class ControladorInicioSesion {
    
    public Usuario iniciarSesion(String usuario, String contrasena) {
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        return usuarioDAO.consultaInicioSesion(usuario, contrasena);
    } 
}
