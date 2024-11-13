package co.urwallet.viewController;

import co.urwallet.controller.AsistenteControllers;
import co.urwallet.controller.HomeController;
import co.urwallet.controller.HomeUsersController;
import co.urwallet.model.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class PrincipalUserViewsControllers {
    private Usuario usuario;
    HomeUsersController home = new HomeUsersController();
    AsistenteControllers asistenteControllers = new AsistenteControllers();

    public void setUsuarioLogueado(Usuario usuarioLogueado) {
        home.setUsers(usuarioLogueado);
        asistenteControllers.setUsers(usuarioLogueado);
    }
}
