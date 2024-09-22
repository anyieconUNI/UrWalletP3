package co.urwallet.controller;

import co.urwallet.controller.service.IHomeUsersControlleSevice;
import co.urwallet.controller.service.IModelFactoryControllerService;
import co.urwallet.mapping.dto.UsuarioDto;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

public class HomeUsersController implements IHomeUsersControlleSevice {
    IModelFactoryControllerService modelFactoryService;

    public HomeUsersController(){
        modelFactoryService =ModelFactoryController.getInstance();
    }
    @Override
    public boolean actualizarUser(String idUser, UsuarioDto usuarioDto) {
        return modelFactoryService.actualizarUser(idUser,usuarioDto);
    }
    @Override
    public boolean eliminarUser(String idUser){
        return modelFactoryService.eliminarUsuario(idUser);
    }
    @Override
    public void cerrarVentana(ActionEvent actionEvent) {
        modelFactoryService.cerrarVentana(actionEvent);
    }
    @Override
    public void mostrarMensaje(String mensaje, String titulo, Alert.AlertType tipo) {
        modelFactoryService.mostrarMensaje(mensaje, titulo, tipo);
    }
}
