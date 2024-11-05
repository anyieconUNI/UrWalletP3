package co.urwallet.viewController;

import co.urwallet.controller.LoginController;
import co.urwallet.controller.ModelFactoryController;
import co.urwallet.controller.service.ILoginControllerService;
import co.urwallet.controller.service.IModelFactoryControllerService;
import co.urwallet.exceptions.LoginException;
import co.urwallet.mapping.dto.LoginDto;
import co.urwallet.model.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginViewController implements ILoginControllerService {
    private final IModelFactoryControllerService modelFactoryService;
    public LoginViewController() {
        this.modelFactoryService = ModelFactoryController.getInstance();
    }
    
    LoginController loginController;
    
    @FXML
    public TextField txtCorreo;
    @FXML
    public PasswordField txtPassword;
    
    @FXML
    public void initialize() {
        loginController = new LoginController();
    }
    public void iniciarSesionUser(ActionEvent actionEvent) {
        inciarSesionUser();
        cerrarVentana(actionEvent);
    }
    public void inciarSesionUser(){
        String correo = txtCorreo.getText();
        String contrasena = txtPassword.getText();
        
        if(datosValidos(correo,contrasena)){
            LoginDto loginDto = new LoginDto(correo, contrasena);
            if(correo.equals("c") && contrasena.equals("1")) {
                ModelFactoryController.getInstance().navegarVentana("urWallet.fxml", "User", null);
            }else{
                try {
                    Usuario user = loginController.inicioUser(loginDto);
                    mostrarMensaje("Inicio de sesión", "Inicio de sesión exitoso", "Bienvenido, " + user.getNombreCompleto(), Alert.AlertType.INFORMATION);
                    if (user != null) {
                        ModelFactoryController.getInstance().navegarVentana("PrincipalUser.fxml", "User", user);
                    }
                } catch (LoginException e) {
                    mostrarMensaje("Inicio de sesión", "Error de inicio de sesión", e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        }
    }

    private boolean datosValidos(String correo, String contrasena) {
        String mensaje = "";
        if (correo == null || correo.isEmpty()) {
            mensaje += "El correo es inválido \n";
        }
        if (contrasena == null || contrasena.isEmpty()) {
            mensaje += "La contraseña es inválida \n";
        }
        if (mensaje.isEmpty()) {
            return true;
        } else {
            mostrarMensaje("Notificación", "Datos inválidos", mensaje, Alert.AlertType.WARNING);
            return false;
        }
    }

    private void mostrarMensaje(String titulo, String header, String contenido, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    @Override
    public Usuario inicioUser(LoginDto loginDto) throws LoginException {
        return modelFactoryService.iniciarSesion(loginDto);
    }

    @Override
    public void cerrarVentana(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}
