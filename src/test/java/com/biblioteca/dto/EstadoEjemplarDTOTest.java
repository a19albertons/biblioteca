package com.biblioteca.dto;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class EstadoEjemplarDTOTest {

    @Test
    public void testConstructorValid() {
        EstadoEjemplarDTO dto = new EstadoEjemplarDTO(1, 100, 1, "2024-01-01", "DISPONIBLE");

        assertNotNull(dto);
        assertEquals(1, dto.getId());
        assertEquals(100, dto.getIdPublicacion());
        assertEquals(1, dto.getNumEjemplar());
        assertNotNull(dto.getFechaAdquisicion());
        assertNotNull(dto.getEstado());
        assertEquals("2024-01-01", dto.getFechaAdquisicion());
        assertEquals("DISPONIBLE", dto.getEstado());
    }

    @Test
    public void testGetters() {
        EstadoEjemplarDTO dto = new EstadoEjemplarDTO(2, 200, 2, "2024-02-01", "BAJA");

        assertEquals(2, dto.getId());
        assertEquals(200, dto.getIdPublicacion());
        assertEquals(2, dto.getNumEjemplar());
        assertEquals("2024-02-01", dto.getFechaAdquisicion());
        assertEquals("BAJA", dto.getEstado());
    }

    @Test
    public void testMultipleInstances() {
        EstadoEjemplarDTO dto1 = new EstadoEjemplarDTO(1, 100, 1, "2024-01-01", "DISPONIBLE");
        EstadoEjemplarDTO dto2 = new EstadoEjemplarDTO(2, 200, 2, "2024-02-01", "BAJA");

        assertNotNull(dto1);
        assertNotNull(dto2);
        assertEquals(1, dto1.getId());
        assertEquals(2, dto2.getId());
    }
}
