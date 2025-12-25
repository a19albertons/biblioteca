package com.example;

import static org.junit.Assert.assertTrue;

import com.example.controlador.Controlador;
import com.example.dao.UsuarioDAO;
import org.junit.Test;

/**
 * Test para crear un nuevo usuario
 */
public class NuevoUsuarioTest {

    @Test
    public void crearUsuario() {
        Controlador c = new Controlador();
        String dni = "15151515Z";
        String nombre = "TestUser";
        String apellidos = "Prueba Uno";
        String email = "testuser@example.com";
        String tipo = "A"; // Administrativo

        boolean ok = c.getControladorNuevoUsuarioDialog().crearUsuario(dni, nombre, apellidos, email, tipo);
        assertTrue(ok);

        UsuarioDAO dao = new UsuarioDAO(c.getDbConnection());
        // debería encontrarse por usuario generado o por email
        assertTrue(dao.consultaRecuperarCuenta(email) != null);

        // limpiamos dejando el DB como estaba: borrar fila insertada
        try (java.sql.Connection conexion = c.getDbConnection().getConnection();
                java.sql.PreparedStatement ps = conexion.prepareStatement("DELETE FROM usuarios WHERE email = ?")) {
            ps.setString(1, email);
            ps.executeUpdate();
        } catch (Exception e) {
            // ignore
        }
    }
}
