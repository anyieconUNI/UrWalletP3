package co.urwallet.controller.service;

import co.urwallet.exceptions.LoginException;
import co.urwallet.mapping.dto.LoginDto;
import co.urwallet.mapping.dto.UsuarioDto;
import co.urwallet.model.Usuario;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

import java.util.List;

public interface IModelFactoryControllerService {

    List<UsuarioDto> obtenerUser();

    boolean agregarUsers(UsuarioDto usuarioDto);


    boolean actualizarUser(String idUser, UsuarioDto usuarioDto);

    boolean eliminarUsuario(String idUser);

    void mostrarMensaje(String titulo, String mensaje, Alert.AlertType tipo);
    Usuario iniciarSesion(LoginDto loginDto) throws LoginException;

    void cerrarVentana(ActionEvent actionEvent);
}
