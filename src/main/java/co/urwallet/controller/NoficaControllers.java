package co.urwallet.controller;

import co.urwallet.controller.service.IModelFactoryControllerService;
import co.urwallet.mapping.dto.CuentaDto;
import co.urwallet.model.Usuario;
import co.urwallet.viewController.AsistenteUsersViewControllers;
import co.urwallet.viewController.NotificacionesViewsControllers;
import co.urwallet.viewController.SolicitudesAdmnViewControllers;

import java.util.List;

public class NoficaControllers {
    IModelFactoryControllerService modelFactoryService;

    public NoficaControllers(){
        modelFactoryService =ModelFactoryController.getInstance();
    }

    public void setUsers(Usuario user){
        NotificacionesViewsControllers.getInstance().setUsuarioLogueado(user);
        System.out.println("AQUIIIIIIIIIIII SI ENTRAAAAAAAAAAAAAA");
    }
    public List<CuentaDto> obtenerCuenta(){
        return modelFactoryService.obtenerCuentas();
    }
}
