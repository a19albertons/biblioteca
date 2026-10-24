package com.biblioteca.dto;

import org.junit.Test;

import com.biblioteca.modelo.TipoPublicacion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class EjemplarTipoPublicacionDTOTest {

    @Test
    public void testConstructorValid() {
        EjemplarTipoPublicacionDTO dto = new EjemplarTipoPublicacionDTO(1, com.biblioteca.modelo.TipoPublicacion.L);

        assertNotNull(dto);
        assertEquals(1, dto.getNumEjemplar());
        assertNotNull(dto.getTipoPublicacion());
        assertEquals(TipoPublicacion.L, dto.getTipoPublicacion());
    }

    @Test
    public void testGetters() {
        EjemplarTipoPublicacionDTO dto = new EjemplarTipoPublicacionDTO(2, com.biblioteca.modelo.TipoPublicacion.R);

        assertEquals(2, dto.getNumEjemplar());
        assertNotNull(dto.getTipoPublicacion());
        assertEquals(TipoPublicacion.R, dto.getTipoPublicacion());
    }

    @Test
    public void testMultipleInstances() {
        EjemplarTipoPublicacionDTO dto1 = new EjemplarTipoPublicacionDTO(1, com.biblioteca.modelo.TipoPublicacion.L);
        EjemplarTipoPublicacionDTO dto2 = new EjemplarTipoPublicacionDTO(2, com.biblioteca.modelo.TipoPublicacion.R);

        assertNotNull(dto1);
        assertNotNull(dto2);
        assertEquals(1, dto1.getNumEjemplar());
        assertEquals(2, dto2.getNumEjemplar());
    }
}
