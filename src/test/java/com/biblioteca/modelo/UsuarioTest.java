package com.biblioteca.modelo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UsuarioTest {

    @Test
    public void testConstructorValid() {
        Usuario usuario = new Usuario(
            "12345678J",
            "Juan Pérez",
            "Pérez",
            "López",
            null,
            "password123",
            TipoUsuario.E,
            true
        );

        assertNotNull(usuario);
        assertEquals("12345678J", usuario.getDni());
        assertEquals("Juan Pérez", usuario.getNombre());
        assertEquals("Pérez", usuario.getApellido1());
        assertEquals("López", usuario.getApellido2());
        assertEquals("A26Juan PérezPL", usuario.getUsuario());
        assertNull(usuario.getEmail());
        assertEquals(TipoUsuario.E, usuario.getTipo());
        assertTrue(usuario.getEstado());
    }

    @Test
    public void testGettersSetters() {
        Usuario usuario = new Usuario(
            "12345678J",
            "Juan Pérez",
            "Pérez",
            "López",
            "juanperez24",
            "password123",
            TipoUsuario.E,
            true
        );

        usuario.setNombre("Ana García");
        assertEquals("Ana García", usuario.getNombre());

        usuario.setApellido1("García");
        assertEquals("García", usuario.getApellido1());

        usuario.setApellido2("Martínez");
        assertEquals("Martínez", usuario.getApellido2());
    }

    @Test
    public void testSetTipo() {
        Usuario usuario = new Usuario(
            "12345678J",
            "Juan Pérez",
            "Pérez",
            "López",
            "juanperez24",
            "password123",
            TipoUsuario.E,
            true
        );

        usuario.setTipo(TipoUsuario.P);
        assertEquals(TipoUsuario.P, usuario.getTipo());
    }

    @Test
    public void testSetEstado() {
        Usuario usuario = new Usuario(
            "12345678J",
            "Juan Pérez",
            "Pérez",
            "López",
            "juanperez24",
            "password123",
            TipoUsuario.E,
            true
        );

        usuario.setEstado(false);
        assertFalse(usuario.getEstado());

        usuario.setEstado(true);
        assertTrue(usuario.getEstado());
    }
}
