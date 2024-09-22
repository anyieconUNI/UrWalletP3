package co.urwallet.controller.service;

import co.urwallet.mapping.dto.UsuarioDto;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

public interface IRegistroControllerService {
    boolean agregarRegistro(UsuarioDto registroDto);

    void mostrarMensaje(String mensaje, String titulo, Alert.AlertType tipo);

    void cerrarVentana(ActionEvent actionEvent);
}
