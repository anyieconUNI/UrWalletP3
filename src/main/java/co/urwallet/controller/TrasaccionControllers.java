package co.urwallet.controller;

import co.urwallet.controller.service.IModelFactoryControllerService;
import co.urwallet.controller.service.TrasaccionControllersService;
import co.urwallet.mapping.dto.CuentaDto;
import co.urwallet.mapping.dto.TransaccionDto;
import co.urwallet.model.Cuenta;
import co.urwallet.model.Usuario;
import co.urwallet.viewController.AsistenteUsersViewControllers;
import co.urwallet.viewController.TransferenciasUsersViewsControllers;
import co.urwallet.viewController.TransferenciasViewsControllers;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

import java.util.List;

public class TrasaccionControllers implements TrasaccionControllersService {
    IModelFactoryControllerService modelFactoryService;

    public TrasaccionControllers() {
        modelFactoryService = ModelFactoryController.getInstance();
    }

    CuentaControllers cuentas = new CuentaControllers();

    @Override
    public boolean agregarTrasaccion(TransaccionDto transaccionDto) {
        return modelFactoryService.agregarTrasaccion(transaccionDto);
    }

    public List<TransaccionDto> obtenerTrasaccion() {
        return modelFactoryService.obtenerTrasaccion();
    }
    @Override
    public Cuenta obtenerbyid(String Id){
        return modelFactoryService.obtenerCuentaPorId(Id);
    }
    @Override
    public List<CuentaDto> obtenerCuentas() {
        List<CuentaDto> cuentasDos = cuentas.obtenerCuenta();
        System.out.println("Clientes : " + cuentasDos);
        TransferenciasViewsControllers.getInstance().cargarCuentas();
        return cuentasDos;
    }
    @Override
    public void SumarSaldo(TransaccionDto transaccionDto){
        modelFactoryService.SumarSaldo(transaccionDto);
    }
    @Override
    public void RestarrSaldo(TransaccionDto transaccionDto){
        modelFactoryService.RestarSaldo(transaccionDto);
    }
    @Override
    public void mostrarMensaje(String mensaje, String titulo, Alert.AlertType tipo) {
        modelFactoryService.mostrarMensaje(mensaje, titulo, tipo);
    }

    @Override
    public void cerrarVentana(ActionEvent actionEvent) {
        modelFactoryService.cerrarVentana(actionEvent);
    }

    public void setUsers(Usuario user) {
        TransferenciasUsersViewsControllers.getInstance().setUsuarioLogueado(user);
    }
}
