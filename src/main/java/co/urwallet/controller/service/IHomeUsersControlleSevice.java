package co.urwallet.controller.service;

import co.urwallet.mapping.dto.UsuarioDto;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

public interface IHomeUsersControlleSevice {
    boolean actualizarUser(String idUser, UsuarioDto usuarioDto);

    boolean eliminarUser(String idUser);

    void cerrarVentana(ActionEvent actionEvent);

    void mostrarMensaje(String mensaje, String titulo, Alert.AlertType tipo);
}
