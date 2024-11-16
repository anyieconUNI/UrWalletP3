package co.urwallet.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerMain {
    private static final int PORT = 12345;
    private static final CopyOnWriteArrayList<Socket> clients = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor iniciado en el puerto " + PORT);

            while (true) {
                // Esperar conexión de cliente
                Socket clientSocket = serverSocket.accept();
                clients.add(clientSocket);
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress());

                // Manejar cliente en un hilo separado
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            String message;

            while ((message = reader.readLine()) != null) {
                System.out.println("Mensaje recibido: " + message);

                // Retransmitir mensaje a todos los demás clientes
                broadcastMessage(message, clientSocket);
            }
        } catch (Exception e) {
            System.out.println("Cliente desconectado: " + clientSocket.getInetAddress());
        } finally {
            // Eliminar cliente desconectado
            clients.remove(clientSocket);
            try {
                clientSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void broadcastMessage(String message, Socket sender) {
        for (Socket client : clients) {
            if (!client.equals(sender)) {
                try {
                    PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
                    writer.println(message); // Enviar mensaje
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
