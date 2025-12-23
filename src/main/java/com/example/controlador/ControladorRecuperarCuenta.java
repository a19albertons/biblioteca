package com.example.controlador;

import com.example.dao.UsuarioDAO;
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


}
