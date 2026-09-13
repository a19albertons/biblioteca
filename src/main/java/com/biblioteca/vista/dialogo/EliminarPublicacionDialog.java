package com.biblioteca.vista.dialogo;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.biblioteca.controlador.Controlador;

/**
 * Diálogo de confirmación para eliminar (dar de baja) una publicación.
 * Comprueba que no haya préstamos activos antes de borrar y muestra mensajes
 * al usuario.
 */
public class EliminarPublicacionDialog extends BaseEliminarDialog {
    /**
     * ID de la publicación a eliminar
     */
    private final int idPublicacion;

    /**
     * Constructor del diálogo
     * 
     * @param parent
     * @param controlador
     * @param idPublicacion
     */
    public EliminarPublicacionDialog(final JFrame parent, final Controlador controlador, final int idPublicacion) {
        super(parent, controlador, idPublicacion, "Eliminar Publicación", "Seguro que quieres eliminar la publicación?",
                "PUBLICACION");
        this.idPublicacion = idPublicacion;
        super.initUI();
        setSize(new Dimension(360, 160));
        setLocationRelativeTo(parent);
    }

    /**
     * Lógica de eliminación específica para publicación
     * 
     * @return true si la eliminación fue exitosa
     */
    @Override
    protected boolean onEliminar() {
        // Pedir al controlador que elimine la publicación
        boolean ok = controlador.getControladorEliminarPublicacion().eliminarPublicacion(idPublicacion);
        return ok;
    }

    /**
     * Manejo de salida (refrescos) específico para publicación
     * 
     * @param exito true si la eliminación fue exitosa
     */
    @Override
    protected void onExit(boolean exito) {
        if (exito) {
            JOptionPane.showMessageDialog(this, "Publicación eliminada (marcada como baja)", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            // Refresh views
            controlador.getControladorNavegacion().refrescarPublicaciones();
            controlador.getControladorNavegacion().refrescarPanelControl();
            dispose();
        } else {
                JOptionPane.showMessageDialog(this,
                        "No es posible eliminar la publicación: existen préstamos activos o ocurrió un error",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        
    }
}
