package com.example.controlador;

import com.example.dao.AutorDAO;
import com.example.dao.CicloDAO;
import com.example.dao.ModuloDAO;
import com.example.dao.PrestamoDAO;
import com.example.dao.PublicacionDAO;
import com.example.dao.TemaDAO;
import com.example.dao.UsuarioDAO;
import java.time.LocalDate;

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
     * 
     * @return
     */
    public String[][] obtenerUltimosMovimientos() {
        PrestamoDAO prestamoDAO = new PrestamoDAO();
        return prestamoDAO.ultimosMovimientos();
    }

    /**
     * Obtiene la lista de ciclos
     * 
     * @return
     */
    public String[] listaCiclos() {
        CicloDAO cicloDAO = new CicloDAO();
        String[] listaCiclos = cicloDAO.listaCiclos();
        return listaCiclos;
    }

    /**
     * Obtiene la lista de editoriales
     * 
     * @return
     */
    public String[] listaEditoriales() {
        PublicacionDAO publicacionDAO = new PublicacionDAO();
        String[] listaEditoriales = publicacionDAO.listaEditoriales();
        return listaEditoriales;
    }

    /**
     * Obtiene el resumen de publicaciones
     * 
     * @return String[][] con columnas: titulo, isbn, autores, ciclos, editorial,
     *         disponibles, id
     */
    public String[][] listaPublicacionesResumen() {
        PublicacionDAO publicacionDAO = new PublicacionDAO();
        String[][] resumen = publicacionDAO.listaPublicacionesResumen();
        return resumen;
    }

    /*
     * La creación/persistencia de nuevas publicaciones fue refactorizada a
     * `ControladorNuevaPublicacionDialog` para separar responsabilidades de UI
     * y lógica de persistencia. Use ese controlador para crear libros y
     * revistas.
     */
    // NOTA: métodos mover a ControladorNuevaPublicacionDialog

}
