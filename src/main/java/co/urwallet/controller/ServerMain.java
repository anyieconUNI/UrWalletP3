package co.urwallet.controller;

import co.urwallet.model.Transaccion;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerMain {
    private static final int PORT = 12345;
    private static final CopyOnWriteArrayList<ObjectOutputStream> clients = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor iniciado en el puerto " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress());

                // Crear flujo de salida para este cliente
                ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                clients.add(outputStream);

                // Manejar cliente en un hilo separado
                new Thread(() -> handleClient(clientSocket, outputStream)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket, ObjectOutputStream outputStream) {
        try (ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream())) {
            Object message;

            while ((message = inputStream.readObject()) != null) {
                if (message instanceof Transaccion) {
                    Transaccion transaccion = (Transaccion) message;
                    System.out.println("Transferencia recibida: " + transaccion);

                    // Retransmitir a todos los clientes
                    broadcastTransaction(transaccion, outputStream);
                }
            }
        } catch (Exception e) {
            System.out.println("Cliente desconectado: " + clientSocket.getInetAddress());
        } finally {
            clients.remove(outputStream);
            try {
                clientSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void broadcastTransaction(Transaccion transaccion, ObjectOutputStream sender) {
        for (ObjectOutputStream client : clients) {
            if (!client.equals(sender)) {
                try {
                    client.writeObject(transaccion);
                    client.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
