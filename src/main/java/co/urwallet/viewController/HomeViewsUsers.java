package co.urwallet.viewController;

import co.urwallet.controller.HomeUsersController;
import co.urwallet.mapping.dto.UsuarioDto;
import co.urwallet.model.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.UUID;

public class HomeViewsUsers {
    HomeUsersController homeUsersController;

    @FXML
    public Label welcomeLabel;
    @FXML
    public TextField txtIdentificacion;
    @FXML
    public TextField txtNombreCompleto;
    @FXML
    public TextField txtCorreo;
    @FXML
    public TextField txtTelefono;
    @FXML
    public TextField txtDireccion;
    @FXML
    public Button editar;
    @FXML
    public Button actualizar;
    private Usuario usuarioLogueado;

    @FXML
    public void initialize() {
        homeUsersController = new HomeUsersController();
    }

    // Método que recibe el usuario logueado
    public void setUsuarioLogueado(Usuario usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
        cargarDatosUsuario();
            }

    // Método para mostrar el nombre del usuario en la bienvenida
    private void cargarDatosUsuario() {
        if (usuarioLogueado != null) {
            welcomeLabel.setText("Bienvenido, " + usuarioLogueado.getNombreCompleto());
            txtIdentificacion.setText(usuarioLogueado.getCedula());
            txtNombreCompleto.setText(usuarioLogueado.getNombreCompleto());
            txtCorreo.setText(usuarioLogueado.getCorreo());
            txtTelefono.setText(usuarioLogueado.getTelefono());
            txtDireccion.setText(usuarioLogueado.getDireccion());
        }
    }


    public void editarUser(ActionEvent actionEvent) {
        txtIdentificacion.setEditable(true);
        txtNombreCompleto.setEditable(true);
        txtCorreo.setEditable(true);
        txtTelefono.setEditable(true);
        txtDireccion.setEditable(true);
        actualizar.setDisable(false);
        editar.setDisable(true);
    }

    public void actualizarUser(ActionEvent actionEvent) {
        boolean userActualizado = false;
        String idUser = usuarioLogueado.getIdUsuario();
        UsuarioDto usuarioDto = contruirDataUser();
        if(usuarioDto != null) {
            if (datosValidos(usuarioDto)) {
                userActualizado = homeUsersController.actualizarUser(idUser,usuarioDto);
                if(userActualizado){
                   homeUsersController.mostrarMensaje("Notificación Cliente", "Cliente actualizado",  Alert.AlertType.INFORMATION);
                    txtIdentificacion.setEditable(false);
                    txtNombreCompleto.setEditable(false);
                    txtCorreo.setEditable(false);
                    txtTelefono.setEditable(false);
                    txtDireccion.setEditable(false);
                    actualizar.setDisable(true);
                    editar.setDisable(false);
                }
                else{
                    homeUsersController.mostrarMensaje("Notificación Cliente", "Cliente no actualizado",  Alert.AlertType.ERROR);

                }
            }
        }
    }
    private UsuarioDto contruirDataUser(){
        Float saldo = usuarioLogueado.getSaldoDispo();
        String id = usuarioLogueado.getIdUsuario();
        String password = usuarioLogueado.getContrasena();
        return new UsuarioDto(
                id,
                txtIdentificacion.getText(),
                txtNombreCompleto.getText(),
                txtTelefono.getText(),
                txtCorreo.getText(),
                password,
                txtDireccion.getText(),
                saldo
        );
    }
    private boolean datosValidos(UsuarioDto usuarioDto) {
        String mensaje = "";
        if(usuarioDto.nombreCompleto() == null || usuarioDto.nombreCompleto().equals(""))
            mensaje += "El nombre es invalido \n" ;
        if(usuarioDto.cedula() == null || usuarioDto.cedula().equals(""))
            mensaje += "El documento es invalido \n" ;
        if(mensaje.equals("")){
            return true;
        }else{
            homeUsersController.mostrarMensaje("Notificación cliente", mensaje, Alert.AlertType.WARNING);
            return false;
        }
    }

    public void eliminar(ActionEvent actionEvent) {
        try {
            String idUser = usuarioLogueado.getIdUsuario();

            // Llamada al método eliminar del controlador
            boolean userEliminado = homeUsersController.eliminarUser(idUser);

            if (userEliminado) {
                homeUsersController.mostrarMensaje("Notificación Cliente", "Usuario eliminado correctamente.", Alert.AlertType.INFORMATION);
                homeUsersController.cerrarVentana(actionEvent);
            } else {
                homeUsersController.mostrarMensaje("Notificación Cliente", "No se pudo eliminar el usuario.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            homeUsersController.mostrarMensaje("Error", "Ocurrió un error al eliminar el usuario.", Alert.AlertType.ERROR);
        }

    }

}
