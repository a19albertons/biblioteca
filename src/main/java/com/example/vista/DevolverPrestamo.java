package com.example.vista;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.BorderFactory;

import com.example.controlador.Controlador;

/**
 * Clase para la vista Devolver préstamo
 */
public class DevolverPrestamo {

    /**
     * Controlador de la aplicación
     */
    Controlador controlador;

    /**
     * Constructor de la vista DevolverPrestamo
     *
     * @param controlador controlador principal
     */
    public DevolverPrestamo(Controlador controlador) {
        this.controlador = controlador;
    }

    /**
     * Muestra la pantalla para gestionar devoluciones de préstamo
     *
     * @return JPanel con la vista de devolución de préstamos
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
        JLabel titulo = new JLabel("Devolución Préstamo");
        titulo.setFont(titulo.getFont().deriveFont(24f));
        titulo.setBounds(30, 10, 300, 40);
        encabezado.add(titulo);

        // Link / boton formulario registrar prestamo (estilo enlace azul)
        JButton btnFormularioRegistrar = new JButton("Formulario registrar prestamo");
        btnFormularioRegistrar.setBounds(360, 15, 220, 30);
        btnFormularioRegistrar.setBackground(Color.white);
        btnFormularioRegistrar.setForeground(Color.decode("#468DAE"));
        btnFormularioRegistrar.setFocusPainted(false);
        btnFormularioRegistrar.setBorder(null);
        encabezado.add(btnFormularioRegistrar);

        // Panel de contenido
        JPanel contenido = new JPanel();
        contenido.setSize(540, 480);
        contenido.setLayout(null);
        contenido.setBackground(Color.white);
        contenido.setBounds(30, 80, 540, 480);

        // Componentes del formulario
        // Paso 1: Identificar socio
        JLabel paso1 = new JLabel("1. Identificar Socio (Usuario)");
        paso1.setBounds(20, 20, 350, 30);
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

        JLabel resultadoSocio = new JLabel("");
        resultadoSocio.setBounds(20, 150, 480, 40);
        resultadoSocio.setText("<html>Usuario: (Estudiante) - <span style='color:#2BC187; font-weight:bold'>Sin Sanciones</span></html>");
        resultadoSocio.setBackground(Color.decode("#EDF3F6"));
        resultadoSocio.setOpaque(true);
        resultadoSocio.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        contenido.add(resultadoSocio);

        // mutable holder para el id de usuario seleccionado (para usar desde lambdas)
        final int[] usuarioSeleccionado = new int[] { -1 };

        // Paso 2: Identificar Ejemplar
        JLabel paso2 = new JLabel("2. Identificar Ejemplar");
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

        // Publicacion (mostrador de publicación detectada)
        JLabel publicacion = new JLabel("Publicación");
        publicacion.setBounds(230, 250, 300, 35);
        contenido.add(publicacion);

        JLabel txtPublicacion = new JLabel("Detectado: Estructura de Datos (Ed. 2)");
        txtPublicacion.setBounds(230, 280, 260, 35);
        txtPublicacion.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, Color.white));
        contenido.add(txtPublicacion);

        // Detectar automáticamente al escribir ID de ejemplar
        txtIdEjemplar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void doDetect() {
                String idEjStr = txtIdEjemplar.getText().trim();
                // Si está vacío, limpiar y salir
                if (idEjStr.isEmpty()) {
                    txtPublicacion.setText("");
                    return;
                }
                // intentar parsear id ejemplar
                int idEj;
                try {
                    idEj = Integer.parseInt(idEjStr);
                } catch (NumberFormatException ex) {
                    txtPublicacion.setText("");
                    return;
                }
                // detectar ejemplar
                String[] detectado = controlador.getControladorDevolverPrestamo().detectarEjemplar(idEj);
                if (detectado == null) {
                    txtPublicacion.setText("");
                    return;
                }
                // Aplica datos en el formato adecuado
                String pubTitulo = detectado[4];
                String numEd = detectado[5];
                String tipoPub = detectado[6];
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

            // modifcacion del texto de ejemplar
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                doDetect();
            }

            // modifcacion del texto de ejemplar
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                doDetect();
            }

            // modifcacion del texto de ejemplar
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

        // Boton Devolver Prestamo
        JButton btnDevolverPrestamo = new JButton("DEVOLVER PRESTAMO");
        btnDevolverPrestamo.setBounds(370, 420, 120, 35);
        btnDevolverPrestamo.setBackground(Color.decode("#F4791B"));
        btnDevolverPrestamo.setForeground(Color.WHITE);
        btnDevolverPrestamo.setFocusPainted(false);
        btnDevolverPrestamo.setBorder(null);
        btnDevolverPrestamo.setFont(new Font("Open Sans", Font.PLAIN, 11));
        contenido.add(btnDevolverPrestamo);

        // Eventos botones
        btnFormularioRegistrar.addActionListener(e -> {
            controlador.getControladorNavegacion().cambiarPantallaHijo("concederPrestamo");
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
            String[] datos = controlador.getControladorDevolverPrestamo().buscarUsuarioPorDniOId(input);
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

        btnDevolverPrestamo.addActionListener(e -> {
            // Validar que se haya seleccionado usuario
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
                idEj = Integer.parseInt(idEjStr);
            } catch (NumberFormatException ex) {
                javax.swing.JOptionPane.showMessageDialog(null, "ID de ejemplar inválido", "Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }
            // comprobar que exista préstamo activo entre usuario y ejemplar
            boolean existe = controlador.getControladorDevolverPrestamo()
                    .existePrestamoActivoUsuarioEjemplar(usuarioSeleccionado[0], idEj);
            if (!existe) {
                javax.swing.JOptionPane.showMessageDialog(null,
                        "No existe un préstamo activo entre este usuario y el ejemplar", "Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }
            // ejecutar devolución
            String err = controlador.getControladorDevolverPrestamo().registrarDevolucion(usuarioSeleccionado[0],
                    idEj);
            if (err == null) {
                javax.swing.JOptionPane.showMessageDialog(null, "Devolución registrada correctamente", "Éxito",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
                // Mostrar notificación sobre sanción si existe
                String notif = controlador.getControladorDevolverPrestamo().obtenerYLimpiarUltimaNotificacionSancion();
                if (notif != null) {
                    javax.swing.JOptionPane.showMessageDialog(null, notif, "Información", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                }
                // refrescar vistas dependientes y volver al panelControl
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
