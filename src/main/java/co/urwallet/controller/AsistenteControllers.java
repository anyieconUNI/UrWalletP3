package co.urwallet.controller;

import co.urwallet.controller.service.IModelFactoryControllerService;
import co.urwallet.model.Usuario;
import co.urwallet.viewController.AsistenteUsersViewControllers;
import co.urwallet.viewController.HomeViewsUsers;

public class AsistenteControllers {
    IModelFactoryControllerService modelFactoryService;

    public AsistenteControllers(){
        modelFactoryService =ModelFactoryController.getInstance();
    }

    public void setUsers(Usuario user){
        AsistenteUsersViewControllers.getInstance().setUsuarioLogueado(user);
        System.out.println("AQUIIIIIIIIIIII SI ENTRAAAAAAAAAAAAAA");
    }
}
