package co.urwallet.controller;


import co.urwallet.controller.service.IHomeController;

public class HomeController implements IHomeController {

    private final ModelFactoryController modelFactoryController;

    public HomeController(){
        this.modelFactoryController = ModelFactoryController.getInstance();
    }

    @Override
    public void iniciarSesion() {
        System.out.println("hola");
        modelFactoryController.navegarVentana("Login.fxml", "Login", null);
    }

    @Override
    public void registrarse() {
//        System.out.println("hola");
        modelFactoryController.navegarVentana("RegistroUsersViews.fxml", "Registro", null);
    }
}
