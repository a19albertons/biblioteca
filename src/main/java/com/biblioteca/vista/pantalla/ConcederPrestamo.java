package com.biblioteca.vista.pantalla;

import java.awt.Color;
import java.time.LocalDate;

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
 * Clase para la vista Conceder préstamo
 */
public class ConcederPrestamo extends PantallaPrestamoBase {

    /**
     * Controlador de la aplicación
     */
    private final Controlador controlador;

    /**
     * Botón cancelar
     */
    private JButton btnCancelar;

    /**
     * Botón registrar préstamo
     */
    private JButton btnRegistrarPrestamo;

    /**
     * Constructor de la vista ConcederPrestamo
     *
     * @param controlador controlador principal
     */
    public ConcederPrestamo(final Controlador controlador) {
        super(controlador, "Nuevo prestamo", "Formulario dar de baja");
        this.controlador = controlador;
        super.pantalla();

    }

    /**
     * Getters para el controlador
     * 
     * @return controlador principal
     */
    public Controlador getControlador() {
        return controlador;
    }

    /**
     * Crea el panel de contenido con todos los componentes del formulario
     *
     * @param contenido     el panel de contenido
     * @param txtIdEjemplar campo de texto para el ID del ejemplar
     */
    @Override 
    protected void configurarPasoEjemplar(final JPanel contenido, final JTextField txtIdEjemplar) {
        // Paso 2: Seleccionar libro
        JLabel paso2 = new JLabel("2. Seleccionar Libro");
        paso2.setBounds(20, 200, 300, 30);
        paso2.setForeground(Color.decode("#468DAE"));
        paso2.setFont(paso2.getFont().deriveFont(16f));
        contenido.add(paso2);

        // ID Ejemplar
        JLabel idEjemplar = new JLabel("ID Ejemplar:");
        idEjemplar.setBounds(20, 250, 100, 25);
        contenido.add(idEjemplar);

        // Texto id ejemplar
        txtIdEjemplar.setBounds(20, 280, 200, 35);
        contenido.add(txtIdEjemplar);

        // Publicacion
        JLabel publicacion = new JLabel("Publicacion");
        publicacion.setBounds(230, 250, 300, 35);
        contenido.add(publicacion);

        JLabel txtPublicacion = new JLabel();
        txtPublicacion.setBounds(230, 280, 260, 35);
        txtPublicacion.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, Color.white));
        contenido.add(txtPublicacion);

        // Fecha Inicio
        JLabel fechaInicio = new JLabel("Fecha Inicio");
        fechaInicio.setBounds(20, 330, 300, 35);
        contenido.add(fechaInicio);

        JLabel txtFechaInicio = new JLabel();
        txtFechaInicio.setBounds(20, 360, 200, 35);
        txtFechaInicio.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, Color.white));
        contenido.add(txtFechaInicio);

        // Fecha Fin
        JLabel fechaFin = new JLabel("Fecha Devolucion Prevista");
        fechaFin.setBounds(230, 330, 300, 35);
        contenido.add(fechaFin);

        JLabel txtFechaFin = new JLabel();
        txtFechaFin.setBounds(230, 360, 200, 35);
        txtFechaFin.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, Color.white));
        contenido.add(txtFechaFin);

        // Detectar automáticamente al escribir ID de ejemplar
        DocumentListener listener = new DocumentListener() {
            private void doDetect() {
                detectarCampos(txtIdEjemplar, txtPublicacion, txtFechaInicio, txtFechaFin, getUsuarioSeleccionado());
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
        };
        txtIdEjemplar.getDocument().addDocumentListener(listener);

        // Boton cancelar
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(250, 420, 100, 35);
        btnCancelar.setBackground(Color.white);
        btnCancelar.setForeground(Color.BLACK);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorder(null);
        contenido.add(btnCancelar);

        // Boton Registar Prestamo
        btnRegistrarPrestamo = new JButton("Registrar Préstamo");
        btnRegistrarPrestamo.setBounds(370, 420, 120, 35);
        btnRegistrarPrestamo.setBackground(Color.decode("#F4791B"));
        btnRegistrarPrestamo.setForeground(Color.WHITE);
        btnRegistrarPrestamo.setFocusPainted(false);
        btnRegistrarPrestamo.setBorder(null);
        contenido.add(btnRegistrarPrestamo);

    }

    /**
     * Detecta automáticamente los campos al escribir ID de ejemplar
     *
     * @param txtIdEjemplar       JTextField con el ID del ejemplar
     * @param txtPublicacion      JLabel para mostrar la publicación
     * @param txtFechaInicio      JLabel para mostrar la fecha de inicio
     * @param txtFechaFin         JLabel para mostrar la fecha de fin
     * @param usuarioSeleccionado array holder con el ID de usuario seleccionado
     */
    private void detectarCampos(final JTextField txtIdEjemplar, final JLabel txtPublicacion,
            final JLabel txtFechaInicio, final JLabel txtFechaFin, final int[] usuarioSeleccionado) {
        // si está vacío, limpiar campos
        String idEjStr = txtIdEjemplar.getText().trim();
        if (idEjStr.isEmpty()) {
            txtPublicacion.setText("");
            txtFechaInicio.setText("");
            txtFechaFin.setText("");
            return;
        }
        int idEj;
        try {
            // convertir a entero
            idEj = Integer.parseInt(idEjStr);
        } catch (NumberFormatException ex) {
            // si hay texto no numérico, limpiar campos
            txtPublicacion.setText("");
            txtFechaInicio.setText("");
            txtFechaFin.setText("");
            return;
        }
        // detectar ejemplar
        EjemplarConTituloDTO detectado = controlador.getControladorConcederPrestamo().detectarEjemplar(idEj);
        if (detectado == null) {
            txtPublicacion.setText("");
            txtFechaInicio.setText("");
            txtFechaFin.setText("");
            return;
        }
        // detectado: idEjemplar, idPublicacion, numEjemplar, estadoEjemplar, titulo,
        // numEdicion, tipo
        // CPD-OFF
        String pubTitulo = detectado.getTitulo();
        String numEd = String.valueOf(detectado.getNumEdicion());
        String tipoPub = detectado.getTipoPublicacion().toString();
        // mostrar info publicación
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
        // CPD-ON

        // establecer fecha inicio como hoy
        LocalDate hoy = LocalDate.now();
        txtFechaInicio.setText(hoy.toString());
        // calcular fecha fin segun reglas y tipo de usuario
        int idUsuarioSel = usuarioSeleccionado[0];
        LocalDate fechaFinLocal;
        // para revistas, mismo día; para libros, +7 días (o +7 días si es profesor)
        if ("R".equalsIgnoreCase(tipoPub)) {
            // revista
            if (idUsuarioSel != -1) {
                // obtener tipo de usuario
                String[] detUsuario = controlador.getControladorEditarUsuarioDialog()
                        .obtenerDetallesUsuario(idUsuarioSel);
                String tipoCode = detUsuario != null ? detUsuario[5] : null;
                // ajustar fecha fin
                if ("P".equalsIgnoreCase(tipoCode)) {
                    fechaFinLocal = hoy.plusDays(7);
                } else {
                    fechaFinLocal = hoy; // mismo dia
                }
            } else {
                // usuario no seleccionado -> asumir mismo dia para revistas
                fechaFinLocal = hoy;
            }
        } else {
            fechaFinLocal = hoy.plusDays(7);
        }
        txtFechaFin.setText(fechaFinLocal.toString());
    }

    /**
     * Configura los eventos de los botones
     *
     * @param contenido     el panel de contenido
     * @param txtIdEjemplar campo de texto para el ID del ejemplar
     */
    @Override 
    protected void configurarBotonesAccion(final JPanel contenido, final JTextField txtIdEjemplar) {
        // Eventos botones
        getBtnCambiarFormulario().addActionListener(e -> {
            controlador.getControladorNavegacion().cambiarPantallaHijo("devolverPrestamo");
        });

        btnCancelar.addActionListener(e -> {
            controlador.getControladorNavegacion().cambiarPantallaHijo("panelControl");
        });

        btnRegistrarPrestamo.addActionListener(e -> {
            // Validar datos
            if (!super.validarUsuarioYEjemplar(getUsuarioSeleccionado(), txtIdEjemplar)) {
                return;
            }
            // registrar préstamo
            String err = controlador.getControladorConcederPrestamo()
                    .registrarPrestamo(getUsuarioSeleccionado()[0], Integer.parseInt(txtIdEjemplar.getText()));
            // mostrar resultado
            if (err == null) {
                JOptionPane.showMessageDialog(null, "Préstamo registrado correctamente", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                // Refrescar vistas dependientes y marcar inicio como activo
                controlador.getControladorNavegacion().refrescarPublicaciones();
                controlador.getControladorNavegacion().refrescarPanelControl();
                controlador.getControladorNavegacion().marcarPantallaActiva("panelControl");
                controlador.getControladorNavegacion().cambiarPantallaHijo("panelControl");
            } else {
                JOptionPane.showMessageDialog(null, err, "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

}
