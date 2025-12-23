package com.example;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import com.example.dao.PublicacionDAO;
import com.example.controlador.Controlador;
import com.example.controlador.ControladorEliminarPublicacion;
import org.junit.Test;

/**
 * Test para verificar eliminación (baja) de una publicación sin préstamos activos.
 * Requiere DB inicializada (docker compose up -d).
 */
public class EliminarPublicacionTest {

    @Test
    public void eliminarPublicacionSinPrestamos() {
        Controlador c = new Controlador();
        ControladorEliminarPublicacion ced = c.getControladorEliminarPublicacion();
        int id = 12; // publicaion 12 no tiene prestamos activos en inicializacion.sql
        boolean ok = ced.eliminarPublicacion(id);
        // Debe devolver true
        org.junit.Assert.assertTrue(ok);
        // Después, obtenerResumenPublicacionPorId debe devolver null (porque p.estado = FALSE)
        PublicacionDAO dao = new PublicacionDAO();
        String[] resumen = dao.obtenerResumenPublicacionPorId(id);
        assertNull(resumen);
    }

    @Test
    public void eliminarPublicacionConPrestamoActivaFalla() {
        Controlador c = new Controlador();
        ControladorEliminarPublicacion ced = c.getControladorEliminarPublicacion();
        int id = 1; // publicacion 1 tiene prestamos activos en inicializacion.sql
        boolean ok = ced.eliminarPublicacion(id);
        // Debe devolver false porque existen prestamos activos
        assertFalse(ok);
    }
}
