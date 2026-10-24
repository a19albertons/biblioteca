package com.biblioteca.modelo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class TipoPublicacionTest {

    @Test
    public void testRevistaConstructor() {
        TipoPublicacion tipo = TipoPublicacion.R;

        assertNotNull(tipo);
        assertEquals(TipoPublicacion.R, tipo);
    }

    @Test
    public void testLibroConstructor() {
        TipoPublicacion tipo = TipoPublicacion.L;

        assertNotNull(tipo);
        assertEquals(TipoPublicacion.L, tipo);
    }
}
