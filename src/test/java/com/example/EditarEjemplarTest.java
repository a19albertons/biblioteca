package com.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;

import com.example.controlador.Controlador;
import com.example.dao.EjemplarDAO;
import org.junit.Test;

/**
 * Test para editar un ejemplar (cambiar fecha de adquisición)
 */
public class EditarEjemplarTest {

    @Test
    public void editarFechaEjemplarYRestaurar() {
        Controlador c = new Controlador();
        EjemplarDAO dao = new EjemplarDAO(c.getDbConnection());
        int idEjemplar = 12; // presente en inicializacion.sql

        String[] antes = dao.obtenerEjemplarPorId(idEjemplar);
        assertNotNull(antes);
        String fechaAntes = antes[3];

        LocalDate nuevaFecha = LocalDate.of(2025, 1, 1);
        boolean ok = c.getControladorEditarEjemplarDialog().editarEjemplar(idEjemplar, nuevaFecha);
        org.junit.Assert.assertTrue(ok);

        String[] despues = dao.obtenerEjemplarPorId(idEjemplar);
        assertNotNull(despues);
        assertEquals("2025-01-01", despues[3]);

        // Restaurar fecha original para no dejar side-effects
        LocalDate fechaOriginal = fechaAntes == null || fechaAntes.isEmpty() ? LocalDate.now() : LocalDate.parse(fechaAntes);
        boolean ok2 = c.getControladorEditarEjemplarDialog().editarEjemplar(idEjemplar, fechaOriginal);
        org.junit.Assert.assertTrue(ok2);
    }
}
