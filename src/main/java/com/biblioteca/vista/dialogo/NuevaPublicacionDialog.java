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
        confirmarLibro.setText("Añadir");
        confirmarRevista.setText("Añadir");
        configurarEventos();
        setSize(new Dimension(340, 500));
        setLocationRelativeTo(parent);
    }



    public void configurarEventos() {
        // Acción botón añadir
        confirmarLibro.addActionListener(e -> {
            // Validar campos
            if (!validarPasoLibro()) {
                return;
            }
            // Intentar crear la publicación
            boolean ok = controlador.getControladorNuevaPublicacionDialog().crearPublicacionLibro(
                    isbnField.getText().trim(),
                    tituloField.getText().trim(),
                    idiomaField.getText().trim(),
                    temasField.getText().trim(),
                    modulosField.getText().trim(),
                    ciclosField.getText().trim(),
                    editorialField.getText().trim(),
                    Integer.parseInt(numeroEdicionField.getText().trim()),
                    LocalDate.parse(fechaPublicacionField.getText().trim()),
                    autoresField.getText().trim());
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
        confirmarRevista.addActionListener(e -> {
            // Validar campos
            if (!validarPasoRevista()) {
                return;
            }
            // Intentar crear la publicación
            boolean ok = controlador.getControladorNuevaPublicacionDialog().crearPublicacionRevista(
                    isbnField.getText().trim(),
                    tituloField.getText().trim(),
                    idiomaField.getText().trim(),
                    temasField.getText().trim(),
                    modulosField.getText().trim(),
                    ciclosField.getText().trim(),
                    editorialField.getText().trim(),
                    periodicidadField.getText().trim());
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
