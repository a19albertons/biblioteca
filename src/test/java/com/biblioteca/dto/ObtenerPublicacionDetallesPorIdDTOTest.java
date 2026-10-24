package com.biblioteca.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.biblioteca.modelo.TipoPublicacion;

public class ObtenerPublicacionDetallesPorIdDTOTest {

    @Test
    public void testConstructorValid() {
        ObtenerPublicacionDetallesPorIdDTO dto = new ObtenerPublicacionDetallesPorIdDTO(
                TipoPublicacion.L,
                "Título del Libro",
                "978-0123456789",
                "ES",
                "Historia",
                "Capítulo 1",
                "Primero",
                "Editorial Test",
                "1",
                "2024",
                "Juan Pérez",
                "Mensual",
                1,
                true
        );

        assertNotNull(dto);
        assertEquals(com.biblioteca.modelo.TipoPublicacion.L, dto.getTipoPublicacion());
        assertEquals("Título del Libro", dto.getTitulo());
        assertEquals("978-0123456789", dto.getCodigoISBN());
        assertEquals("ES", dto.getIdioma());
        assertEquals("Historia", dto.getTemas());
        assertEquals("Capítulo 1", dto.getModulos());
        assertEquals("Primero", dto.getCiclos());
        assertEquals("Editorial Test", dto.getEditorial());
        assertEquals("1", dto.getNumEdicion());
        assertEquals("2024", dto.getFechaPublicacion());
        assertEquals("Juan Pérez", dto.getAutores());
        assertEquals("Mensual", dto.getPeriodicidad());
        assertEquals(1, dto.getIdpublicacion());
        assertTrue(dto.getEstado());
    }

    @Test
    public void testGetters() {
        ObtenerPublicacionDetallesPorIdDTO dto = new ObtenerPublicacionDetallesPorIdDTO(
                TipoPublicacion.R,
                "Revista",
                "978-1122334455",
                "EN",
                "Ciencia",
                "Artículo",
                "Segundo",
                "Editorial 2",
                "2",
                "2025",
                "Maria López",
                "Trimestral",
                2,
                false
        );

        assertEquals(TipoPublicacion.R, dto.getTipoPublicacion());
        assertEquals("Revista", dto.getTitulo());
        assertEquals("978-1122334455", dto.getCodigoISBN());
        assertEquals("EN", dto.getIdioma());
        assertEquals("Ciencia", dto.getTemas());
        assertEquals("Artículo", dto.getModulos());
        assertEquals("Segundo", dto.getCiclos());
        assertEquals("Editorial 2", dto.getEditorial());
        assertEquals("2", dto.getNumEdicion());
        assertEquals("2025", dto.getFechaPublicacion());
        assertEquals("Maria López", dto.getAutores());
        assertEquals("Trimestral", dto.getPeriodicidad());
        assertEquals(2, dto.getIdpublicacion());
        assertFalse(dto.getEstado());
    }

    @Test
    public void testMultipleInstances() {
        ObtenerPublicacionDetallesPorIdDTO dto1 = new ObtenerPublicacionDetallesPorIdDTO(
                TipoPublicacion.L, "Libro 1", "978-0000000001", "ES", "Test", "1", "1", "Editorial", "1", "2024", "Author1", "Anual", 1, true);
        ObtenerPublicacionDetallesPorIdDTO dto2 = new ObtenerPublicacionDetallesPorIdDTO(
                TipoPublicacion.R, "Revista 1", "978-0000000002", "ES", "Test", "1", "1", "Editorial", "1", "2024", "Author2", "Mensual", 2, false);

        assertNotNull(dto1);
        assertNotNull(dto2);
        assertEquals(1, dto1.getIdpublicacion());
        assertEquals(2, dto2.getIdpublicacion());
    }
}
