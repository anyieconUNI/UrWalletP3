package co.urwallet.controller;

import co.urwallet.model.Cuenta;
import co.urwallet.model.Transaccion;
import co.urwallet.model.Usuario;

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

                if (message instanceof String) {
                    String receivedMessage = (String) message;
                    System.out.println("Mensaje recibido: " + receivedMessage);

                    // Retransmitir el mensaje a todos los clientes
                    broadcastMessage(receivedMessage, outputStream);
                }

                if (message instanceof Cuenta) {
                    Cuenta cuenta = (Cuenta) message;
                    System.out.println("Cuenta recibida: " + cuenta);

                    // Retransmitir la cuenta a todos los clientes
                    broadcastCuenta(cuenta, outputStream);
                }
                if (message instanceof Usuario) {
                    Usuario usuario= (Usuario) message;
                    System.out.println("usuario recibida: " + usuario);

                    broadcastUsuario(usuario, outputStream);
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

    private static void broadcastMessage(String message, ObjectOutputStream sender) {
        for (ObjectOutputStream client : clients) {
            if (!client.equals(sender)) {
                try {
                    client.writeObject(message);
                    client.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void broadcastCuenta(Cuenta cuenta, ObjectOutputStream sender) {
        for (ObjectOutputStream client : clients) {
            if (!client.equals(sender)) {
                try {
                    client.writeObject(cuenta);
                    client.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private static void broadcastUsuario(Usuario usuario, ObjectOutputStream sender) {
        for (ObjectOutputStream client : clients) {
            if (!client.equals(sender)) {
                try {
                    client.writeObject(usuario);
                    client.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
