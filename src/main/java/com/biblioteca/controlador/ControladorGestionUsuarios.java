package com.biblioteca.controlador;

import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.table.DefaultTableModel;

import com.biblioteca.conexiones.DBConnection;
import com.biblioteca.dao.UsuarioDAO;

/**
 * Controlador para la gestión de usuarios
 */
public class ControladorGestionUsuarios {
    /**
     * DBConnection para conexiones a la base de datos
     */
    private final DBConnection dbConnection;

    /**
     * Constructor que permite inyectar una `DBConnection` (recomendado para tests
     * y para la nueva arquitectura).
     * 
     * @param dbConnection
     */
    public ControladorGestionUsuarios(final DBConnection dbConnection) {
        if (dbConnection == null) {
            throw new IllegalArgumentException("DBConnection cannot be null");
        }
        this.dbConnection = dbConnection;
    }

    /**
     * Obtiene la lista de usuarios y su estado de sanción activa
     * 
     * @return String[][] con columnas: id, nombre, sancion_activa
     */
    public String[][] obtenerUsuariosYEstadoSancionActiva() {
        try (Connection conexion = this.dbConnection.getConnection()) {
            if (conexion == null) {
                System.out.println("No se puede obtener conexión a BD");
                return null;
            }
            UsuarioDAO usuarioDAO = new UsuarioDAO(conexion);
            return usuarioDAO.listaUsuariosYEstadoSancionActiva();
        } catch (SQLException e) {
            System.out.println("Error al obtener conexión: " + e.getMessage());
            return null;
        }

    }

    /**
     * Carga los datos de los usuarios en el modelo de tabla
     * 
     * @param rawData String[][] con los datos crudos de la base de datos
     * @param modelo DefaultTableModel donde se cargarán los datos
     * @return String[][] con los datos procesados para la vista
     */
    public String[][] cargarDatosEnTabla(String[][] rawData, DefaultTableModel modelo) {
        if (rawData == null) {
            return new String[0][0];
        }
        
        // Limpiar modelo
        for (int i = modelo.getRowCount() - 1; i >= 0; i--) {
            modelo.removeRow(i);
        }
        
        // Procesar datos
        String[][] procesados = new String[rawData.length][6];
        for (int i = 0; i < rawData.length; i++) {
            String[] r = rawData[i];
            String id = (r.length > 0 && r[0] != null) ? r[0] : "";
            String dni = (r.length > 1 && r[1] != null) ? r[1] : "";
            String nombre = (r.length > 2 && r[2] != null) ? r[2] : "";
            String tipo = (r.length > 4 && r[4] != null) ? r[4] : "";
            String sancion = (r.length > 3 && r[3] != null) ? r[3] : "";
            procesados[i] = new String[] { id, dni, nombre, tipo, sancion, "" };
        }
        
        // Cargar en modelo
        for (String[] r : procesados) {
            modelo.addRow(r);
        }
        
        return procesados;
    }

    /**
     * Obtiene la lista de usuarios sancionables (estudiantes activos)
    *
     * @return String[][] con columnas: id, dni, nombre_completo, tipo
     */
    public String[][] obtenerUsuariosSancionables() {
        try (Connection conexion = this.dbConnection.getConnection()) {
            if (conexion == null) {
                System.out.println("No se puede obtener conexión a BD");
                return null;
            }
            UsuarioDAO usuarioDAO = new UsuarioDAO(conexion);
            return usuarioDAO.obtenerUsuariosSancionables();
        } catch (SQLException e) {
            System.out.println("Error al obtener conexión: " + e.getMessage());
            return null;
        }
    }
}
