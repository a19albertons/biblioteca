package com.biblioteca.utilities;

import java.sql.Connection;
import java.sql.SQLException;

import com.biblioteca.dao.CicloDAO;
import com.biblioteca.dao.ModuloDAO;
import com.biblioteca.dao.PublicacionDAO;
import com.biblioteca.dao.TemaDAO;

/**
 * Clase utilitaria para manejar transacciones de publicaciones.
 * Proporciona métodos para procesar relaciones comunes (modulos, ciclos, temas)
 * de forma transaccional, evitando duplicación de código en los controladores.
 */
public class PublicationTransactionHelper {

    /**
     * Procesa relaciones de una publicación (modulos, ciclos o temas).
     * Versión genérica que acepta cualquier DAO que implemente obtenerOCrear.
     *
     * @param conexion       conexión a la base de datos
     * @param publicacionDAO DAO para operaciones de publicación
     * @param dao            DAO para la relación específica
     * @param csv             lista CSV de elementos
     * @param idPublicacion   id de la publicación
     * @param tipoRelacion    tipo de relación: 'M' para modulos, 'C' para ciclos, 'T' para temas
     * @return true si se procesaron correctamente, false si hay error
     * @throws SQLException si ocurre un error en la base de datos
     */
    public boolean procesarRelaciones(final Connection conexion, final PublicacionDAO publicacionDAO,
            final Object dao, final String csv, final int idPublicacion, final RelacionPublicacionHelperEnum tipoRelacion) throws SQLException {
        if (csv == null || csv.trim().isEmpty()) {
            return true;
        }

        String[] items = csv.split(",");
        for (String item : items) {
            String nombre = item.trim();
            if (nombre.isEmpty()) {
                continue;
            }
            int id = obtenerID(dao, nombre);
            if (id == -1) {
                throw new SQLException("No se pudo crear el elemento: " + nombre + " (" + tipoRelacion + ")");
            }
            if (!publicacionDAO.insertarPublicacionRelacion(idPublicacion, id, tipoRelacion)) {
                throw new SQLException("No se pudo insertar la relación para " + nombre + " (" + tipoRelacion + ")");
            }
        }
        return true;
    }

    /**
     * Obtiene el ID de un elemento usando el DAO adecuado según el tipo de relación.
     * 
     * @param dao    DAO para la relación específica (ModuloDAO, CicloDAO, TemaDAO)
     * @param nombre nombre del elemento a obtener o crear
     * @return ID del elemento, o -1 si no se pudo obtener o crear
     */
    private int obtenerID(final Object dao, final String nombre) {
        if (dao instanceof ModuloDAO) {
            return ((ModuloDAO) dao).obtenerOCrear(nombre);
        } else if (dao instanceof CicloDAO) {
            return ((CicloDAO) dao).obtenerOCrear(nombre);
        } else if (dao instanceof TemaDAO) {
            return ((TemaDAO) dao).obtenerOCrear(nombre);
        }
        return -1;
    }
}
