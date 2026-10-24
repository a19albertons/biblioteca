package com.biblioteca.modelo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class AutorTest {

    @Test
    public void testConstructorValid() {
        Autor autor = new Autor("Juan Pérez", "Español");

        assertNotNull(autor);
        assertEquals("Juan Pérez", autor.getNombre());
        assertEquals("Español", autor.getNacionalidad());
    }

    @Test
    public void testConstructorWithId() {
        Autor autor = new Autor(1, "Ana García", "Mexicana");

        assertNotNull(autor);
        assertEquals(1, autor.getId());
        assertEquals("Ana García", autor.getNombre());
        assertEquals("Mexicana", autor.getNacionalidad());
    }

    @Test
    public void testGettersSetters() {
        Autor autor = new Autor("Juan Pérez", "Español");

        autor.setNombre("Ana García");
        assertEquals("Ana García", autor.getNombre());

        autor.setNacionalidad("Argentina");
        assertEquals("Argentina", autor.getNacionalidad());
    }
}
