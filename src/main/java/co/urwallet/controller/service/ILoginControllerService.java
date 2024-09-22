package co.urwallet.controller.service;

import co.urwallet.exceptions.LoginException;
import co.urwallet.mapping.dto.LoginDto;
import co.urwallet.model.Usuario;
import javafx.event.ActionEvent;

public interface ILoginControllerService {
    Usuario inicioUser(LoginDto loginDto) throws LoginException;

    void cerrarVentana(ActionEvent actionEvent);
}
