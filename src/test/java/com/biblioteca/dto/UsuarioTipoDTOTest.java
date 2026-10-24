package com.biblioteca.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.biblioteca.modelo.TipoUsuario;

public class UsuarioTipoDTOTest {

    @Test
    public void testConstructorValid() {
        UsuarioTipoDTO dto = new UsuarioTipoDTO(1, com.biblioteca.modelo.TipoUsuario.E);

        assertNotNull(dto);
        assertEquals(1, dto.getIdUsuario());
        assertNotNull(dto.getTipoUsuario());
        assertEquals(TipoUsuario.E, dto.getTipoUsuario());
    }

    @Test
    public void testGetters() {
        UsuarioTipoDTO dto = new UsuarioTipoDTO(123, com.biblioteca.modelo.TipoUsuario.P);

        assertEquals(123, dto.getIdUsuario());
        assertNotNull(dto.getTipoUsuario());
        assertEquals(TipoUsuario.P, dto.getTipoUsuario());
    }

    @Test
    public void testMultipleInstances() {
        UsuarioTipoDTO dto1 = new UsuarioTipoDTO(1, com.biblioteca.modelo.TipoUsuario.E);
        UsuarioTipoDTO dto2 = new UsuarioTipoDTO(2, com.biblioteca.modelo.TipoUsuario.L);

        assertNotNull(dto1);
        assertNotNull(dto2);
        assertEquals(1, dto1.getIdUsuario());
        assertEquals(2, dto2.getIdUsuario());
    }
}
