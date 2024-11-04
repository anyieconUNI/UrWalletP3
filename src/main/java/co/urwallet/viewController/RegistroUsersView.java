package co.urwallet.viewController;

import co.urwallet.controller.RegistroUsersController;
import co.urwallet.mapping.dto.UsuarioDto;
import co.urwallet.model.Cuenta;
import co.urwallet.model.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RegistroUsersView {
    RegistroUsersController registroUsersController;

    UsuarioDto registroUsers;
    @FXML
    public TextField txtIdentificacion;
    @FXML
    public TextField txtNombreCompleto;
    @FXML
    public TextField txtCorreo;
    @FXML
    public TextField txtTelefono;
    @FXML
    public PasswordField txtPassword;
    @FXML
    public TextField txtDireccion;

    @FXML
    public void initialize() {
        try {
            registroUsersController = new RegistroUsersController();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registrarUser(ActionEvent actionEvent) {
        RegistrarUser();
        registroUsersController.cerrarVentana(actionEvent);
    }
    private void RegistrarUser(){
        UsuarioDto registroUser =contruirRegistroAdmin();
        if(datosValidos(registroUser)){
            if(registroUsersController.agregarRegistro(registroUser)){
                registroUsersController.mostrarMensaje("El Usuario se ha creado con éxito", "Notificación Usuario", Alert.AlertType.INFORMATION);
                limpiarCamposUsuario();

            }
            else{
                registroUsersController.mostrarMensaje("Los datos ingresados son invalidos", "Usuario no creado", Alert.AlertType.ERROR);
            }
        }
        
    }
    private UsuarioDto contruirRegistroAdmin(){
        Float saldo = 0.0f;
        String id = UUID.randomUUID().toString();
        List<Cuenta> cuentasBancarias = new ArrayList<>();
        return new UsuarioDto(
                id,
                txtIdentificacion.getText(),
                txtNombreCompleto.getText(),
                txtTelefono.getText(),
                txtCorreo.getText(),
                txtPassword.getText(),
                txtDireccion.getText(),
                saldo,
                cuentasBancarias
        );
    }
    private boolean datosValidos(UsuarioDto registroDto) {
        String mensaje = "";
        if(registroDto.nombreCompleto() == null || registroDto.nombreCompleto().equals(""))
            mensaje += "El nombre es invalido \n" ;
        if(registroDto.cedula() == null || registroDto.cedula().equals(""))
            mensaje += "El documento es invalido \n" ;
        if(mensaje.equals("")){
            return true;
        }else{
            registroUsersController.mostrarMensaje("Notificación", mensaje, Alert.AlertType.WARNING);
            return false;
        }
    }
    public void limpiarCamposUsuario(){
        txtIdentificacion.setText("");
        txtNombreCompleto.setText("");
        txtCorreo.setText("");
        txtTelefono.setText("");
        txtPassword.setText("");
        txtDireccion.setText("");
    }
}
