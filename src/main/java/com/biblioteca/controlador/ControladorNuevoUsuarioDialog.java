package com.biblioteca.controlador;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.biblioteca.conexiones.DBConnection;
import com.biblioteca.dao.UsuarioDAO;
import com.biblioteca.security.HashearContrasena;

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
    public ControladorNuevoUsuarioDialog(final DBConnection dbConnection) {
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
    public boolean crearUsuario(final String dni, final String nombre, final String apellidos, final String email,
            final String tipoCode) {
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
            String apellido1Formateado = apellido1.isEmpty() ? "" : apellido1.substring(0, 1).toUpperCase();
            String apellido2Formateado = apellido2.isEmpty() ? "" : apellido2.substring(0, 1).toUpperCase();
            String usuarioConsultar = "A" + fecha2digitos + nombre.trim().toUpperCase().charAt(0)
                    + nombre.trim().substring(1) + apellido1Formateado + apellido2Formateado;

            try {
                // Iniciar transacción
                conexion.setAutoCommit(false);
                int numeroUsuariosMismoPatron = usuarioDAO.consultaNumeroUsuariosPorUsuario(usuarioConsultar);

                // Genera el nombre de usuario final basado en el patrón y el número de usuarios
                // existentes
                String usuarioFinal;
                if (numeroUsuariosMismoPatron == -1) {
                    return false;
                } else if (numeroUsuariosMismoPatron == 0) {
                    usuarioFinal = usuarioConsultar;
                } else {
                    usuarioFinal = usuarioConsultar + (numeroUsuariosMismoPatron + 1);
                }

                char[] contrasenaCharArray = contrasena.toCharArray();
                String contrasenaHasheada = HashearContrasena.hash(contrasenaCharArray);

                boolean ok = usuarioDAO.insertarUsuario(conexion, dni.trim(), nombre.trim(), apellido1, apellido2,
                        email,
                        contrasenaHasheada, tipoCode, true, usuarioFinal);
                // Si no se pudo insertar, hacer rollback y devolver false
                if (!ok) {
                    conexion.rollback();
                    return false;
                }
                // CPD-OFF
                // Confirmar transacción
                conexion.commit();
                return true;
            } catch (SQLException e) {
                try {
                    // Hacer rollback en caso de error
                    conexion.rollback();
                } catch (SQLException ex) {
                    System.out.println("Error al hacer rollback: " + ex.getMessage());
                }
                System.out.println(e.getMessage());
                System.out.println(e.getCause());
                return false;
            } finally {
                try {
                    // Restaurar auto-commit y cerrar conexión
                    conexion.setAutoCommit(true);
                } catch (SQLException ex) {
                    System.out.println("Error al restaurar auto-commit: " + ex.getMessage());
                }
            }
        } catch (SQLException e1) {
            System.out.println("Error al obtener conexión: " + e1.getMessage());
            return false;
        }
        // CPD-ON
    }
}
