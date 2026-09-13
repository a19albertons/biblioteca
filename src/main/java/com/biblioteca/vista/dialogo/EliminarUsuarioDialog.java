package com.biblioteca.vista.dialogo;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.biblioteca.controlador.Controlador;

/**
 * Diálogo para confirmar la eliminación (desactivación) de un usuario
 */
public class EliminarUsuarioDialog extends BaseEliminarDialog {
    /**
     * ID del usuario a eliminar
     */
    private final int idUsuario;

    /**
     * Constructor
     * 
     * @param parent
     * @param controlador
     * @param idUsuario
     */
    public EliminarUsuarioDialog(final JFrame parent, final Controlador controlador, final int idUsuario) {
        super(parent, controlador, idUsuario, "Eliminar Usuario", "Seguro que quieres eliminar el usuario?", "USUARIO");
        this.idUsuario = idUsuario;
        super.initUI();
        setSize(new Dimension(360, 140));
        setLocationRelativeTo(parent);
    }

    /**
     * Lógica de eliminación específica para usuario
     * 
     * @return true si la eliminación fue exitosa
     */
    @Override
    protected boolean onEliminar() {
        // Intentar eliminar usuario
        boolean ok = controlador.getControladorEliminarUsuario().eliminarUsuario(idUsuario);
        return ok;
    }

    /**
     * Manejo de salida (refrescos) específico para usuario
     * 
     * @param exito true si la eliminación fue exitosa
     */
    @Override
    protected void onExit(boolean exito) {
        if (exito) {
            JOptionPane.showMessageDialog(this, "Usuario desactivado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            controlador.getControladorNavegacion().refrescarUsuarios();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No es posible eliminar el usuario: existen préstamos activos o ocurrió un error", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
