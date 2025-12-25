package com.example.controlador;

import java.sql.Connection;

import com.example.dao.UsuarioDAO;

/**
 * Controlador dedicado a la creación de nuevos usuarios (socios) desde el
 * diálogo.
 * Encapsula validaciones sencillas y la operación transaccional de inserción.
 */
public class ControladorNuevoUsuarioDialog {
    /**
     * DBConnection para conexiones a la base de datos
     */
    private final com.example.conexiones.DBConnection dbConnection;

    /**
     * Constructor que permite inyectar una `DBConnection` (recomendado para tests
     * y para la nueva arquitectura).
     * 
     * @param dbConnection
     */
    public ControladorNuevoUsuarioDialog(com.example.conexiones.DBConnection dbConnection) {
        if (dbConnection == null) {
            throw new IllegalArgumentException("DBConnection cannot be null");
        }
        this.dbConnection = dbConnection;
    }

    /**
     * Crea un nuevo usuario. La contraseña se inicializa con el DNI proporcionado.
     *
     * @param dni
     * @param nombre
     * @param apellidos (texto libre; se guardará en `apellido1` y `apellido2` queda
     *                  vacío)
     * @param email
     * @param tipoCode  código de tipo de usuario (E,P,A,C,L)
     * @return true si la creación y commit fue satisfactoria
     */
    public boolean crearUsuario(String dni, String nombre, String apellidos, String email, String tipoCode) {
        UsuarioDAO usuarioDAO = new UsuarioDAO(this.dbConnection);
        // Validaciones básicas
        if (dni == null || dni.trim().isEmpty() || nombre == null || nombre.trim().isEmpty()) {
            return false;
        }
        // Preparar datos
        String apellido1 = (apellidos != null) ? apellidos.trim() : "";
        String apellido2 = ""; // dejamos el campo apellido2 vacío por simplicidad
        String contrasena = dni.trim(); // la contraseña inicial es el DNI

        Connection conexion = this.dbConnection.getConnection();
        // Comprobar conexión
        if (conexion == null) {
            System.out.println("No se puede obtener conexión a BD");
            return false;
        }
        try {
            // Iniciar transacción
            conexion.setAutoCommit(false);
            boolean ok = usuarioDAO.insertarUsuario(conexion, dni.trim(), nombre.trim(), apellido1, apellido2, email,
                    contrasena, tipoCode, true);
            // Si no se pudo insertar, hacer rollback y devolver false
            if (!ok) {
                conexion.rollback();
                return false;
            }
            // Confirmar transacción
            conexion.commit();
            return true;
        } catch (Exception e) {
            try {
                // Hacer rollback en caso de error
                conexion.rollback();
            } catch (Exception ex) {
                System.out.println("Error al hacer rollback: " + ex.getMessage());
            }
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            return false;
        } finally {
            try {
                // Restaurar auto-commit y cerrar conexión
                conexion.setAutoCommit(true);
                conexion.close();
            } catch (Exception ex) {
                System.out.println("Error cerrando conexión: " + ex.getMessage());
            }
        }
    }
}
