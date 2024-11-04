package co.urwallet.controller;

import co.urwallet.controller.service.IModelFactoryControllerService;
import co.urwallet.controller.service.TrasaccionControllersService;
import co.urwallet.mapping.dto.TransaccionDto;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

public class TrasaccionControllers implements TrasaccionControllersService {
    IModelFactoryControllerService modelFactoryService;
    public TrasaccionControllers(){
        modelFactoryService =ModelFactoryController.getInstance();
    }
    @Override
    public boolean agregarTrasaccion(TransaccionDto transaccionDto){
        return modelFactoryService.agregarTrasaccion(transaccionDto);
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
