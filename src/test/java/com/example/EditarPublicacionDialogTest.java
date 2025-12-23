package com.example;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.example.controlador.Controlador;
import com.example.controlador.ControladorEditarPublicacionDialog;
import org.junit.Test;

/**
 * Test rápido para comprobar que el DAO retorna detalles de una publicación
 * existente (requiere DB levantada, usa datos de inicializacion.sql)
 */
public class EditarPublicacionDialogTest {

    @Test
    public void obtenerDetallesDevuelveAlgo() {
        Controlador c = new Controlador();
        ControladorEditarPublicacionDialog ced = c.getControladorEditarPublicacionDialog();
        String[] detalles = ced.obtenerDetallesPublicacion(1); // id 1 existe en inicializacion.sql
        assertNotNull("Detalles no debe ser null", detalles);
        assertTrue("Debe contener al menos 10 campos", detalles.length >= 10);
    }
}
