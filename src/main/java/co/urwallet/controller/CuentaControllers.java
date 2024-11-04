package co.urwallet.controller;

import co.urwallet.controller.service.CuentaControllersService;
import co.urwallet.controller.service.IModelFactoryControllerService;
import co.urwallet.mapping.dto.CuentaDto;
import co.urwallet.mapping.dto.UsuarioDto;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

import java.util.List;

public class CuentaControllers implements CuentaControllersService {
    IModelFactoryControllerService modelFactoryService;
    public CuentaControllers(){
        modelFactoryService =ModelFactoryController.getInstance();
    }
    public List<CuentaDto> obtenerCuenta(){
        return modelFactoryService.obtenerCuentas();
    }
    @Override
    public boolean agregarCuenta(CuentaDto cuentaDto){
        return modelFactoryService.agregarCuenta(cuentaDto);
    }
    @Override
    public boolean actualizarCuenta(String idCuenta, CuentaDto cuentaDto) {
        return modelFactoryService.actualizarCuenta(idCuenta,cuentaDto);
    }
    @Override
    public boolean eliminarCuenta(String idCuenta){
        return modelFactoryService.eliminarCuenta(idCuenta);
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

