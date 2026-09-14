package com.biblioteca.vista.dialogo;

import java.awt.BorderLayout;
import java.awt.Color;
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
 * Clase base para diálogos de eliminación (ejemplar, publicación, usuario).
 * Proporciona la estructura común de UI y lógica de confirmación.
 */
public abstract class BaseEliminarDialog extends OverlayDialog {
    /**
     * Controlador principal de la aplicación
     */
    private final Controlador controlador;
    /**
     * ID del elemento a eliminar
     */
    private final int id;
    /**
     * Tipo de elemento (EJEMPLAR, PUBLICACION, USUARIO)
     */
    private final String tipo;
    /**
     * Título del diálogo
     */
    private String titulo;
    /**
     * Mensaje de confirmación
     */
    private String mensajeConfirmacion;
    /**
     * ID de la publicación asociada (solo para ejemplares)
     */
    private int idPublicacion;

    /**
     * Obtiene el controlador principal de la aplicación.
     * 
     * @return Controlador principal
     */
    public Controlador getControlador() {
        return controlador;
    }

    /**
     * Obtiene el ID del elemento a eliminar.
     * 
     * @return ID del elemento
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el ID de la publicación asociada (solo para ejemplares).
     * 
     * @param idPublicacion ID de la publicación
     */
    public void setIdPublicacion(final int idPublicacion) {
        this.idPublicacion = idPublicacion;
    }

    /**
     * Obtiene el tipo de elemento.
     * 
     * @return Tipo de elemento
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Obtiene el título del diálogo.
     * 
     * @return Título del diálogo
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Obtiene el mensaje de confirmación.
     * 
     * @return Mensaje de confirmación
     */
    public String getMensajeConfirmacion() {
        return mensajeConfirmacion;
    }

    /**
     * Obtiene el ID de la publicación asociada.
     * 
     * @return ID de la publicación
     */
    public int getIdPublicacion() {
        return idPublicacion;
    }

    /**
     * Constructor del diálogo con inicialización de UI
     * 
     * @param parent              Ventana padre (puede ser null)
     * @param controlador         Controlador principal
     * @param id                  ID del elemento a eliminar
     * @param titulo              Título del diálogo
     * @param mensajeConfirmacion Mensaje de confirmación
     * @param tipo                Tipo de elemento (EJEMPLAR, PUBLICACION, USUARIO)
     */
    public BaseEliminarDialog(final JFrame parent, final Controlador controlador, final int id, final String titulo,
            final String mensajeConfirmacion, final String tipo) {
        super(parent, titulo);
        this.controlador = controlador;
        this.id = id;
        this.tipo = tipo;
        this.titulo = titulo;
        this.mensajeConfirmacion = mensajeConfirmacion;
        this.idPublicacion = 0;
    }

    /**
     * Inicializa la interfaz del diálogo
     */
    protected final void initUI() {
        // Layout y contenido
        getContentPane().setLayout(new BorderLayout());
        JPanel contenido = new JPanel(new BorderLayout());
        contenido.setBackground(Color.white);
        contenido.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Título y mensaje
        JLabel tituloLabel = new JLabel(titulo);
        tituloLabel.setFont(tituloLabel.getFont().deriveFont(Font.BOLD, 16f));
        contenido.add(tituloLabel, BorderLayout.NORTH);

        // Mensaje de confirmación
        JLabel texto = new JLabel(mensajeConfirmacion);
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
            if (onEliminar()) {
                onExit(true);
            } else {
                onExit(false);
            }
        });

        botones.add(cancelar);
        botones.add(eliminar);

        getContentPane().add(contenido, BorderLayout.CENTER);
        getContentPane().add(botones, BorderLayout.SOUTH);
    }

    /**
     * Lógica de eliminación específica
     * Sobrescribir en subclases
     * 
     * @return true si la eliminación fue exitosa
     */
    protected boolean onEliminar() {
        // Implementación genérica - subclases deben manejar la lógica específica
        return false;
    }

    /**
     * Manejo de salida (refrescos)
     * Sobrescribir en subclases
     * 
     * @param exito true si la eliminación fue exitosa
     */
    protected void onExit(final boolean exito) {
        if (exito) {
            JOptionPane.showMessageDialog(this, "Éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
        dispose();
    }
}
