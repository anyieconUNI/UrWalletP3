package co.urwallet.controller;

import co.urwallet.controller.service.IModelFactoryControllerService;
import co.urwallet.model.Usuario;
import co.urwallet.viewController.AsistenteUsersViewControllers;
import co.urwallet.viewController.NotificacionesViewsControllers;

public class NoficaControllers {
    IModelFactoryControllerService modelFactoryService;

    public NoficaControllers(){
        modelFactoryService =ModelFactoryController.getInstance();
    }

    public void setUsers(Usuario user){
        NotificacionesViewsControllers.getInstance().setUsuarioLogueado(user);
        System.out.println("AQUIIIIIIIIIIII SI ENTRAAAAAAAAAAAAAA");
    }
}
