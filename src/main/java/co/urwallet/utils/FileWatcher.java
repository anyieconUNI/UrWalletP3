package co.urwallet.utils;

import java.io.IOException;
import java.nio.file.*;

public class FileWatcher {

    private static final String FILE_PATH = "src/main/resources/co/urwallet/Persistencia/archivos/Cuentas.txt";

    public static void startWatching(Runnable onFileChange) {
        Path path = Paths.get(FILE_PATH).getParent();

        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

            System.out.println("Monitoreando cambios en el archivo: " + FILE_PATH);

            new Thread(() -> {
                while (true) {
                    try {
                        WatchKey key = watchService.take();
                        for (WatchEvent<?> event : key.pollEvents()) {
                            if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
                                Path changed = (Path) event.context();
                                if (changed.endsWith(FILE_PATH.substring(FILE_PATH.lastIndexOf("/") + 1))) {
                                    System.out.println("El archivo ha cambiado, recargando datos...");
                                    onFileChange.run();
                                }
                            }
                        }
                        key.reset();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.err.println("El monitoreo de archivos se ha interrumpido.");
                        break;
                    }
                }
            }).start();
        } catch (IOException e) {
            System.err.println("Error al iniciar el monitoreo de archivos:");
            e.printStackTrace();
        }
    }
}
