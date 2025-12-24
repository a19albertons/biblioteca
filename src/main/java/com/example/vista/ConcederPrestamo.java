package com.example.vista;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.BorderFactory;

import com.example.controlador.Controlador;

/**
 * Clase para la vista Conceder préstamo
 */
public class ConcederPrestamo {

    /**
     * Controlador de la aplicación
     */
    Controlador controlador;

    /**
     * Constructor de la vista ConcederPrestamo
     *
     * @param controlador controlador principal
     */
    public ConcederPrestamo(Controlador controlador) {
        this.controlador = controlador;
    }

    /**
     * Muestra la pantalla para conceder un préstamo
     *
     * @return JPanel con la vista de concesión de préstamos
     */
    public JPanel pantalla() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(600, 600));
        panel.setBackground(Color.decode("#EDF3F6"));
        panel.setLayout(null);

        // Panel de encabezado con título
        JPanel encabezado = new JPanel();
        encabezado.setSize(new Dimension(600, 60));
        encabezado.setBackground(Color.white);
        encabezado.setLayout(null);
        encabezado.setBounds(0, 0, 600, 60);

        // Titulo
        JLabel titulo = new JLabel("Nuevo prestamo");
        titulo.setFont(titulo.getFont().deriveFont(24f));
        titulo.setBounds(30, 10, 300, 40);
        encabezado.add(titulo);

        // Boton nueva publicacion
        JButton btnFormularioDevolver = new JButton("Formulario dar de baja");
        btnFormularioDevolver.setBounds(430, 15, 150, 30);
        btnFormularioDevolver.setBackground(Color.white);
        btnFormularioDevolver.setForeground(Color.decode("#468DAE"));
        btnFormularioDevolver.setFocusPainted(false);
        btnFormularioDevolver.setBorder(null);
        encabezado.add(btnFormularioDevolver);

        // Panel de contenido
        JPanel contenido = new JPanel();
        contenido.setSize(540, 480);
        contenido.setLayout(null);
        contenido.setBackground(Color.white);
        contenido.setBounds(30, 80, 540, 480);

        // Componentes del formulario
        // Paso 1: Identificar socio
        JLabel paso1 = new JLabel("1. Identificar Socio (Usuario)");
        paso1.setBounds(20, 20, 300, 30);
        paso1.setForeground(Color.decode("#468DAE"));
        paso1.setFont(paso1.getFont().deriveFont(16f));
        contenido.add(paso1);

        JLabel dniID = new JLabel("DNI / ID:");
        dniID.setBounds(20, 70, 100, 25);
        contenido.add(dniID);

        JTextField txtDniID = new JTextField();
        txtDniID.setBounds(20, 100, 300, 35);
        contenido.add(txtDniID);

        JButton btnBuscarSocio = new JButton("Buscar");
        btnBuscarSocio.setBounds(330, 100, 90, 35);
        btnBuscarSocio.setBackground(Color.decode("#468DAE"));
        btnBuscarSocio.setForeground(Color.WHITE);
        btnBuscarSocio.setFocusPainted(false);
        btnBuscarSocio.setBorder(null);
        contenido.add(btnBuscarSocio);

        // Resultado búsqueda socio
        JLabel resultadoSocio = new JLabel("");
        resultadoSocio.setBounds(20, 150, 400, 40);
        resultadoSocio.setText(
                "<html>Usuario: Estudiante) : <span style='color:#2BC187; font-weight:bold'>Sin sanciones</span></html>");
        resultadoSocio.setBackground(Color.decode("#EDF3F6"));
        resultadoSocio.setOpaque(true);
        // Pequeño margen izquierdo para separar el texto del borde (10px)
        resultadoSocio.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        contenido.add(resultadoSocio);

        // mutable holder para el id de usuario seleccionado (para usar desde lambdas)
        final int[] usuarioSeleccionado = new int[] { -1 };

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

        JTextField txtIdEjemplar = new JTextField();
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
        txtIdEjemplar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void doDetect() {
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
                String[] detectado = controlador.getControladorConcederPrestamo().detectarEjemplar(idEj);
                if (detectado == null) {
                    txtPublicacion.setText("");
                    txtFechaInicio.setText("");
                    txtFechaFin.setText("");
                    return;
                }
                // detectado: idEjemplar, idPublicacion, numEjemplar, estadoEjemplar, titulo,
                // numEdicion, tipo
                String pubTitulo = detectado[4];
                String numEd = detectado[5];
                String tipoPub = detectado[6];
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

                // establecer fecha inicio como hoy
                java.time.LocalDate hoy = java.time.LocalDate.now();
                txtFechaInicio.setText(hoy.toString());
                // calcular fecha fin segun reglas y tipo de usuario
                int idUsuarioSel = usuarioSeleccionado[0];
                java.time.LocalDate fechaFinLocal;
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

            // Detectar cambios en el campo de texto
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                doDetect();
            }

            // Detectar cambios en el campo de texto
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                doDetect();
            }

            // Detectar cambios en el campo de texto
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                doDetect();
            }
        });

        // Boton cancelar
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(250, 420, 100, 35);
        btnCancelar.setBackground(Color.white);
        btnCancelar.setForeground(Color.BLACK);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorder(null);
        contenido.add(btnCancelar);

        // Boton Registar Prestamo
        JButton btnRegistrarPrestamo = new JButton("Registrar Préstamo");
        btnRegistrarPrestamo.setBounds(370, 420, 120, 35);
        btnRegistrarPrestamo.setBackground(Color.decode("#F4791B"));
        btnRegistrarPrestamo.setForeground(Color.WHITE);
        btnRegistrarPrestamo.setFocusPainted(false);
        btnRegistrarPrestamo.setBorder(null);
        contenido.add(btnRegistrarPrestamo);

        // Eventos botones
        btnFormularioDevolver.addActionListener(e -> {
            controlador.getControladorNavegacion().cambiarPantallaHijo("devolverPrestamo");
        });

        btnBuscarSocio.addActionListener(e -> {
            String input = txtDniID.getText().trim();
            // si está vacío, mensaje de error
            if (input.isEmpty()) {
                resultadoSocio.setText("<html><span style='color:#F4791B'>Ingrese DNI o ID</span></html>");
                usuarioSeleccionado[0] = -1;
                return;
            }
            // buscar usuario y comprobar resultado
            String[] datos = controlador.getControladorConcederPrestamo().buscarUsuarioPorDniOId(input);
            if (datos == null) {
                resultadoSocio.setText("<html><span style='color:#F4791B'>Usuario no encontrado</span></html>");
                usuarioSeleccionado[0] = -1;
                return;
            }
            // datos: id, dni, nombre_completo, sancion_activa, tipo_desc
            usuarioSeleccionado[0] = Integer.parseInt(datos[0]);
            String estado = datos[3];
            String tipoDesc = datos[4];
            // mostrar resultado de estado usuario
            if ("SANCIONADO".equalsIgnoreCase(estado)) {
                resultadoSocio.setText("<html>Usuario: " + datos[2] + " (" + tipoDesc
                        + ") - <span style='color:#F4791B; font-weight:bold'>Tiene sanciones</span></html>");
            } else if ("BAJA".equalsIgnoreCase(estado)) {
                resultadoSocio.setText("<html>Usuario: " + datos[2]
                        + " - <span style='color:#F4791B; font-weight:bold'>Dado de baja</span></html>");
            } else {
                resultadoSocio.setText("<html>Usuario: " + datos[2] + " (" + tipoDesc
                        + ") - <span style='color:#2BC187; font-weight:bold'>Sin sanciones</span></html>");
            }
        });

        btnCancelar.addActionListener(e -> {
            controlador.getControladorNavegacion().cambiarPantallaHijo("panelControl");
        });

        btnRegistrarPrestamo.addActionListener(e -> {
            // Validar datos
            if (usuarioSeleccionado[0] == -1) {
                javax.swing.JOptionPane.showMessageDialog(null, "Seleccione primero un usuario válido", "Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }
            // validar id ejemplar
            String idEjStr = txtIdEjemplar.getText().trim();
            if (idEjStr.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(null, "Introduzca el ID del ejemplar", "Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }
            int idEj;
            try {
                // convertir a entero
                idEj = Integer.parseInt(idEjStr);
            } catch (NumberFormatException ex) {
                // mostrar error
                javax.swing.JOptionPane.showMessageDialog(null, "ID de ejemplar inválido", "Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }
            // registrar préstamo
            String err = controlador.getControladorConcederPrestamo().registrarPrestamo(usuarioSeleccionado[0], idEj);
            // mostrar resultado
            if (err == null) {
                javax.swing.JOptionPane.showMessageDialog(null, "Préstamo registrado correctamente", "Éxito",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
                // Refrescar vistas dependientes y marcar inicio como activo
                controlador.getControladorNavegacion().refrescarPublicaciones();
                controlador.getControladorNavegacion().refrescarPanelControl();
                controlador.getControladorNavegacion().marcarPantallaActiva("panelControl");
                controlador.getControladorNavegacion().cambiarPantallaHijo("panelControl");
            } else {
                javax.swing.JOptionPane.showMessageDialog(null, err, "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        });

        // Añadir los dos subpaneles al principal
        panel.add(encabezado);
        panel.add(contenido);
        return panel;
    }

}
