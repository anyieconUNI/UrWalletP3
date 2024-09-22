package co.urwallet.controller;

import co.urwallet.controller.service.ILoginControllerService;
import co.urwallet.exceptions.LoginException;
import co.urwallet.mapping.dto.LoginDto;
import co.urwallet.model.Usuario;
import javafx.event.ActionEvent;

public class LoginController implements ILoginControllerService {
    ModelFactoryController modelFactoryService;

    public LoginController() {
        modelFactoryService = ModelFactoryController.getInstance();
    }

    @Override
    public Usuario inicioUser(LoginDto loginDto) throws LoginException {
        return modelFactoryService.iniciarSesion(loginDto);
    }
    @Override
    public void cerrarVentana(ActionEvent actionEvent) {
        modelFactoryService.cerrarVentana(actionEvent);
    }
}