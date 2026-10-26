package com.biblioteca.modelo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class CicloTest {

    @Test
    public void testConstructorValid() {
        Ciclo ciclo = new Ciclo("2024");

        assertNotNull(ciclo);
        assertEquals("2024", ciclo.getNombre());
    }

    @Test
    public void testConstructorWithId() {
        Ciclo ciclo = new Ciclo(1, "2025");

        assertNotNull(ciclo);
        assertEquals(1, ciclo.getId());
        assertEquals("2025", ciclo.getNombre());
    }

    @Test
    public void testGettersSetters() {
        Ciclo ciclo = new Ciclo("2023");

        ciclo.setNombre("2024");
        assertEquals("2024", ciclo.getNombre());
    }
}
