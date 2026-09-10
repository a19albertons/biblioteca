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
 * Diálogo para confirmar la eliminación (desactivación) de un usuario
 */
public class EliminarUsuarioDialog extends OverlayDialog {
    /**
     * Controlador principal de la aplicación
     */
    private final Controlador controlador;
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
        super(parent, "Eliminar Usuario");
        this.controlador = controlador;
        this.idUsuario = idUsuario;

        // Inicializar interfaz de usuario
        initUI();
        setSize(new Dimension(360, 140));
        setLocationRelativeTo(parent);
    }

    /**
     * Inicializa la interfaz de usuario
     */
    private void initUI() {
        // Layout principal
        getContentPane().setLayout(new BorderLayout());
        JPanel contenido = new JPanel(new BorderLayout());
        contenido.setBackground(Color.white);
        contenido.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Título y texto
        JLabel titulo = new JLabel("Eliminar Usuario");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 16f));
        contenido.add(titulo, BorderLayout.NORTH);

        // Texto de confirmación
        JLabel texto = new JLabel("Seguro que quieres eliminar el usuario?");
        texto.setBorder(BorderFactory.createEmptyBorder(12, 6, 12, 6));
        contenido.add(texto, BorderLayout.CENTER);

        // Botones
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.setBackground(Color.white);

        // Botón Cancelar
        JButton cancelar = new JButton("Cancelar");
        cancelar.setBackground(Color.white);
        cancelar.setForeground(Color.decode("#000000"));
        cancelar.setBorder(null);
        cancelar.addActionListener(e -> dispose());

        // Botón Eliminar
        JButton eliminar = new JButton("Eliminar");
        eliminar.setBackground(Color.decode("#E53935"));
        eliminar.setForeground(Color.white);
        eliminar.setBorder(null);
        eliminar.addActionListener(e -> {
            // Intentar eliminar usuario
            boolean ok = controlador.getControladorEliminarUsuario().eliminarUsuario(idUsuario);
            // Mostrar resultado
            if (ok) {
                JOptionPane.showMessageDialog(this, "Usuario desactivado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                controlador.getControladorNavegacion().refrescarUsuarios();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No es posible eliminar el usuario: existen préstamos activos o ocurrió un error", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        botones.add(cancelar);
        botones.add(eliminar);

        getContentPane().add(contenido, BorderLayout.CENTER);
        getContentPane().add(botones, BorderLayout.SOUTH);
    }
}
