package co.urwallet.controller;

import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientController {
    private static final String SERVER_HOST = "localhost"; // Cambiar si el servidor está en otra IP
    private static final int SERVER_PORT = 12345;
    private Socket socket;
    private PrintWriter writer;

    public void connect() {
        try {
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            writer = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Conectado al servidor.");

            // Iniciar hilo para recibir mensajes del servidor
            new Thread(this::listenForMessages).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        if (writer != null) {
            writer.println(message);
            System.out.println("Mensaje enviado al servidor: " + message);
        }
    }

    private void listenForMessages() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String message;
            while ((message = reader.readLine()) != null) {
                System.out.println("Mensaje recibido del servidor: " + message);
                showAlert(message);
            }
        } catch (Exception e) {
            System.out.println("Conexión con el servidor cerrada.");
        }
    }
    private void showAlert(String message) {
        // Utiliza Platform.runLater porque JavaFX solo permite modificaciones de la UI en el hilo principal
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Mensaje del Servidor");
            alert.setHeaderText("Nuevo Mensaje Recibido");
            alert.setContentText(message);
            alert.showAndWait();
        });
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