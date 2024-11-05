package co.urwallet.controller.service;

import co.urwallet.mapping.dto.CuentaDto;
import co.urwallet.mapping.dto.UsuarioDto;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

import java.util.List;

public interface AsignarCuentaUserService {
    List<CuentaDto> obtenerCuentas();

    List<UsuarioDto> obtenerUsuario();

    void mostrarMensaje(String mensaje, String titulo, Alert.AlertType tipo);

    void cerrarVentana(ActionEvent actionEvent);
}
