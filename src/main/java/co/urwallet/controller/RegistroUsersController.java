package co.urwallet.controller;

import co.urwallet.controller.service.IModelFactoryControllerService;
import co.urwallet.controller.service.IRegistroControllerService;
import co.urwallet.mapping.dto.UsuarioDto;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

public class RegistroUsersController implements IRegistroControllerService {
    IModelFactoryControllerService modelFactoryService;

    public RegistroUsersController(){
        modelFactoryService =ModelFactoryController.getInstance();
    }
    @Override
    public boolean agregarRegistro(UsuarioDto registroDto){
        return modelFactoryService.agregarUsers(registroDto);
    }

    @Override
    public void mostrarMensaje(String mensaje, String titulo, Alert.AlertType tipo) {
        modelFactoryService.mostrarMensaje(mensaje, titulo, tipo);
    }
    @Override
    public void cerrarVentana(ActionEvent actionEvent) {
        modelFactoryService.cerrarVentana(actionEvent);
    }
}
