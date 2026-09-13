package com.biblioteca.vista.dialogo;

import java.awt.Dimension;
import java.time.LocalDate;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.biblioteca.controlador.Controlador;
import com.biblioteca.dto.ObtenerPublicacionDetallesPorIdDTO;

/**
 * Modal para editar una publicación (basado en `NuevaPublicacionDialog`).
 *
 * Presenta un pequeño wizard (datos comunes → datos por tipo) y rellena
 * los campos con los valores actuales de la publicación usando
 * `ControladorEditarPublicacionDialog`. Al guardar, delega en el controlador
 * la edición transaccional en la base de datos.
 */
public class EditarPublicacionDialog extends BasePublicacionDialog {
    /**
     * Controlador principal de la aplicación
     */
    private Controlador controlador;

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
        super(parent, "Editar Publicación");
        this.controlador = controlador;
        this.idPublicacion = idPublicacion;
        super.initUI();
        confirmarLibro.setText("Guardar");
        confirmarRevista.setText("Guardar");
        configurarEventos();
        setSize(new Dimension(340, 500));
        setLocationRelativeTo(parent);

        // Prefill fields from DB
        cargarDatos();
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
    }

    public void configurarEventos() {
        // Acción botón Guardar
        confirmarLibro.addActionListener(e -> {
            // Validar y guardar cambios
            if (!validarPasoLibro()) {
                return;
            }
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
        });

        // Acción botón Guardar
        confirmarRevista.addActionListener(e -> {
            // Validar y guardar cambios
            if (!validarPasoRevista()) {
                return;
            }
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
        });
    }

}
