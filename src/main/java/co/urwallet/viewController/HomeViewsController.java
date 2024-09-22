package co.urwallet.viewController;


import co.urwallet.controller.HomeController;
import co.urwallet.model.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class HomeViewsController {

    private final HomeController homeController;

    public HomeViewsController(){
        homeController = new HomeController();
    }

    @FXML
    public void iniciarSesion(ActionEvent actionEvent) {
        homeController.iniciarSesion();
    }

    @FXML
    public void registrarse(ActionEvent actionEvent) {
        homeController.registrarse();
    }

}
