package com.biblioteca.vista.dialogo;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.RootPaneContainer;

import com.biblioteca.controlador.Controlador;
import com.biblioteca.dto.ObtenerPublicacionDetallesPorIdDTO;
import com.biblioteca.utilities.Fonts;

/**
 * Modal para editar una publicación (basado en `NuevaPublicacionDialog`).
 *
 * Presenta un pequeño wizard (datos comunes → datos por tipo) y rellena
 * los campos con los valores actuales de la publicación usando
 * `ControladorEditarPublicacionDialog`. Al guardar, delega en el controlador
 * la edición transaccional en la base de datos.
 */
public class EditarPublicacionDialog extends JDialog {
    /**
     * Controlador principal de la aplicación
     */
    private Controlador controlador;
    /**
     * Ventana padre (para el modal)
     */
    private JFrame parentFrame;
    /**
     * Panel con CardLayout para los pasos del wizard
     */
    private JPanel cardPanel;
    /**
     * CardLayout para navegar entre pasos
     */
    private CardLayout cardLayout;
    /**
     * Componente previo del glass pane (para restaurar al cerrar el diálogo)
     */
    private Component previousGlassPane;

    // Step 1 fields
    /**
     * Campos comunes del paso (isbn)
     */
    private JTextField isbnField;
    /**
     * Campos comunes del paso (título)
     */
    private JTextField tituloField;
    /**
     * Campos comunes del paso (idioma)
     */
    private JTextField idiomaField;
    /**
     * Campos comunes del paso (temas)
     */
    private JTextField temasField;
    /**
     * Campos comunes del paso (módulos)
     */
    private JTextField modulosField;
    /**
     * Campos comunes del paso (ciclos)
     */
    private JTextField ciclosField;
    /**
     * Campos comunes del paso (editorial)
     */
    private JTextField editorialField;
    /**
     * Campos comunes del paso (tipo de publicación)
     */
    private JComboBox<String> tipoCombo;

    // Step Libro
    /**
     * Campos del paso Libro (número de edición)
     */
    private JTextField numeroEdicionField;
    /**
     * Campos del paso Libro (fecha de publicación)
     */
    private JTextField fechaPublicacionField;
    /**
     * Campos del paso Libro (autores)
     */
    private JTextField autoresField;

    // Step Revista
    /**
     * Campos del paso Revista (periodicidad)
     */
    private JTextField periodicidadField;
    /**
     * Id de la publicación a editar
     */
    private int idPublicacion;

