package com.biblioteca.controlador;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.biblioteca.conexiones.DBConnection;
import com.biblioteca.dao.UsuarioDAO;

/**
 * Controlador dedicado a la creación de nuevos usuarios (socios) desde el
 * diálogo.
 * Encapsula validaciones sencillas y la operación transaccional de inserción.
 */
public class ControladorNuevoUsuarioDialog {
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
    public ControladorNuevoUsuarioDialog(DBConnection dbConnection) {
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
        try (Connection conexion = this.dbConnection.getConnection()) {
            // Comprobar conexión
            if (conexion == null) {
                System.out.println("No se puede obtener conexión a BD");
                return false;
            }

            // Crear DAO de usuario
            UsuarioDAO usuarioDAO = new UsuarioDAO(conexion);

            // Validaciones básicas
            if (dni == null || dni.trim().isEmpty() || nombre == null || nombre.trim().isEmpty()) {
                return false;
            }
            // Preparar datos
            String apellido1 = (apellidos != null) ? apellidos.trim() : "";
            String apellido2 = ""; // dejamos el campo apellido2 vacío por simplicidad
            String contrasena = dni.trim(); // la contraseña inicial es el DNI

            // Crear string de usuario (login) a partir del nombre y apellido1
            LocalDate fechaActual = LocalDate.now();
            String fecha2digitos = fechaActual.format(DateTimeFormatter.ofPattern("yy"));
            String apellido1Formateado = apellido1.substring(0, 0).toUpperCase();
            String apellido2Formateado = apellido2.substring(0, 0).toUpperCase();
            String usuarioConsultar = "A"+fecha2digitos+nombre.trim().toUpperCase().charAt(0)+nombre.trim().substring(1)+apellido1Formateado+apellido2Formateado;


            try {
                // Iniciar transacción
                conexion.setAutoCommit(false);
                int numeroUsuariosMismoPatron = usuarioDAO.consultaNumeroUsuariosPorUsuario(usuarioConsultar);

                String usuarioFinal;
                if (numeroUsuariosMismoPatron == -1) {
                    return false;
                } else if (numeroUsuariosMismoPatron == 0) {
                    usuarioFinal = usuarioConsultar;
                } else {
                    usuarioFinal = usuarioConsultar + (numeroUsuariosMismoPatron + 1);
                }

                boolean ok = usuarioDAO.insertarUsuario(conexion, dni.trim(), nombre.trim(), apellido1, apellido2,
                        email,
                        contrasena, tipoCode, true, usuarioFinal);
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
        } catch (SQLException e1) {
            System.out.println("Error al obtener conexión: " + e1.getMessage());
            return false;
        }

    }
}
