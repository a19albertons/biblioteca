package com.biblioteca.modelo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class TipoUsuarioTest {

    @Test
    public void testEstudiante() {
        assertNotNull(TipoUsuario.E);
        assertEquals("Estudiante", TipoUsuario.E.getDescripcion());
    }

    @Test
    public void testProfesor() {
        assertNotNull(TipoUsuario.P);
        assertEquals("Profesor", TipoUsuario.P.getDescripcion());
    }

    @Test
    public void testAdministrativo() {
        assertNotNull(TipoUsuario.A);
        assertEquals("Administrativo", TipoUsuario.A.getDescripcion());
    }

    @Test
    public void testConserje() {
        assertNotNull(TipoUsuario.C);
        assertEquals("Conserje", TipoUsuario.C.getDescripcion());
    }

    @Test
    public void testLimpiador() {
        assertNotNull(TipoUsuario.L);
        assertEquals("Limpiador", TipoUsuario.L.getDescripcion());
    }
}