    /**
     * Crea el diálogo de edición para la publicación indicada.
     *
     * @param parent        ventana padre para el modal
     * @param controlador   controlador principal de la aplicación
     * @param idPublicacion id de la publicación a editar
     */
    public EditarPublicacionDialog(final JFrame parent, final Controlador controlador, final int idPublicacion) {
        super(parent, "Editar Publicación", true);
        this.controlador = controlador;
        this.parentFrame = parent;
        this.idPublicacion = idPublicacion;
        initUI();
        setSize(new Dimension(340, 500));
        setLocationRelativeTo(parent);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(final WindowEvent e) {
                removeOverlay();
            }

            @Override
            public void windowClosing(final WindowEvent e) {
                removeOverlay();
            }
        });

        // Prefill fields from DB
        cargarDatos();
    }

    /**
     * Inicializa la interfaz del diálogo
     */
    private void initUI() {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        JPanel paso1 = crearPaso1();
        JPanel pasoLibro = crearPasoLibro();
        JPanel pasoRevista = crearPasoRevista();

        cardPanel.add(paso1, "paso1");
        cardPanel.add(pasoLibro, "libro");
        cardPanel.add(pasoRevista, "revista");

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(cardPanel, BorderLayout.CENTER);
    }

    /**
     * Configura un campo de texto con los estilos por defecto
     */
    @Override
    public void setVisible(final boolean visible) {
        if (visible) {
            installOverlay();
        }
        super.setVisible(visible);
        if (!visible) {
            removeOverlay();
        }
    }

    /**
     * Carga desde la base de datos los detalles de la publicación y rellena los
     * campos
     * del formulario. El array devuelto por el DAO tiene el siguiente orden:
     * tipo, titulo, codigo_isbn, idioma, temasCSV, modulosCSV, ciclosCSV,
     * editorial,
     * num_edicion, fecha_publicacion, autoresCSV, periodicidad, id
     */
    private void cargarDatos() {
        try {
            ObtenerPublicacionDetallesPorIdDTO datos = controlador.getControladorEditarPublicacionDialog()
                    .obtenerDetallesPublicacion(idPublicacion);
            if (datos == null) {
                return;
            }
            // datos: tipo, titulo, codigo_isbn, idioma, temasCSV, modulosCSV, ciclosCSV,
            // editorial, num_edicion, fecha_publicacion, autoresCSV, periodicidad, id
            String tipo = datos.getTipoPublicacion() != null ? datos.getTipoPublicacion().name() : "";
            String titulo = datos.getTitulo() != null ? datos.getTitulo() : "";
            String isbn = datos.getCodigoISBN() != null ? datos.getCodigoISBN() : "";
            String idioma = datos.getIdioma() != null ? datos.getIdioma() : "";
            String temas = datos.getTemas() != null ? datos.getTemas() : "";
            String modulos = datos.getModulos() != null ? datos.getModulos() : "";
            String ciclos = datos.getCiclos() != null ? datos.getCiclos() : "";
            String editorial = datos.getEditorial() != null ? datos.getEditorial() : "";
            String numEd = datos.getNumEdicion() != null ? datos.getNumEdicion() : "";
            String fecha = datos.getFechaPublicacion() != null ? datos.getFechaPublicacion() : "";
            String autores = datos.getAutores() != null ? datos.getAutores() : "";
            String periodicidad = datos.getPeriodicidad() != null ? datos.getPeriodicidad() : "";

            tituloField.setText(titulo);
            isbnField.setText(isbn);
            idiomaField.setText(idioma);
            temasField.setText(temas);
            modulosField.setText(modulos);
            ciclosField.setText(ciclos);
            editorialField.setText(editorial);

            // seleccionar tipo y rellenar campos específicos, pero mantener el
            // diálogo en el paso 1 para que el usuario vea primero los datos comunes.
            if ("L".equalsIgnoreCase(tipo)) {
                tipoCombo.setSelectedItem("Libro");
                // Rellenar campos de libro (no mostramos la tarjeta aún)
                numeroEdicionField.setText(numEd == null ? "" : numEd);
                fechaPublicacionField.setText(fecha == null ? "" : fecha);
                autoresField.setText(autores == null ? "" : autores);
            } else {
                tipoCombo.setSelectedItem("Revista");
                // Rellenar campos de revista (no mostramos la tarjeta aún)
                periodicidadField.setText(periodicidad == null ? "" : periodicidad);
            }
            // Mostrar paso 1 por defecto al abrir el modal
            cardLayout.show(cardPanel, "paso1");

        } catch (Exception e) {
            System.out.println("Error cargando datos: " + e.getMessage());
        }
    }

    /**
     * Instala un overlay semitransparente en la ventana padre para indicar
     * que está bloqueada mientras el diálogo está abierto.
     */
    private void installOverlay() {
        if (parentFrame == null) {
            return;
        }
        try {
            RootPaneContainer rpc = (RootPaneContainer) parentFrame;
            Component current = rpc.getRootPane().getGlassPane();
            previousGlassPane = current;

            JPanel overlay = new JPanel();
            overlay.setOpaque(true);
            overlay.setBackground(new Color(217, 217, 217, 153));
            overlay.addMouseListener(new MouseAdapter() {
            });

            rpc.getRootPane().setGlassPane(overlay);
            overlay.setVisible(true);
        } catch (Exception e) {
            System.out.println("No se pudo instalar overlay: " + e.getMessage());
        }
    }

    /**
     * Quita el overlay de la ventana padre al cerrar el diálogo.
     */
    private void removeOverlay() {
        if (parentFrame == null) {
            return;
        }
        try {
            RootPaneContainer rpc = (RootPaneContainer) parentFrame;
            if (previousGlassPane != null) {
                rpc.getRootPane().setGlassPane(previousGlassPane);
                previousGlassPane.setVisible(false);
                previousGlassPane = null;
            } else {
                rpc.getRootPane().getGlassPane().setVisible(false);
            }
        } catch (Exception e) {
            System.out.println("No se pudo quitar overlay: " + e.getMessage());
        }
    }

    /**
     * Crea el panel del primer paso del wizard (datos comunes)
     * 
     * @return panel del wizard paso 1
     */
    private JPanel crearPaso1() {
        // Crear panel para paso 1
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.white);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 12, 8, 12);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;

        // Título
        JLabel title = new JLabel("Editar Publicación");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        panel.add(title, c);

        // Campos comunes
        // isbn
        c.gridwidth = 1;
        c.gridy++;
        panel.add(new JLabel("ISBN"), c);
        isbnField = new JTextField();
        configurarCampo(isbnField);
        c.gridx = 1;
        panel.add(isbnField, c);

        // titulo
        c.gridx = 0;
        c.gridy++;
        panel.add(new JLabel("Titulo"), c);
        tituloField = new JTextField();
        configurarCampo(tituloField);
        c.gridx = 1;
        panel.add(tituloField, c);

        // idioma
        c.gridx = 0;
        c.gridy++;
        panel.add(new JLabel("Idioma"), c);
        idiomaField = new JTextField();
        configurarCampo(idiomaField);
        c.gridx = 1;
        panel.add(idiomaField, c);

        // temas
        c.gridx = 0;
        c.gridy++;
        panel.add(new JLabel("Temas"), c);
        temasField = new JTextField();
        configurarCampo(temasField);
        c.gridx = 1;
        panel.add(temasField, c);

        // modulos
        c.gridx = 0;
        c.gridy++;
        panel.add(new JLabel("Modulos"), c);
        modulosField = new JTextField();
        configurarCampo(modulosField);
        c.gridx = 1;
        panel.add(modulosField, c);

        // ciclos
        c.gridx = 0;
        c.gridy++;
        panel.add(new JLabel("Ciclos"), c);
        ciclosField = new JTextField();
        configurarCampo(ciclosField);
        c.gridx = 1;
        panel.add(ciclosField, c);

        // editorial
        c.gridx = 0;
        c.gridy++;
        panel.add(new JLabel("Editorial"), c);
        editorialField = new JTextField();
        configurarCampo(editorialField);
        c.gridx = 1;
        panel.add(editorialField, c);

        // tipo
        c.gridx = 0;
        c.gridy++;
        panel.add(new JLabel("Tipo"), c);
        tipoCombo = new JComboBox<>();
        tipoCombo.addItem("Libro");
        tipoCombo.addItem("Revista");
        c.gridx = 1;
        panel.add(tipoCombo, c);

        // Botón Siguiente
        JButton siguiente = new JButton("Siguiente");
        siguiente.setBackground(Color.decode("#F4791B"));
        siguiente.setForeground(Color.white);
        siguiente.setBorder(null);
        c.gridx = 1;
        c.gridy++;
        c.anchor = GridBagConstraints.EAST;
        panel.add(siguiente, c);

        // Acción botón Siguiente
        siguiente.addActionListener(e -> {
            String tipo = (String) tipoCombo.getSelectedItem();
            if ("Libro".equals(tipo)) {
                cardLayout.show(cardPanel, "libro");
            } else {
                cardLayout.show(cardPanel, "revista");
            }
        });

        return panel;
    }

    /**
     * Crea el panel del paso Libro del wizard
     * 
     * @return panel del paso de libro
     */
    private JPanel crearPasoLibro() {
        // Crear panel para paso Libro
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.white);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 12, 8, 12);
        c.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel title = new JLabel("Editar Publicación (Libro)");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        panel.add(title, c);

        // Campos específicos de libro
        // Número de edición
        c.gridwidth = 1;
        c.gridy++;
        panel.add(new JLabel("Numero edición"), c);
        numeroEdicionField = new JTextField();
        configurarCampo(numeroEdicionField);
        c.gridx = 1;
        panel.add(numeroEdicionField, c);

        // Fecha de publicación
        c.gridx = 0;
        c.gridy++;
        panel.add(new JLabel("Fecha publicacion"), c);
        fechaPublicacionField = new JTextField();
        configurarCampo(fechaPublicacionField);
        c.gridx = 1;
        panel.add(fechaPublicacionField, c);

        // Autores
        c.gridx = 0;
        c.gridy++;
        panel.add(new JLabel("Autores"), c);
        autoresField = new JTextField();
        configurarCampo(autoresField);
        c.gridx = 1;
        panel.add(autoresField, c);

        // Botones Guardar y Volver
        JButton guardar = new JButton("Guardar");
        guardar.setBackground(Color.decode("#F4791B"));
        guardar.setForeground(Color.white);
        guardar.setBorder(null);
        c.gridx = 1;
        c.gridy++;
        c.anchor = GridBagConstraints.EAST;
        panel.add(guardar, c);

        // Acción botón Guardar
        guardar.addActionListener(e -> {
            // Validar y guardar cambios
            if (!validarPasoLibro()) {
                return;
            }
            try {
                // llamar al controlador para actualizar la publicación
                boolean ok = controlador.getControladorEditarPublicacionDialog().editarPublicacionLibro(
                        idPublicacion,
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
                // devuelve true si se actualizó correctamente
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Publicación actualizada", "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                    controlador.getControladorNavegacion().refrescarPublicaciones();
                    controlador.getControladorNavegacion().refrescarPanelControl();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Error actualizando la publicación en la base de datos",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                // cerrar diálogo
            } catch (Exception ex) {
                // mostrar error
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Volver
        JButton volver = new JButton("Volver");
        volver.setBackground(Color.white);
        volver.setForeground(Color.decode("#000000"));
        c.gridx = 0;
        panel.add(volver, c);
        volver.addActionListener(e -> cardLayout.show(cardPanel, "paso1"));

        return panel;
    }

    /**
     * Crea el panel del paso Revista del wizard
     * 
     * @return panel del paso de Revista
     */
    private JPanel crearPasoRevista() {
        // Crear panel para paso Revista
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.white);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 12, 8, 12);
        c.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel title = new JLabel("Editar Publicación (Revista)");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        panel.add(title, c);

        // Campos específicos de revista
        // Periodicidad
        c.gridwidth = 1;
        c.gridy++;
        panel.add(new JLabel("Periodicidad"), c);
        periodicidadField = new JTextField();
        configurarCampo(periodicidadField);
        c.gridx = 1;
        panel.add(periodicidadField, c);

        // Botones Guardar y Volver
        JButton guardar = new JButton("Guardar");
        guardar.setBackground(Color.decode("#F4791B"));
        guardar.setForeground(Color.white);
        guardar.setBorder(null);
        c.gridx = 1;
        c.gridy++;
        c.anchor = GridBagConstraints.EAST;
        panel.add(guardar, c);

        // Acción botón Guardar
        guardar.addActionListener(e -> {
            // Validar y guardar cambios
            if (!validarPasoRevista()) {
                return;
            }
            try {
                // llamar al controlador para actualizar la publicación
                boolean ok = controlador.getControladorEditarPublicacionDialog().editarPublicacionRevista(
                        idPublicacion,
                        isbnField.getText().trim(),
                        tituloField.getText().trim(),
                        idiomaField.getText().trim(),
                        temasField.getText().trim(),
                        modulosField.getText().trim(),
                        ciclosField.getText().trim(),
                        editorialField.getText().trim(),
                        periodicidadField.getText().trim());
                // devuelve true si se actualizó correctamente
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Publicación actualizada", "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                    controlador.getControladorNavegacion().refrescarPublicaciones();
                    controlador.getControladorNavegacion().refrescarPanelControl();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Error actualizando la publicación en la base de datos",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                // mostrar error
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Volver
        JButton volver = new JButton("Volver");
        volver.setBackground(Color.white);
        volver.setForeground(Color.decode("#000000"));
        c.gridx = 0;
        panel.add(volver, c);
        volver.addActionListener(e -> cardLayout.show(cardPanel, "paso1"));

        return panel;
    }

    /**
     * Valida los campos comunes (paso 1) usados tanto para creación como para
     * edición.
     * 
     * @return true si campos obligatorios están presentes
     */
    private boolean validarPasoComun() {
        if (tituloField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El título es requerido", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (isbnField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El ISBN es requerido", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (editorialField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La editorial es requerida", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (idiomaField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El idioma es requerido", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (tipoCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Seleccione el tipo de publicación", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Valida los campos del paso Libro
     * 
     * @return true si los campos están válidos, false en caso contrario
     */
    private boolean validarPasoLibro() {
        if (!validarPasoComun()) {
            return false;
        }
        String numEd = numeroEdicionField.getText().trim();
        if (numEd.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El número de edición es requerido", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            int n = Integer.parseInt(numEd);
            if (n <= 0) {
                JOptionPane.showMessageDialog(this, "El número de edición debe ser mayor que 0", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Número de edición inválido", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        String fecha = fechaPublicacionField.getText().trim();
        if (fecha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La fecha de publicación es requerida (YYYY-MM-DD)", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            LocalDate.parse(fecha);
        } catch (java.time.format.DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use YYYY-MM-DD", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Valida los campos del paso Revista
     * 
     * @return true si los campos están válidos, false en caso contrario
     */
    private boolean validarPasoRevista() {
        if (!validarPasoComun()) {
            return false;
        }
        String per = periodicidadField.getText().trim();
        if (per.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La periodicidad es requerida", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Aplica estilo y fuente a los campos de texto (coincide con el estilo usado
     * en `NuevaPublicacionDialog` para consistencia visual).
     * 
     * @param field campo de texto a configurar
     */
    private void configurarCampo(final JTextField field) {
        field.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.decode("#CCCCCC")),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)));
        field.setFont(Fonts.openSans(12f));
    }

}
