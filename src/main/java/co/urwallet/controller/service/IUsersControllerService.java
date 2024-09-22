package co.urwallet.controller.service;

import co.urwallet.mapping.dto.UsuarioDto;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

public interface IUsersControllerService {

    boolean agregarUsuario(UsuarioDto registroDto);

    boolean actualizarUser(String idUser, UsuarioDto usuarioDto);

    boolean eliminarUser(String idUser);

    void mostrarMensaje(String mensaje, String titulo, Alert.AlertType tipo);

    void cerrarVentana(ActionEvent actionEvent);
}
