package com.biblioteca.modelo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ModuloTest {

    @Test
    public void testConstructorValid() {
        Modulo modulo = new Modulo("Matemáticas");

        assertNotNull(modulo);
        assertEquals("Matemáticas", modulo.getNombre());
    }

    @Test
    public void testConstructorWithId() {
        Modulo modulo = new Modulo(1, "Ciencias");

        assertNotNull(modulo);
        assertEquals(1, modulo.getId());
        assertEquals("Ciencias", modulo.getNombre());
    }

    @Test
    public void testGettersSetters() {
        Modulo modulo = new Modulo("Historia");

        modulo.setNombre("Historia Universal");
        assertEquals("Historia Universal", modulo.getNombre());
    }
}
