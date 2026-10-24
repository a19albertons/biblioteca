package com.biblioteca.dto;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RegistroDevolucionDTOTest {

    @Test
    public void testConstructorValid() {
        RegistroDevolucionDTO dto = new RegistroDevolucionDTO(1, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 15));

        assertNotNull(dto);
        assertEquals(1, dto.getIdPrestamo());
        assertNotNull(dto.getFechaInicio());
        assertNotNull(dto.getFechaFin());
        assertEquals(LocalDate.of(2024, 1, 1), dto.getFechaInicio());
        assertEquals(LocalDate.of(2024, 1, 15), dto.getFechaFin());
    }

    @Test
    public void testGetters() {
        RegistroDevolucionDTO dto = new RegistroDevolucionDTO(100, LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 20));

        assertEquals(100, dto.getIdPrestamo());
        assertEquals(LocalDate.of(2024, 2, 1), dto.getFechaInicio());
        assertEquals(LocalDate.of(2024, 2, 20), dto.getFechaFin());
    }

    @Test
    public void testMultipleInstances() {
        RegistroDevolucionDTO dto1 = new RegistroDevolucionDTO(1, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 15));
        RegistroDevolucionDTO dto2 = new RegistroDevolucionDTO(2, LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 20));

        assertNotNull(dto1);
        assertNotNull(dto2);
        assertEquals(1, dto1.getIdPrestamo());
        assertEquals(2, dto2.getIdPrestamo());
    }
}
