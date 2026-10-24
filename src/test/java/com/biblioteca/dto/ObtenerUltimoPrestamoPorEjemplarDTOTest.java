package com.biblioteca.dto;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ObtenerUltimoPrestamoPorEjemplarDTOTest {

    @Test
    public void testConstructorValid() {
        ObtenerUltimoPrestamoPorEjemplarDTO dto = new ObtenerUltimoPrestamoPorEjemplarDTO(
                1,
                100,
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                true
        );

        assertNotNull(dto);
        assertEquals(1, dto.getId());
        assertEquals(100, dto.getIdUsuario());
        assertNotNull(dto.getFechaInicio());
        assertNotNull(dto.getFechaFin());
        assertTrue(dto.isEstado());
    }

    @Test
    public void testGetters() {
        ObtenerUltimoPrestamoPorEjemplarDTO dto = new ObtenerUltimoPrestamoPorEjemplarDTO(
                42,
                200,
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 2, 1),
                false
        );

        assertEquals(42, dto.getId());
        assertEquals(200, dto.getIdUsuario());
        assertEquals(LocalDate.of(2024, 1, 1), dto.getFechaInicio());
        assertEquals(LocalDate.of(2024, 2, 1), dto.getFechaFin());
        assertFalse(dto.isEstado());
    }

    @Test
    public void testMultipleInstances() {
        ObtenerUltimoPrestamoPorEjemplarDTO dto1 = new ObtenerUltimoPrestamoPorEjemplarDTO(1, 1, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 2), true);
        ObtenerUltimoPrestamoPorEjemplarDTO dto2 = new ObtenerUltimoPrestamoPorEjemplarDTO(2, 2, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 2), false);

        assertNotNull(dto1);
        assertNotNull(dto2);
        assertEquals(1, dto1.getId());
        assertEquals(2, dto2.getId());
    }
}
