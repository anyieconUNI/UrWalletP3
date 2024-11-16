package co.urwallet.viewController;

import co.urwallet.controller.HomeUsersController;
import co.urwallet.controller.NoficaControllers;
import co.urwallet.mapping.dto.TransaccionDto;
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

    public void actualizarNoti(TransaccionDto transaccionDto) {
        if (usuario != null && transaccionDto != null) {
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

    }
}
