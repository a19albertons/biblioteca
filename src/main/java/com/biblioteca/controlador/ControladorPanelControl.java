package com.biblioteca.controlador;

import java.sql.Connection;

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
    public ControladorPanelControl(final DBConnection dbConnection) {
        if (dbConnection == null) {
            throw new IllegalArgumentException("DBConnection cannot be null");
        }
        this.dbConnection = dbConnection;
    }

    /**
     * Obtiene el número de préstamos realizados hoy
     * 
     * @return String con el número de préstamos realizados hoy
     */
    public String obtenerPrestamosHoy() {
        try (Connection conexion = this.dbConnection.getConnection()) {
            if (conexion == null) {
                System.out.println("No se puede obtener conexión a BD");
                return "error";
            }
            PrestamoDAO prestamoDAO = new PrestamoDAO(conexion);
            return prestamoDAO.prestamosHoy();
        } catch (Exception e) {
            // En caso de error, devolver "error" y loguear el error
            System.out.println("Error al obtener conexión: " + e.getMessage());
            return "error";
        }

    }

    /**
     * Obtiene el número de préstamos pendientes
     * 
     * @return String con el número de préstamos pendientes
     */
    public String obtenerPrestamosPendientes() {
        try (Connection conexion = this.dbConnection.getConnection()) {
            if (conexion == null) {
                System.out.println("No se puede obtener conexión a BD");
                return "error";
            }
            PrestamoDAO prestamoDAO = new PrestamoDAO(conexion);
            return prestamoDAO.prestamosPendientes();
        } catch (Exception e) {
            // En caso de error, devolver "error" y loguear el error
            System.out.println("Error al obtener conexión: " + e.getMessage());
            return "error";
        }
    }

    /**
     * Obtiene el número total de socios activos
     * 
     * @return String con el número total de socios activos
     */
    public String obtenerTotalSociosActivos() {
        try (Connection conexion = this.dbConnection.getConnection()) {
            if (conexion == null) {
                System.out.println("No se puede obtener conexión a BD");
                return "error";
            }
            UsuarioDAO usuarioDAO = new UsuarioDAO(conexion);
            return usuarioDAO.totalSociosActivos();
        } catch (Exception e) {
            // En caso de error, devolver "error" y loguear el error
            System.out.println("Error al obtener conexión: " + e.getMessage());
            return "error";
        }

    }

    /**
     * Obtiene los últimos movimientos de préstamos
     * 
     * @return String[][] con los últimos movimientos de préstamos
     */
    public String[][] obtenerUltimosMovimientos() {
        try (Connection conexion = this.dbConnection.getConnection()) {
            if (conexion == null) {
                System.out.println("No se puede obtener conexión a BD");
                return new String[0][0];
            }
            PrestamoDAO prestamoDAO = new PrestamoDAO(conexion);
            return prestamoDAO.ultimosMovimientos();
        } catch (Exception e) {
            System.out.println("Error al obtener conexión: " + e.getMessage());
            return new String[0][0];
        }
    }

    /**
     * Obtiene la lista de ciclos
     * 
     * @return String[] con la lista de ciclos
     */
    public String[] listaCiclos() {
        String[] listaCiclos = new String[0];
        try (Connection conexion = this.dbConnection.getConnection()) {
            CicloDAO cicloDAO = new CicloDAO(conexion);
            listaCiclos = cicloDAO.listaCiclos();
        } catch (Exception e) {
            // En caso de error, devolver lista vacía y loguear el error
            System.out.println("Error al obtener conexión: " + e.getMessage());
            listaCiclos = new String[0];
        }
        return listaCiclos;
    }

    /**
     * Obtiene la lista de editoriales
     * 
     * @return String[] con la lista de editoriales
     */
    public String[] listaEditoriales() {
        try (Connection conexion = this.dbConnection.getConnection()) {
            if (conexion == null) {
                System.out.println("No se puede obtener conexión a BD");
                return new String[0];
            }
            PublicacionDAO publicacionDAO = new PublicacionDAO(conexion);
            String[] listaEditoriales = publicacionDAO.listaEditoriales();
            return listaEditoriales;
        } catch (Exception e) {
            System.out.println("Error al obtener conexión: " + e.getMessage());
            return new String[0];
        }
    }

    /**
     * Obtiene el resumen de publicaciones
     * 
     * @return String[][] con columnas: titulo, isbn, autores, ciclos, editorial,
     *         disponibles, id
     */
    public String[][] listaPublicacionesResumen() {
        try (Connection conexion = this.dbConnection.getConnection()) {
            if (conexion == null) {
                System.out.println("No se puede obtener conexión a BD");
                return new String[0][0];
            }
            // Recupera la string con el resumen de publicaciones
            PublicacionDAO publicacionDAO = new PublicacionDAO(conexion);
            String[][] resumen = publicacionDAO.listaPublicacionesResumen();
            return resumen;
        } catch (Exception e) {
            System.out.println("Error al obtener conexión: " + e.getMessage());
            return new String[0][0];
        }
    }

    /*
     * La creación/persistencia de nuevas publicaciones fue refactorizada a
     * `ControladorNuevaPublicacionDialog` para separar responsabilidades de UI
     * y lógica de persistencia. Use ese controlador para crear libros y
     * revistas.
     */
    // NOTA: métodos mover a ControladorNuevaPublicacionDialog

}
