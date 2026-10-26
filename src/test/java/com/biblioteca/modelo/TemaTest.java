package com.biblioteca.modelo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class TemaTest {

    @Test
    public void testConstructorValid() {
        Tema tema = new Tema("Programación");

        assertNotNull(tema);
        assertEquals("Programación", tema.getNombre());
    }

    @Test
    public void testConstructorWithId() {
        Tema tema = new Tema(1, "Desarrollo Web");

        assertNotNull(tema);
        assertEquals(1, tema.getId());
        assertEquals("Desarrollo Web", tema.getNombre());
    }

    @Test
    public void testGettersSetters() {
        Tema tema = new Tema("Base de Datos");

        tema.setNombre("Programación Orientada a Objetos");
        assertEquals("Programación Orientada a Objetos", tema.getNombre());
    }
}
