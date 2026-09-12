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
import com.biblioteca.dto.EstadoEjemplarDTO;

/**
 * Diálogo de confirmación para eliminar (marcar como baja) un ejemplar.
 * Comprueba que no haya préstamos activos antes de marcar la baja.
 */
public class EliminarEjemplarDialog extends OverlayDialog {
    /**
     * Controlador de la aplicación
     */
    private final Controlador controlador;
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
        super(parent, "Eliminar Ejemplar");
        this.controlador = controlador;
        this.idEjemplar = idEjemplar;
        initUI();
        setSize(new Dimension(420, 160));
        setLocationRelativeTo(parent);
    }

    /**
     * Inicializa la interfaz del diálogo
     */
    private void initUI() {
        // Layout y contenido
        getContentPane().setLayout(new BorderLayout());
        JPanel contenido = new JPanel(new BorderLayout());
        contenido.setBackground(Color.white);
        contenido.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Título y mensaje
        JLabel titulo = new JLabel("Eliminar Ejemplar");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 16f));
        contenido.add(titulo, BorderLayout.NORTH);

        // Mensaje de confirmación
        JLabel texto = new JLabel("Seguro que quieres eliminar el ejemplar?");
        texto.setBorder(BorderFactory.createEmptyBorder(12, 6, 12, 6));
        contenido.add(texto, BorderLayout.CENTER);

        // Botones
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.setBackground(Color.white);

        // Botones Cancelar y Eliminar
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
            // Antes de eliminar, obtener id_publicacion para refrescar la vista tras la
            // operación
            EstadoEjemplarDTO detalles = controlador.getControladorEjemplares().obtenerDetallesEjemplar(idEjemplar);
            int idPublicacion = -1;
            // extraer id_publicacion de los detalles obtenidos
            if (detalles != null) {
                idPublicacion = detalles.getIdPublicacion();
            }

            // Intentar eliminar el ejemplar
            boolean ok = controlador.getControladorEliminarEjemplarDialog().eliminarEjemplar(idEjemplar);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Ejemplar marcado como baja", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                // refrescar lista de ejemplares para la publicación asociada (si se pudo
                // obtener)
                if (idPublicacion > 0) {
                    controlador.getControladorNavegacion().mostrarEjemplaresParaPublicacion(idPublicacion);
                } else {
                    controlador.getControladorNavegacion().refrescarPanelControl();
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No es posible eliminar el ejemplar: existen préstamos activos o error",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        botones.add(cancelar);
        botones.add(eliminar);

        getContentPane().add(contenido, BorderLayout.CENTER);
        getContentPane().add(botones, BorderLayout.SOUTH);
    }
}
