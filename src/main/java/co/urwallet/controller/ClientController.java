package co.urwallet.controller;

import co.urwallet.mapping.dto.CuentaDto;
import co.urwallet.mapping.dto.TransaccionDto;
import co.urwallet.mapping.dto.UsuarioDto;
import co.urwallet.model.Cuenta;
import co.urwallet.model.Transaccion;
import co.urwallet.model.Usuario;
import co.urwallet.viewController.*;
import javafx.application.Platform;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

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

    public void sendMessageNotify(String message) {
        try {
            if (outputStream != null) {
                outputStream.writeObject(message);
                outputStream.flush();
                System.out.println("Mensaje enviado al servidor: " + message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void sendCuenta(Cuenta cuenta) {
        try {
            if (outputStream != null) {
                outputStream.writeObject(cuenta);
                outputStream.flush();
                System.out.println("Cuenta enviada al servidor: " + cuenta);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void sendUsuario(Usuario usuario) {
        try {
            if (outputStream != null) {
                outputStream.writeObject(usuario);
                outputStream.flush();
                System.out.println("Cuenta enviada al servidor: " + usuario);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listenForMessages() {
        try (ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream())) {
            Object message;

            while ((message = inputStream.readObject()) != null) {
                if (message instanceof String) {
                    String receivedMessage = (String) message;
                    System.out.println("Mensaje recibido del servidor: " + receivedMessage);

                    // Actualizar las vistas con el mensaje recibido
                    Platform.runLater(() -> {
                        SolicitudesAdmnViewControllers solicitudesAdmnViewControllers = SolicitudesAdmnViewControllers.getInstance();
                        if (solicitudesAdmnViewControllers != null) {
                            solicitudesAdmnViewControllers.recibirSolicitud(receivedMessage);
                        }
                    });
                }
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

                        TransferenciasViewsControllers adminViewController = TransferenciasViewsControllers.getInstance();
                        if (adminViewController != null) {
                            adminViewController.actualizarTablaTransacciones(transaccionDto);
                        }

                        NotificacionesViewsControllers notificacionesController = NotificacionesViewsControllers.getInstance();
                        if (notificacionesController != null) {
                            notificacionesController.actualizarNoti(transaccionDto);
                        }
                    });

                    System.out.println("Transferencia recibida del servidor: " + transaccion);
                }
                if (message instanceof Cuenta) {
                    Cuenta cuenta= (Cuenta) message;
                    CuentaDto cuentaDto = convertirCuentaADto(cuenta);

                    // Agregar la transacción al modelo y actualizar la vista
                    Platform.runLater(() -> {
                        ModelFactoryController.getInstance().agregarCuenta(cuentaDto,true);
                        ModelFactoryController.getInstance().cargarDatosDesdeArchivos();
                        SaldoClienteViewsControllers saldo = SaldoClienteViewsControllers.getInstance();
                        if (saldo != null) {
                            saldo.actualizarTablaCuentasUser(cuentaDto);
                        }

                        NotificacionesViewsControllers notificacionesController = NotificacionesViewsControllers.getInstance();
                        if (notificacionesController != null) {
                            notificacionesController.actualizarNoti(cuentaDto);
                        }
                    });

                }
                if (message instanceof Usuario) {
                    Usuario usuario= (Usuario) message;
                    UsuarioDto usuarioDto = convertirUsuarioADto(usuario);

                    // Agregar la transacción al modelo y actualizar la vista
                    Platform.runLater(() -> {
                        ModelFactoryController.getInstance().agregarUsersSocket(usuarioDto,true);

                        UsersViewsController user = UsersViewsController.getInstance();
                        if (user != null) {
                            user.actualizarTablasUser(usuario);
                        }

//                        NotificacionesViewsControllers notificacionesController = NotificacionesViewsControllers.getInstance();
//                        if (notificacionesController != null) {
//                            notificacionesController.actualizarNoti(cuentaDto);
//                        }
                    });

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
    private CuentaDto convertirCuentaADto(Cuenta cuenta) {
        return new CuentaDto(
                cuenta.getIdCuenta(),
                cuenta.getNumeCuenta(),
                cuenta.getNombreCuenta(),
                cuenta.getTipoCuenta().toString(),
                cuenta.getSaldo(),
                ""
        );
    }
    private UsuarioDto convertirUsuarioADto(Usuario usuario) {
        return new UsuarioDto(
                usuario.getIdUsuario(),
                usuario.getCedula(),
                usuario.getNombreCompleto(),
                usuario.getTelefono(),
                usuario.getCorreo(),
                usuario.getContrasena(),
                usuario.getDireccion(),
                usuario.getSaldoDispo(),
                usuario.getCuentasBancarias()
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
