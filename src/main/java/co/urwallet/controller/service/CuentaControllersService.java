package co.urwallet.controller.service;

import co.urwallet.mapping.dto.CuentaDto;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

public interface CuentaControllersService {
    boolean agregarCuenta(CuentaDto cuentaDto);

    boolean actualizarCuenta(String idCuenta, CuentaDto cuentaDto);

    boolean eliminarCuenta(String idCuenta);

    void mostrarMensaje(String mensaje, String titulo, Alert.AlertType tipo);

    void cerrarVentana(ActionEvent actionEvent);
}
