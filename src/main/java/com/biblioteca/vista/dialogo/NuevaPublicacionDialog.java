package com.biblioteca.vista.dialogo;

import java.awt.Dimension;
import java.time.LocalDate;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.biblioteca.controlador.Controlador;

/**
 * Modal para crear una nueva publicación.
 * Implementa un pequeño wizard: paso 1 (datos comunes) → paso 2 (datos por
 * tipo)
 */
public class NuevaPublicacionDialog extends BasePublicacionDialog {
    /**
     * Controlador principal de la aplicación
     */
    private Controlador controlador;

    /**
     * Constructor
     * 
     * @param parent
     * @param controlador
     */
    public NuevaPublicacionDialog(final JFrame parent, final Controlador controlador) {
        super(parent, "Nueva Publicación");
        this.controlador = controlador;
        super.initUI();
        getConfirmarLibro().setText("Añadir");
        getConfirmarRevista().setText("Añadir");
        configurarEventos();
        setSize(new Dimension(340, 500));
        setLocationRelativeTo(parent);
    }

    /**
     * Configura los eventos de los botones de añadir publicación
     */
    public void configurarEventos() {
        // Acción botón añadir
        getConfirmarLibro().addActionListener(e -> {
            // Validar campos
            if (!validarPasoLibro()) {
                return;
            }
            // Intentar crear la publicación
            boolean ok = controlador.getControladorNuevaPublicacionDialog().crearPublicacionLibro(
                    getIsbnTexto(),
                    getTituloTexto(),
                    getIdiomaTexto(),
                    getTemasTexto(),
                    getModulosTexto(),
                    getCiclosTexto(),
                    getEditorialTexto(),
                    Integer.parseInt(getNumeroEdicionTexto()),
                    LocalDate.parse(getFechaPublicacionTexto()),
                    getAutoresTexto());
            // Mostrar resultado
            if (ok) {
                JOptionPane.showMessageDialog(this, "Publicación tipo Libro añadida", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                // Refrescar vista de publicaciones y panel de control
                controlador.getControladorNavegacion().refrescarPublicaciones();
                controlador.getControladorNavegacion().refrescarPanelControl();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error añadiendo la publicación en la base de datos", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Acción botón añadir
        getConfirmarRevista().addActionListener(e -> {
            // Validar campos
            if (!validarPasoRevista()) {
                return;
            }
            // Intentar crear la publicación
            boolean ok = controlador.getControladorNuevaPublicacionDialog().crearPublicacionRevista(
                    getIsbnTexto(),
                    getTituloTexto(),
                    getIdiomaTexto(),
                    getTemasTexto(),
                    getModulosTexto(),
                    getCiclosTexto(),
                    getEditorialTexto(),
                    getPeriodicidadTexto());
            // Mostrar resultado
            if (ok) {
                JOptionPane.showMessageDialog(this, "Publicación tipo Revista añadida", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                // Refrescar vista de publicaciones y panel de control
                controlador.getControladorNavegacion().refrescarPublicaciones();
                controlador.getControladorNavegacion().refrescarPanelControl();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error añadiendo la publicación en la base de datos", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

}
