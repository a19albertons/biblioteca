package com.biblioteca.modelo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class RevistaTest {

    @Test
    public void testConstructorValid() {
        List<Modulo> modulos = new ArrayList<>();
        List<Ciclo> ciclos = new ArrayList<>();
        List<Tema> temas = new ArrayList<>();

        Revista revista = new Revista(
            "Revista Test",
            "Editorial Test",
            "123-456-789",
            "es",
            TipoPublicacion.L,
            true,
            modulos,
            ciclos,
            temas,
            "Mensual",
            1
        );

        assertNotNull(revista);
        assertEquals("Revista Test", revista.getTitulo());
        assertEquals("Editorial Test", revista.getEditorial());
        assertEquals("123-456-789", revista.getCodigoISBN());
        assertEquals("es", revista.getIdioma());
        assertEquals(TipoPublicacion.L, revista.getTipo());
        assertTrue(revista.isEstado());
    }

    @Test
    public void testConstructorWithId() {
        List<Modulo> modulos = new ArrayList<>();
        List<Ciclo> ciclos = new ArrayList<>();
        List<Tema> temas = new ArrayList<>();

        Revista revista = new Revista(
            1,
            "Revista Test",
            "Editorial Test",
            "123-456-789",
            "es",
            TipoPublicacion.L,
            true,
            modulos,
            ciclos,
            temas,
            "Mensual",
            1
        );

        assertEquals(1, revista.getId());
        assertEquals("Revista Test", revista.getTitulo());
    }

    @Test
    public void testGettersSetters() {
        List<Modulo> modulos = new ArrayList<>();
        List<Ciclo> ciclos = new ArrayList<>();
        List<Tema> temas = new ArrayList<>();

        Revista revista = new Revista(
            "Título",
            "Editorial",
            "123",
            "es",
            TipoPublicacion.L,
            true,
            modulos,
            ciclos,
            temas,
            "Mensual",
            1
        );

        revista.setNumRevista(2);
        assertEquals(2, revista.getNumRevista());
    }
}
