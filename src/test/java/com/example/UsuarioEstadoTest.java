package com.example;

import static org.junit.Assert.assertTrue;

import com.example.dao.UsuarioDAO;
import com.example.controlador.Controlador;
import org.junit.Test;

/**
 * Comprueba que la consulta de estados devuelve 'BAJA' para usuarios con estado = FALSE
 */
public class UsuarioEstadoTest {

    @Test
    public void usuarioBajaPresente() {
        Controlador c = new Controlador();
        UsuarioDAO dao = new UsuarioDAO(c.getDbConnection());
        String[][] filas = dao.listaUsuariosYEstadoSancionActiva();
        boolean foundBaja = false;
        for (String[] f : filas) {
            if (f != null && f.length > 0) {
                String id = f[0];
                String estado = f[3]; // BEFORE change it was 3? careful: DAO returns id,dni,nombre,sancion_activa,tipo
                // In listaUsuariosYEstadoSancionActiva we set fila[3] = sancion_activa
                // We check for id == "11" and estado == "BAJA"
                if ("11".equals(id) && "BAJA".equalsIgnoreCase(estado)) {
                    foundBaja = true;
                    break;
                }
            }
        }
        assertTrue("Debe existir un usuario con id=11 en estado BAJA (según inicializacion.sql)", foundBaja);
    }
}
