package com.biblioteca.modelo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class PublicacionTest {

    @Test
    public void testConstructorValid() {
        List<Modulo> modulos = new ArrayList<>();
        List<Ciclo> ciclos = new ArrayList<>();
        List<Tema> temas = new ArrayList<>();

        Publicacion publicacion = new Publicacion(
            "Título de la Publicación",
            "Editorial Test",
            "987-654-321",
            "es",
            TipoPublicacion.L,
            true,
            modulos,
            ciclos,
            temas
        );

        assertNotNull(publicacion);
        assertEquals("Título de la Publicación", publicacion.getTitulo());
        assertEquals("Editorial Test", publicacion.getEditorial());
        assertEquals("987-654-321", publicacion.getCodigoISBN());
        assertEquals("es", publicacion.getIdioma());
        assertEquals(TipoPublicacion.L, publicacion.getTipo());
        assertTrue(publicacion.isEstado());
    }

    @Test
    public void testConstructorWithId() {
        List<Modulo> modulos = new ArrayList<>();
        List<Ciclo> ciclos = new ArrayList<>();
        List<Tema> temas = new ArrayList<>();

        Publicacion publicacion = new Publicacion(
            1,
            "Título de la Publicación",
            "Editorial Test",
            "987-654-321",
            "es",
            TipoPublicacion.L,
            true,
            modulos,
            ciclos,
            temas
        );

        assertEquals(1, publicacion.getId());
        assertEquals("Título de la Publicación", publicacion.getTitulo());
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

        publicacion.setTitulo("Nuevo Título");
        assertEquals("Nuevo Título", publicacion.getTitulo());

        publicacion.setEditorial("Nueva Editorial");
        assertEquals("Nueva Editorial", publicacion.getEditorial());
    }

    @Test
    public void testSetTipo() {
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

        publicacion.setTipo(TipoPublicacion.R);
        assertEquals(TipoPublicacion.R, publicacion.getTipo());
    }

    @Test
    public void testSetEstado() {
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

        publicacion.setEstado(false);
        assertFalse(publicacion.isEstado());

        publicacion.setEstado(true);
        assertTrue(publicacion.isEstado());
    }
}
