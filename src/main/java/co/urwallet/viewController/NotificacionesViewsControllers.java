package co.urwallet.viewController;

import co.urwallet.controller.HomeUsersController;
import co.urwallet.controller.NoficaControllers;
import co.urwallet.mapping.dto.CuentaDto;
import co.urwallet.mapping.dto.TransaccionDto;
import co.urwallet.model.Cuenta;
import co.urwallet.model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.util.List;

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
            List<CuentaDto> cuentasUsuario = noficaControllers.obtenerCuenta().stream()
                    .filter(cuenta -> cuenta.clienteId().equals(usuario.getCedula()))
                    .toList();

            if (objeto instanceof TransaccionDto) {
                TransaccionDto transaccionDto = (TransaccionDto) objeto;
                boolean esCuentaDestinoDelUsuario = cuentasUsuario.stream()
                        .anyMatch(cuenta -> cuenta.numeCuenta().equals(transaccionDto.cuentaDestino().getNumeCuenta()));

                if (esCuentaDestinoDelUsuario) {
                    String mensaje = String.format(
                            "Ha recibido una transferencia:\n - Monto: %.2f\n - Descripción: %s\n",
                            transaccionDto.monto(),
                            transaccionDto.descripcion()
                    );
                    NotiArea.appendText(mensaje);
                }
            } else if (objeto instanceof CuentaDto) {
                CuentaDto cuenta = (CuentaDto) objeto;
                String mensaje = String.format(
                        "Se ha actualizado una cuenta:\n - Número: %s\n - Banco: %s\n - Saldo: %.2f\n",
                        cuenta.numeCuenta(),
                        cuenta.nombreCuenta(),
                        cuenta.saldo()
                );
                NotiArea.appendText(mensaje);
            }
        }
    }
}
