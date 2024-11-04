package co.urwallet.controller.service;

import co.urwallet.mapping.dto.TransaccionDto;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

public interface TrasaccionControllersService {
    boolean agregarTrasaccion(TransaccionDto transaccionDto);

    void mostrarMensaje(String mensaje, String titulo, Alert.AlertType tipo);

    void cerrarVentana(ActionEvent actionEvent);
}
