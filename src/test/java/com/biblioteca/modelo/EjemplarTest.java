package com.biblioteca.modelo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.Test;

public class EjemplarTest {

    @Test
    public void testConstructorValid() {
        Publicacion publicacion = new Publicacion(
            "Título",
            "Editorial",
            "123",
            "es",
            TipoPublicacion.L,
            true,
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>()
        );

        Ejemplar ejemplar = new Ejemplar(
            1,
            publicacion,
            1,
            LocalDate.of(2024, 1, 1),
            true
        );

        assertNotNull(ejemplar);
        assertEquals(1, ejemplar.getId());
        assertEquals(publicacion, ejemplar.getPublicacion());
        assertEquals(1, ejemplar.getNumEjemplar());
        assertEquals(LocalDate.of(2024, 1, 1), ejemplar.getFechaAdquisicion());
        assertTrue(ejemplar.isEstado());
    }

    @Test
    public void testSetPublicacion() {
        Publicacion publicacion1 = new Publicacion(
            "Título",
            "Editorial",
            "123",
            "es",
            TipoPublicacion.L,
            true,
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>()
        );

        Publicacion publicacion2 = new Publicacion(
            "Título 2",
            "Editorial",
            "456",
            "es",
            TipoPublicacion.L,
            true,
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>()
        );

        Ejemplar ejemplar = new Ejemplar(
            1,
            publicacion1,
            1,
            LocalDate.of(2024, 1, 1),
            true
        );

        ejemplar.setPublicacion(publicacion2);
        assertEquals(publicacion2, ejemplar.getPublicacion());
    }

    @Test
    public void testGettersSetters() {
        Publicacion publicacion = new Publicacion(
            "Título",
            "Editorial",
            "123",
            "es",
            TipoPublicacion.L,
            true,
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>()
        );

        Ejemplar ejemplar = new Ejemplar(
            1,
            publicacion,
            1,
            LocalDate.of(2024, 1, 1),
            true
        );

        ejemplar.setId(2);
        assertEquals(2, ejemplar.getId());

        ejemplar.setNumEjemplar(2);
        assertEquals(2, ejemplar.getNumEjemplar());

        ejemplar.setFechaAdquisicion(LocalDate.of(2024, 2, 1));
        assertEquals(LocalDate.of(2024, 2, 1), ejemplar.getFechaAdquisicion());

        ejemplar.setEstado(false);
        assertFalse(ejemplar.isEstado());

        ejemplar.setEstado(true);
        assertTrue(ejemplar.isEstado());
    }
}
