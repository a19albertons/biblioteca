package com.example;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.example.dao.EjemplarDAO;
import com.example.controlador.Controlador;
import org.junit.Test;

/**
 * Tests para la baja (eliminación lógica) de ejemplares.
 */
public class EliminarEjemplarTest {

    @Test
    public void eliminarEjemplarConPrestamoActivoFalla() {
        Controlador c = new Controlador();
        int idEjemplar = 1; // tiene préstamo activo según inicializacion.sql
        boolean ok = c.getControladorEliminarEjemplarDialog().eliminarEjemplar(idEjemplar);
        // Debe fallar porque existe préstamo activo
        assertFalse(ok);
        // Estado debe seguir siendo DISPONIBLE
        EjemplarDAO dao = new EjemplarDAO();
        String[] detalles = dao.obtenerEjemplarPorId(idEjemplar);
        assertTrue(detalles != null && "DISPONIBLE".equals(detalles[4]));
    }

    @Test
    public void eliminarEjemplarSinPrestamosDaBaja() {
        Controlador c = new Controlador();
        EjemplarDAO dao = new EjemplarDAO();
        int idEjemplar = 12; // en inicializacion.sql no tiene prestamos activos

        // Asegurar estado inicial
        String[] antes = dao.obtenerEjemplarPorId(idEjemplar);
        assertTrue(antes != null);

        boolean ok = c.getControladorEliminarEjemplarDialog().eliminarEjemplar(idEjemplar);
        assertTrue(ok);

        String[] despues = dao.obtenerEjemplarPorId(idEjemplar);
        assertTrue(despues != null && "BAJA".equals(despues[4]));

        // Restaurar estado original (poner en servicio de nuevo) para no dejar side-effects
        java.sql.Connection conexion = new com.example.conexiones.MySQLConnection().getConnection();
        try {
            if (conexion != null) {
                conexion.setAutoCommit(false);
                boolean restored = dao.actualizarEjemplar(conexion, idEjemplar, java.sql.Date.valueOf(antes[3]), true);
                if (restored) conexion.commit();
                else conexion.rollback();
            }
        } catch (Exception e) {
            // ignore in test
        } finally {
            try { if (conexion != null) conexion.close(); } catch (Exception ex) {}
        }
    }
}
