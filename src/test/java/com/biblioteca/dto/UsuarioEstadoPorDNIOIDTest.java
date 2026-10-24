package com.biblioteca.dto;

import org.junit.Test;

import com.biblioteca.modelo.TipoUsuario;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UsuarioEstadoPorDNIOIDTest {

    @Test
    public void testConstructorValid() {
        UsuarioEstadoPorDNIOID dto = new UsuarioEstadoPorDNIOID(
                1,
                "12345678J",
                "Juan Pérez",
                "ACTIVO",
                TipoUsuario.E
        );

        assertNotNull(dto);
        assertEquals(1, dto.getId());
        assertEquals("12345678J", dto.getDni());
        assertEquals("Juan Pérez", dto.getNombreCompleto());
        assertEquals("ACTIVO", dto.getSancionactiva());
        assertNotNull(dto.getTipoUsuario());
        assertEquals(TipoUsuario.E, dto.getTipoUsuario());
    }

    @Test
    public void testGetters() {
        UsuarioEstadoPorDNIOID dto = new UsuarioEstadoPorDNIOID(
                100,
                "98765432K",
                "Ana García",
                "SANCIONADO",
                TipoUsuario.P
        );

        assertEquals(100, dto.getId());
        assertEquals("98765432K", dto.getDni());
        assertEquals("Ana García", dto.getNombreCompleto());
        assertEquals("SANCIONADO", dto.getSancionactiva());
        assertNotNull(dto.getTipoUsuario());
        assertEquals(TipoUsuario.P, dto.getTipoUsuario());
    }

    @Test
    public void testMultipleInstances() {
        UsuarioEstadoPorDNIOID dto1 = new UsuarioEstadoPorDNIOID(1, "12345678J", "Juan", "ACTIVO", com.biblioteca.modelo.TipoUsuario.E);
        UsuarioEstadoPorDNIOID dto2 = new UsuarioEstadoPorDNIOID(2, "98765432K", "Ana", "SANCIONADO", com.biblioteca.modelo.TipoUsuario.P);

        assertNotNull(dto1);
        assertNotNull(dto2);
        assertEquals(1, dto1.getId());
        assertEquals(2, dto2.getId());
    }
}
