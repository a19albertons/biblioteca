package com.biblioteca.dto;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UsuarioFinSancionDTOTest {

    @Test
    public void testConstructorValid() {
        UsuarioFinSancionDTO dto = new UsuarioFinSancionDTO(1, "2024-01-01");

        assertNotNull(dto);
        assertEquals(1, dto.getIdSancion());
        assertEquals("2024-01-01", dto.getFinSancion());
    }

    @Test
    public void testGetters() {
        UsuarioFinSancionDTO dto = new UsuarioFinSancionDTO(100, "2025-12-31");

        assertEquals(100, dto.getIdSancion());
        assertEquals("2025-12-31", dto.getFinSancion());
    }

    @Test
    public void testMultipleInstances() {
        UsuarioFinSancionDTO dto1 = new UsuarioFinSancionDTO(1, "2024-01-01");
        UsuarioFinSancionDTO dto2 = new UsuarioFinSancionDTO(2, "2025-01-01");

        assertNotNull(dto1);
        assertNotNull(dto2);
        assertEquals(1, dto1.getIdSancion());
        assertEquals(2, dto2.getIdSancion());
    }
}
