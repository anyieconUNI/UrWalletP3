package co.urwallet.controller;

import co.urwallet.mapping.dto.TransaccionDto;
import co.urwallet.model.Transaccion;
import co.urwallet.viewController.NotificacionesViewsControllers;
import co.urwallet.viewController.TransferenciasUsersViewsControllers;
import co.urwallet.viewController.TransferenciasViewsControllers;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientController {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 12345;
    private Socket socket;
    private ObjectOutputStream outputStream;

    public void connect() {
        try {
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Conectado al servidor.");

            // Iniciar hilo para recibir mensajes
            new Thread(this::listenForMessages).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendTransaction(Transaccion transaccion) {
        try {
            if (outputStream != null) {
                outputStream.writeObject(transaccion);
                outputStream.flush();
                System.out.println("Transferencia enviada al servidor: " + transaccion);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listenForMessages() {
        try (ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream())) {
            Object message;

            while ((message = inputStream.readObject()) != null) {
                if (message instanceof Transaccion) {
                    Transaccion transaccion = (Transaccion) message;
                    TransaccionDto transaccionDto = convertirTransaccionADto(transaccion);

                    // Agregar la transacción al modelo y actualizar la vista
                    Platform.runLater(() -> {
                        ModelFactoryController.getInstance().agregarTrasaccionDeServidor(transaccionDto);


                        TransferenciasUsersViewsControllers viewController = TransferenciasUsersViewsControllers.getInstance();
                        if (viewController != null) {
                            viewController.actualizarTablaTransacciones(transaccionDto);
                        }
                        TransferenciasViewsControllers AsminviewController = TransferenciasViewsControllers.getInstance();
                        if (AsminviewController != null) {
                            AsminviewController.actualizarTablaTransacciones(transaccionDto);
                        }
                        NotificacionesViewsControllers notificacionesViewsControllers = NotificacionesViewsControllers.getInstance();
                        if (notificacionesViewsControllers != null) {
                            notificacionesViewsControllers.actualizarNoti(transaccionDto);
                        }
                    });

                    System.out.println("Transferencia recibida del servidor: " + transaccion);
                }
            }
        } catch (Exception e) {
            System.out.println("Conexión con el servidor cerrada.");
        }
    }
    private TransaccionDto convertirTransaccionADto(Transaccion transaccion) {
        return new TransaccionDto(
                transaccion.getIdTransaccion(),
                transaccion.getFecha(),
                transaccion.getTipoTransaccion(),
                transaccion.getMonto(),
                transaccion.getDescripcion(),
                transaccion.getCuentaOrigen(),
                transaccion.getCuentaDestino(),
                transaccion.getCategoria()
        );
    }

    public void disconnect() {
        try {
            if (socket != null) {
                socket.close();
            }
            System.out.println("Desconectado del servidor.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
