package com.biblioteca.dto;

import org.junit.Test;

import com.biblioteca.modelo.TipoPublicacion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class EjemplarConTituloDTOTest {

    @Test
    public void testConstructorValid() {
        EjemplarConTituloDTO dto = new EjemplarConTituloDTO(1, 100, 1, "DISPONIBLE", "Título del Libro", 1,
                TipoPublicacion.L);

        assertNotNull(dto);
        assertEquals(1, dto.getIdEjemplar());
        assertEquals(100, dto.getIdPublicacion());
        assertEquals(1, dto.getNumEjemplar());
        assertEquals("DISPONIBLE", dto.getEstado());
        assertEquals("Título del Libro", dto.getTitulo());
        assertEquals(1, dto.getNumEdicion());
        assertNotNull(dto.getTipoPublicacion());
        assertEquals(TipoPublicacion.L, dto.getTipoPublicacion());
    }

    @Test
    public void testGetters() {
        EjemplarConTituloDTO dto = new EjemplarConTituloDTO(2, 200, 2, "BAJA", "Título del Libro 2", 2,
                TipoPublicacion.R);

        assertEquals(2, dto.getIdEjemplar());
        assertEquals(200, dto.getIdPublicacion());
        assertEquals(2, dto.getNumEjemplar());
        assertEquals("BAJA", dto.getEstado());
        assertEquals("Título del Libro 2", dto.getTitulo());
        assertEquals(2, dto.getNumEdicion());
        assertNotNull(dto.getTipoPublicacion());
        assertEquals(TipoPublicacion.R, dto.getTipoPublicacion());
    }

    @Test
    public void testMultipleInstances() {
        EjemplarConTituloDTO dto1 = new EjemplarConTituloDTO(1, 100, 1, "DISPONIBLE", "Título 1", 1,
                TipoPublicacion.L);
        EjemplarConTituloDTO dto2 = new EjemplarConTituloDTO(2, 200, 2, "BAJA", "Título 2", 2,
                TipoPublicacion.R);

        assertNotNull(dto1);
        assertNotNull(dto2);
        assertEquals(1, dto1.getIdEjemplar());
        assertEquals(2, dto2.getIdEjemplar());
    }
}
