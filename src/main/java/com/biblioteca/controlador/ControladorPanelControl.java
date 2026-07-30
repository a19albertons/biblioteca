package com.biblioteca.controlador;

import com.biblioteca.conexiones.DBConnection;
import com.biblioteca.dao.CicloDAO;
import com.biblioteca.dao.PrestamoDAO;
import com.biblioteca.dao.PublicacionDAO;
import com.biblioteca.dao.UsuarioDAO;

/**
 * Controlador para el panel de control
 */
public class ControladorPanelControl {
    /**
     * DBConnection para conexiones a la base de datos
     */
    private final DBConnection dbConnection;

    /**
     * Constructor con DBConnection (inyección)
     * 
     * @param dbConnection
     */
    public ControladorPanelControl(DBConnection dbConnection) {
        if (dbConnection == null) {
            throw new IllegalArgumentException("DBConnection cannot be null");
        }
        this.dbConnection = dbConnection;
    }

    /**
     * Obtiene el número de préstamos realizados hoy
     * 
     * @return
     */
    public String obtenerPrestamosHoy() {
        PrestamoDAO prestamoDAO = new PrestamoDAO(this.dbConnection);
        return prestamoDAO.prestamosHoy();

    }

    /**
     * Obtiene el número de préstamos pendientes
     * 
     * @return
     */
    public String obtenerPrestamosPendientes() {
        PrestamoDAO prestamoDAO = new PrestamoDAO(this.dbConnection);
        return prestamoDAO.prestamosPendientes();
    }

    /**
     * Obtiene el número total de socios activos
     * 
     * @return
     */
    public String obtenerTotalSociosActivos() {
        UsuarioDAO usuarioDAO = new UsuarioDAO(this.dbConnection);
        return usuarioDAO.totalSociosActivos();
    }

    /**
     * Obtiene los últimos movimientos de préstamos
     * 
     * @return
     */
    public String[][] obtenerUltimosMovimientos() {
        PrestamoDAO prestamoDAO = new PrestamoDAO(this.dbConnection);
        return prestamoDAO.ultimosMovimientos();
    }

    /**
     * Obtiene la lista de ciclos
     * 
     * @return
     */
    public String[] listaCiclos() {
        CicloDAO cicloDAO = new CicloDAO(this.dbConnection);
        String[] listaCiclos = cicloDAO.listaCiclos();
        return listaCiclos;
    }

    /**
     * Obtiene la lista de editoriales
     * 
     * @return
     */
    public String[] listaEditoriales() {
        PublicacionDAO publicacionDAO = new PublicacionDAO(this.dbConnection);
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
        PublicacionDAO publicacionDAO = new PublicacionDAO(this.dbConnection);
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
