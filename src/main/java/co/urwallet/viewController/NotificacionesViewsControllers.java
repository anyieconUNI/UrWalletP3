package co.urwallet.viewController;

import co.urwallet.controller.HomeUsersController;
import co.urwallet.controller.NoficaControllers;
import co.urwallet.mapping.dto.TransaccionDto;
import co.urwallet.model.Cuenta;
import co.urwallet.model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class NotificacionesViewsControllers {
    private Usuario usuario;
    NoficaControllers noficaControllers;
    private static NotificacionesViewsControllers instance;
    public NotificacionesViewsControllers() {
        if (instance == null) {
            instance = this;
        }
    }
    public static NotificacionesViewsControllers getInstance() {
        return instance;
    }
    @FXML
    public TextArea NotiArea;
    @FXML
    public void initialize() {
        noficaControllers = new NoficaControllers();
    }

    public void setUsuarioLogueado(Usuario user) {
        this.usuario = user;
    }

    public void actualizarNoti(Object objeto) {
        if (usuario != null && objeto != null) {
            if (objeto instanceof TransaccionDto) {
                TransaccionDto transaccionDto = (TransaccionDto) objeto;
                boolean esCuentaDestinoDelUsuario = usuario.getCuentasBancarias().stream()
                        .anyMatch(cuenta -> cuenta.getNumeCuenta().equals(transaccionDto.cuentaDestino().getNumeCuenta()));
                if (esCuentaDestinoDelUsuario) {
                    String mensaje = String.format(
                            "Ha recibido una transferencia:\n - Monto: %.2f\n - Descripción: %s\n",
                            transaccionDto.monto(),
                            transaccionDto.descripcion()
                    );
                    NotiArea.appendText(mensaje);
                }
            }
            if (objeto instanceof Cuenta) {
                Cuenta cuenta = (Cuenta) objeto;
                String mensaje = String.format(
                        "Se ha actualizado una cuenta:\n - Número: %s\n - Banco: %s\n - Saldo: %.2f\n",
                        cuenta.getNumeCuenta(),
                        cuenta.getNombreCuenta(),
                        cuenta.getSaldo()
                );
                NotiArea.appendText(mensaje);
            }
//            if (objeto instanceof String) {
//                String mensaje = (String) objeto;
//                NotiArea.appendText("Notificación: " + mensaje + "\n");
//            } else {
//                NotiArea.appendText("Tipo de objeto no reconocido.\n");
//            }
        }
    }
}
