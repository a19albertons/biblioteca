package com.biblioteca.modelo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Test;

public class PrestamoTest {

    @Test
    public void testConstructorValid() {
        Usuario usuario = new Usuario(
            "12345678J",
            "Juan Pérez",
            "Pérez",
            "López",
            null,
            "password123",
            TipoUsuario.E,
            true
        );

        Ejemplar ejemplar = new Ejemplar(
            1,
            null,
            1,
            LocalDate.of(2024, 1, 1),
            true
        );

        Prestamo prestamo = new Prestamo(
            usuario,
            ejemplar,
            LocalDate.of(2024, 1, 1),
            LocalDate.of(2024, 6, 1),
            true
        );

        assertNotNull(prestamo);
        // El id es siempre 0 en la creación es la base de datos quien asigna el id final.
        assertEquals(0, prestamo.getId());
        assertEquals(usuario, prestamo.getUsuario());
        assertEquals(ejemplar, prestamo.getEjemplar());
        assertEquals(LocalDate.of(2024, 1, 1), prestamo.getFechaInicio());
        assertEquals(LocalDate.of(2024, 6, 1), prestamo.getFechaFin());
        assertTrue(prestamo.isEstado());
    }

    @Test
    public void testSetFechaFinBeforeInicio() {
        Usuario usuario = new Usuario(
            "12345678J",
            "Juan Pérez",
            "Pérez",
            "López",
            null,
            "password123",
            TipoUsuario.E,
            true
        );

        Ejemplar ejemplar = new Ejemplar(
            1,
            null,
            1,
            LocalDate.of(2024, 1, 1),
            true
        );

        Prestamo prestamo = new Prestamo(
            usuario,
            ejemplar,
            LocalDate.of(2024, 6, 1),
            LocalDate.of(2024, 1, 1),
            true
        );

        assertNotNull(prestamo);
        assertEquals(LocalDate.of(2024, 1, 1), prestamo.getFechaInicio());
        assertEquals(LocalDate.of(2024, 6, 1), prestamo.getFechaFin());
    }

    @Test
    public void testGettersSetters() {
        Usuario usuario = new Usuario(
            "12345678J",
            "Juan Pérez",
            "Pérez",
            "López",
            null,
            "password123",
            TipoUsuario.E,
            true
        );

        Ejemplar ejemplar = new Ejemplar(
            1,
            null,
            1,
            LocalDate.of(2024, 1, 1),
            true
        );

        Prestamo prestamo = new Prestamo(
            usuario,
            ejemplar,
            LocalDate.of(2024, 1, 1),
            LocalDate.of(2024, 6, 1),
            true
        );

        prestamo.setUsuario(usuario);
        assertEquals(usuario, prestamo.getUsuario());

        prestamo.setEjemplar(ejemplar);
        assertEquals(ejemplar, prestamo.getEjemplar());

        prestamo.setEstado(false);
        assertFalse(prestamo.isEstado());

        prestamo.setEstado(true);
        assertTrue(prestamo.isEstado());
    }
}
