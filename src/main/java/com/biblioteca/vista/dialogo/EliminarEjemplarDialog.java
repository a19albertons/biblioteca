package com.biblioteca.vista.dialogo;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.biblioteca.controlador.Controlador;
import com.biblioteca.dto.EstadoEjemplarDTO;

/**
 * Diálogo de confirmación para eliminar (marcar como baja) un ejemplar.
 * Comprueba que no haya préstamos activos antes de marcar la baja.
 */
public class EliminarEjemplarDialog extends BaseEliminarDialog {
    /**
     * ID del ejemplar a eliminar
     */
    private final int idEjemplar;

    /**
     * Constructor del diálogo
     * 
     * @param parent
     * @param controlador
     * @param idEjemplar
     */
    public EliminarEjemplarDialog(final JFrame parent, final Controlador controlador, final int idEjemplar) {
        super(parent, controlador, idEjemplar, "Eliminar Ejemplar", "Seguro que quieres eliminar el ejemplar?",
                "EJEMPLAR");
        this.idEjemplar = idEjemplar;
        super.initUI();
        setSize(new Dimension(420, 160));
        setLocationRelativeTo(parent);
    }

    /**
     * Lógica de eliminación específica para ejemplar
     * 
     * @return true si la eliminación fue exitosa
     */
    @Override
    protected boolean onEliminar() {
        // Antes de eliminar, obtener id_publicacion para refrescar la vista tras la
        // operación
        EstadoEjemplarDTO detalles = getControlador().getControladorEjemplares().obtenerDetallesEjemplar(idEjemplar);
        // extraer id_publicacion de los detalles obtenidos
        if (detalles != null) {
            setIdPublicacion(detalles.getIdPublicacion());
        }

        // Intentar eliminar el ejemplar
        boolean ok = getControlador().getControladorEliminarEjemplarDialog().eliminarEjemplar(idEjemplar);
        return ok;
    }

    /**
     * Manejo de salida (refrescos) específico para ejemplar
     * 
     * @param exito true si la eliminación fue exitosa
     */
    @Override
    protected void onExit(final boolean exito) {
        if (exito) {
            JOptionPane.showMessageDialog(this, "Ejemplar marcado como baja", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            // refrescar lista de ejemplares para la publicación asociada (si se pudo
            // obtener)
            if (getIdPublicacion() > 0) {
                getControlador().getControladorNavegacion().mostrarEjemplaresParaPublicacion(getIdPublicacion());
            } else {
                getControlador().getControladorNavegacion().refrescarPanelControl();
            }
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No es posible eliminar el ejemplar: existen préstamos activos o error",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
