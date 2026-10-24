package com.biblioteca.modelo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class LibroTest {

    @Test
    public void testConstructorValid() {
        List<Modulo> modulos = new ArrayList<>();
        List<Ciclo> ciclos = new ArrayList<>();
        List<Tema> temas = new ArrayList<>();
        List<Autor> autores = new ArrayList<>();

        Libro libro = new Libro(
            "Título del Libro",
            "Editorial Test",
            "123-456-789",
            "es",
            TipoPublicacion.L,
            true,
            modulos,
            ciclos,
            temas,
            1,
            LocalDate.now(),
            autores
        );

        assertNotNull(libro);
        assertEquals("Título del Libro", libro.getTitulo());
        assertEquals("Editorial Test", libro.getEditorial());
        assertEquals("123-456-789", libro.getCodigoISBN());
        assertEquals(TipoPublicacion.L, libro.getTipo());
        assertTrue(libro.isEstado());
    }

    @Test
    public void testConstructorWithId() {
        List<Modulo> modulos = new ArrayList<>();
        List<Ciclo> ciclos = new ArrayList<>();
        List<Tema> temas = new ArrayList<>();
        List<Autor> autores = new ArrayList<>();

        Libro libro = new Libro(
            1,
            "Título del Libro",
            "Editorial Test",
            "123-456-789",
            "es",
            TipoPublicacion.L,
            true,
            modulos,
            ciclos,
            temas,
            1,
            LocalDate.now(),
            autores
        );

        assertEquals(1, libro.getId());
        assertEquals("Título del Libro", libro.getTitulo());
    }

    @Test
    public void testGettersSetters() {
        Libro libro = new Libro(
            "Título",
            "Editorial",
            "123",
            "es",
            TipoPublicacion.L,
            true,
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>(),
            1,
            LocalDate.now(),
            new ArrayList<>()
        );

        libro.setNumEdicion(5);
        assertEquals(5, libro.getNumEdicion());

        libro.setFechaPublicacion(LocalDate.of(2020, 1, 1));
        assertEquals(LocalDate.of(2020, 1, 1), libro.getFechaPublicacion());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumEdicionInvalid() {
        Libro libro = new Libro(
            "Título",
            "Editorial",
            "123",
            "es",
            TipoPublicacion.L,
            true,
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>(),
            1,
            LocalDate.now(),
            new ArrayList<>()
        );

        libro.setNumEdicion(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFechaPublicacionNull() {
        Libro libro = new Libro(
            "Título",
            "Editorial",
            "123",
            "es",
            TipoPublicacion.L,
            true,
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>(),
            1,
            null,
            new ArrayList<>()
        );

        libro.setFechaPublicacion(null);
    }
}
