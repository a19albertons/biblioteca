package com.biblioteca.vista.pantalla;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.biblioteca.controlador.Controlador;
import com.biblioteca.dto.EjemplarConTituloDTO;

/**
 * Clase para la vista Devolver préstamo
 */
public class DevolverPrestamo extends PantallaPrestamoBase {

    /**
     * Controlador de la aplicación
     */
    private final Controlador controlador;

    /**
     * Constructor de la vista DevolverPrestamo
     *
     * @param controlador controlador principal
     */
    public DevolverPrestamo(final Controlador controlador) {
        super(controlador, "Devolución Préstamo", "Formulario registrar prestamo");
        this.controlador = controlador;
        super.pantalla();
    }

    /**
     * Getter para el controlador
     *
     * @return el controlador de la aplicación
     */
    public Controlador getControlador() {
        return controlador;
    }

    /**
     * Configura el paso 2: Identificar ejemplar
     *
     * @param contenido     el panel de contenido
     * @param txtIdEjemplar campo de texto para el ID del ejemplar
     */
    @Override 
    protected void configurarPasoEjemplar(final JPanel contenido, final JTextField txtIdEjemplar) {
        JLabel paso2 = new JLabel("2. Identificar Ejemplar");
        paso2.setBounds(20, 200, 300, 30);
        paso2.setForeground(Color.decode("#468DAE"));
        paso2.setFont(paso2.getFont().deriveFont(16f));
        contenido.add(paso2);

        JLabel idEjemplar = new JLabel("ID Ejemplar:");
        idEjemplar.setBounds(20, 250, 100, 25);
        contenido.add(idEjemplar);

        JLabel publicacion = new JLabel("Publicación");
        publicacion.setBounds(230, 250, 300, 35);
        contenido.add(publicacion);

        JLabel txtPublicacion = new JLabel();
        txtPublicacion.setBounds(230, 280, 260, 35);
        txtPublicacion.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, Color.white));
        contenido.add(txtPublicacion);

        configurarDetectorEjemplar(txtIdEjemplar, txtPublicacion);
    }

    /**
     * Configura el detector de ejemplar mediante DocumentListener
     *
     * @param txtIdEjemplar  campo de texto para el ID del ejemplar
     * @param txtPublicacion etiqueta para mostrar la publicación detectada
     */
    private void configurarDetectorEjemplar(final JTextField txtIdEjemplar, final JLabel txtPublicacion) {
        txtIdEjemplar.getDocument().addDocumentListener(new DocumentListener() {
            private void doDetect() {
                String idEjStr = txtIdEjemplar.getText().trim();
                if (idEjStr.isEmpty()) {
                    txtPublicacion.setText("");
                    return;
                }
                int idEj;
                try {
                    idEj = Integer.parseInt(idEjStr);
                } catch (NumberFormatException ex) {
                    txtPublicacion.setText("");
                    return;
                }
                EjemplarConTituloDTO detectado = getControlador().getControladorDevolverPrestamo()
                        .detectarEjemplar(idEj);
                if (detectado == null) {
                    txtPublicacion.setText("");
                    return;
                }
                String pubTitulo = detectado.getTitulo();
                String numEd = String.valueOf(detectado.getNumEdicion());
                String tipoPub = detectado.getTipoPublicacion().toString();
                if ("L".equalsIgnoreCase(tipoPub)) {
                    String texto = "Detectado: " + pubTitulo
                            + (numEd != null && !numEd.isEmpty() ? " (Ed. " + numEd + ")" : "");
                    txtPublicacion.setText(texto);
                } else if ("R".equalsIgnoreCase(tipoPub)) {
                    String texto = "Detectado: " + pubTitulo + " (Revista)";
                    txtPublicacion.setText(texto);
                } else {
                    txtPublicacion.setText("Detectado: " + pubTitulo);
                }
            }

            @Override
            public void insertUpdate(final DocumentEvent e) {
                doDetect();
            }

            @Override
            public void removeUpdate(final DocumentEvent e) {
                doDetect();
            }

            @Override
            public void changedUpdate(final DocumentEvent e) {
                doDetect();
            }
        });
    }

    /**
     * Configura los botones de acción
     *
     * @param contenido     el panel de contenido
     * @param txtIdEjemplar campo de texto para el ID del ejemplar
     */
    @Override 
    protected void configurarBotonesAccion(final JPanel contenido, final JTextField txtIdEjemplar) {
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(250, 420, 100, 35);
        btnCancelar.setBackground(Color.white);
        btnCancelar.setForeground(Color.BLACK);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorder(null);
        contenido.add(btnCancelar);

        JButton btnDevolverPrestamo = new JButton("DEVOLVER PRESTAMO");
        btnDevolverPrestamo.setBounds(370, 420, 120, 35);
        btnDevolverPrestamo.setBackground(Color.decode("#F4791B"));
        btnDevolverPrestamo.setForeground(Color.WHITE);
        btnDevolverPrestamo.setFocusPainted(false);
        btnDevolverPrestamo.setBorder(null);
        btnDevolverPrestamo.setFont(new Font("Open Sans", Font.PLAIN, 11));
        contenido.add(btnDevolverPrestamo);

        btnCancelar.addActionListener(e -> {
            getControlador().getControladorNavegacion().cambiarPantallaHijo("panelControl");
        });

        btnDevolverPrestamo.addActionListener(e -> {
            if (!super.validarUsuarioYEjemplar(getUsuarioSeleccionado(), txtIdEjemplar)) {
                return;
            }
            String err = getControlador().getControladorDevolverPrestamo()
                    .devolverPrestamo(getUsuarioSeleccionado()[0], Integer.parseInt(txtIdEjemplar.getText()));
            if (err == null) {
                JOptionPane.showMessageDialog(null, "Devolución registrada correctamente", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                String notif = getControlador().getControladorDevolverPrestamo()
                        .obtenerYLimpiarUltimaNotificacionSancion();
                if (notif != null) {
                    JOptionPane.showMessageDialog(null, notif, "Información", JOptionPane.INFORMATION_MESSAGE);
                }
                getControlador().getControladorNavegacion().refrescarPublicaciones();
                getControlador().getControladorNavegacion().refrescarPanelControl();
                getControlador().getControladorNavegacion().marcarPantallaActiva("panelControl");
                getControlador().getControladorNavegacion().cambiarPantallaHijo("panelControl");
            } else {
                JOptionPane.showMessageDialog(null, err, "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Eventos botones
        getBtnCambiarFormulario().addActionListener(e -> {
            controlador.getControladorNavegacion().cambiarPantallaHijo("concederPrestamo");
        });
    }

}
