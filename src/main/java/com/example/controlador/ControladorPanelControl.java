package com.example.controlador;

import com.example.dao.PrestamoDAO;
import com.example.dao.UsuarioDAO;
import com.example.modelo.Usuario;

/**
 * Controlador para el panel de control
 */
public class ControladorPanelControl {

    /**
     * Obtiene el número de préstamos realizados hoy
     * 
     * @return
     */
    public String obtenerPrestamosHoy() {
        PrestamoDAO prestamoDAO = new PrestamoDAO();
        return prestamoDAO.prestamosHoy();

    }

    /**
     * Obtiene el número de préstamos pendientes
     * 
     * @return
     */
    public String obtenerPrestamosPendientes() {
        PrestamoDAO prestamoDAO = new PrestamoDAO();
        return prestamoDAO.prestamosPendientes();
    }

    /**
     * Obtiene el número total de socios activos
     * 
     * @return
     */
    public String obtenerTotalSociosActivos() {
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        return usuarioDAO.totalSociosActivos();
    }

    /**
     * Obtiene los últimos movimientos de préstamos
     * @return
     */
    public String[][] obtenerUltimosMovimientos() {
        PrestamoDAO prestamoDAO = new PrestamoDAO();
        return prestamoDAO.ultimosMovimientos();
    }

}
