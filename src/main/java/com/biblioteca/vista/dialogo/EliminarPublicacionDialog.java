package com.biblioteca.vista.dialogo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.biblioteca.controlador.Controlador;

/**
 * Diálogo de confirmación para eliminar (dar de baja) una publicación.
 * Comprueba que no haya préstamos activos antes de borrar y muestra mensajes
 * al usuario.
 */
public class EliminarPublicacionDialog extends OverlayDialog {
    /**
     * Controlador de la aplicación
     */
    private final Controlador controlador;
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
        super(parent, "Eliminar Publicación");
        this.controlador = controlador;
        this.idPublicacion = idPublicacion;
        initUI();
        setSize(new Dimension(360, 160));
        setLocationRelativeTo(parent);
    }

    /**
     * Inicializa la interfaz de usuario del diálogo
     */
    private void initUI() {
        // Configuración del diálogo
        getContentPane().setLayout(new BorderLayout());
        JPanel contenido = new JPanel(new BorderLayout());
        contenido.setBackground(Color.white);
        contenido.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Título y mensaje
        JLabel titulo = new JLabel("Eliminar Publicación");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 16f));
        contenido.add(titulo, BorderLayout.NORTH);

        // Mensaje de confirmación
        JLabel texto = new JLabel("Seguro que quieres eliminar la publicación?");
        texto.setBorder(BorderFactory.createEmptyBorder(12, 6, 12, 6));
        contenido.add(texto, BorderLayout.CENTER);

        // Botones
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.setBackground(Color.white);

        // Cancelar y Eliminar
        JButton cancelar = new JButton("Cancelar");
        cancelar.setBackground(Color.white);
        cancelar.setForeground(Color.decode("#000000"));
        cancelar.setBorder(null);
        cancelar.addActionListener(e -> dispose());

        JButton eliminar = new JButton("Eliminar");
        eliminar.setBackground(Color.decode("#E53935"));
        eliminar.setForeground(Color.white);
        eliminar.setBorder(null);
        eliminar.addActionListener(e -> {
            // Pedir al controlador que elimine la publicación
            boolean ok = controlador.getControladorEliminarPublicacion().eliminarPublicacion(idPublicacion);
            if (ok) {
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
        });

        botones.add(cancelar);
        botones.add(eliminar);

        getContentPane().add(contenido, BorderLayout.CENTER);
        getContentPane().add(botones, BorderLayout.SOUTH);
    }
}
