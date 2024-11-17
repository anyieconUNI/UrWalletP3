package co.urwallet.controller;

import co.urwallet.controller.service.IModelFactoryControllerService;
import co.urwallet.controller.service.IUsersControllerService;
import co.urwallet.mapping.dto.UsuarioDto;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

import java.util.List;

public class UsersController implements IUsersControllerService {
    IModelFactoryControllerService modelFactoryService;
    public UsersController(){
        modelFactoryService =ModelFactoryController.getInstance();
    }
    public List<UsuarioDto> obtenerUsers(){
        return modelFactoryService.obtenerUser();
    }
    @Override
    public boolean agregarUsuario(UsuarioDto registroDto){
        return modelFactoryService.agregarUsers(registroDto,false);
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
    public void mostrarMensaje(String mensaje, String titulo, Alert.AlertType tipo) {
        modelFactoryService.mostrarMensaje(mensaje, titulo, tipo);
    }
    @Override
    public void cerrarVentana(ActionEvent actionEvent) {
        modelFactoryService.cerrarVentana(actionEvent);
    }
}