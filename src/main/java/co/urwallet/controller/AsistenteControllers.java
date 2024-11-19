package co.urwallet.controller;

import co.urwallet.controller.service.IModelFactoryControllerService;
import co.urwallet.mapping.dto.CuentaDto;
import co.urwallet.mapping.dto.TransaccionDto;
import co.urwallet.model.Cuenta;
import co.urwallet.model.Usuario;
import co.urwallet.viewController.AsistenteUsersViewControllers;
import co.urwallet.viewController.HomeViewsUsers;

import java.util.List;

public class AsistenteControllers {
    IModelFactoryControllerService modelFactoryService;

    public AsistenteControllers(){
        modelFactoryService =ModelFactoryController.getInstance();
    }

    public void setUsers(Usuario user){
        AsistenteUsersViewControllers.getInstance().setUsuarioLogueado(user);
        System.out.println("AQUIIIIIIIIIIII SI ENTRAAAAAAAAAAAAAA");
    }
    public List<CuentaDto> obtenerCuenta(){
        return modelFactoryService.obtenerCuentas();
    }
    public List<TransaccionDto> obtenerTrasaccion() {
        return modelFactoryService.obtenerTrasaccion();
    }
}
