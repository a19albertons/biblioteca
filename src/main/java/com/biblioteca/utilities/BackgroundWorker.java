package com.biblioteca.utilities;

import javax.swing.SwingWorker;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Pequeña utilidad para ejecutar tareas en background usando SwingWorker.
 * Uso:
 * BackgroundWorker.run(() -> heavyWork(), result -> updateUI(result), ex -> handle(ex));
 */
public class BackgroundWorker {

    /**
     * Ejecuta una tarea en background y maneja el resultado o error en el hilo de la UI.
     * @param <T> Tipo de resultado esperado
     * @param task Tarea a ejecutar en background
     * @param onDone Consumidor para manejar el resultado en el hilo de la UI
     * @param onError Consumidor para manejar errores en el hilo de la UI
     * @return El SwingWorker creado y ejecutado
     */
    public static <T> SwingWorker<T, Void> run(Supplier<T> task, Consumer<T> onDone, Consumer<Exception> onError) {
        SwingWorker<T, Void> worker = new SwingWorker<>() {
            /**
             * Tarea que se ejecuta en background
             * @return
             * @throws Exception
             */
            @Override
            protected T doInBackground() throws Exception {
                return task.get();
            }

            /**
             * Maneja el resultado o error una vez que la tarea ha terminado
             */
            @Override
            protected void done() {
                try {
                    T result = get();
                    if (onDone != null) onDone.accept(result);
                } catch (Exception e) {
                    if (onError != null) onError.accept(e);
                }
            }
        };
        // Iniciar la ejecución del worker
        worker.execute();
        return worker;
    }

}
