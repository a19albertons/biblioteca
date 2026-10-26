package com.biblioteca.modelo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Test;

public class SancionTest {

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

        Sancion sancion = new Sancion(
            1,
            usuario,
            null,
            LocalDate.of(2024, 1, 1),
            LocalDate.of(2024, 6, 1),
            "Por retraso en entrega",
            true
        );

        assertNotNull(sancion);
        assertEquals(1, sancion.getId());
        assertEquals(usuario, sancion.getUsuario());
        assertNull(sancion.getPrestamo());
        assertEquals(LocalDate.of(2024, 1, 1), sancion.getInicioSancion());
        assertEquals(LocalDate.of(2024, 6, 1), sancion.getFinSancion());
        assertEquals("Por retraso en entrega", sancion.getDescripcion());
        assertTrue(sancion.isEstado());
    }

    @Test
    public void testSetFinSancionBeforeInicio() {
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

        Sancion sancion = new Sancion(
            1,
            usuario,
            null,
            LocalDate.of(2024, 1, 1),
            LocalDate.of(2024, 6, 1),
            "Por retraso en entrega",
            true
        );

        assertNotNull(sancion);
        assertEquals(LocalDate.of(2024, 1, 1), sancion.getInicioSancion());
        assertEquals(LocalDate.of(2024, 6, 1), sancion.getFinSancion());
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

        Sancion sancion = new Sancion(
            1,
            usuario,
            null,
            LocalDate.of(2024, 1, 1),
            LocalDate.of(2024, 6, 1),
            "Por retraso en entrega",
            true
        );

        sancion.setDescripcion("Nueva descripción");
        assertEquals("Nueva descripción", sancion.getDescripcion());

        sancion.setEstado(false);
        assertFalse(sancion.isEstado());

        sancion.setEstado(true);
        assertTrue(sancion.isEstado());
    }
}
