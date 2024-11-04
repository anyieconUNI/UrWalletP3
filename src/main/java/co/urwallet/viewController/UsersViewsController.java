package co.urwallet.viewController;

import co.urwallet.controller.RegistroUsersController;
import co.urwallet.controller.UsersController;
import co.urwallet.mapping.dto.UsuarioDto;
import co.urwallet.model.Cuenta;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UsersViewsController {
    UsersController usersController;
    ObservableList<UsuarioDto> listaUser = FXCollections.observableArrayList();
    UsuarioDto userSeleccionado;
    @FXML
    public TextField txtCedula;
    @FXML
    public TextField txtNombreCompleto;
    @FXML
    public TextField txtTelefono;
    @FXML
    public TextField txtCorreo;
    @FXML
    public PasswordField txtContrasena;
    @FXML
    public TextField txtDireccion;
    @FXML
    public TableView<UsuarioDto> tableusers;
    @FXML
    public TableColumn<UsuarioDto, String> tcCedula;
    @FXML
    public TableColumn<UsuarioDto, String> tcNombreCompleto;
    @FXML
    public TableColumn<UsuarioDto, String> tcTelefono;
    @FXML
    public TableColumn<UsuarioDto, String> tcCorreo;
    @FXML
    public TableColumn<UsuarioDto, String> tcDireccion;
    @FXML
    public TableColumn<UsuarioDto, String> tcSaldoDispo;

    @FXML
    public void initialize() {
        try {
            usersController = new UsersController();
            intiView();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void intiView(){
        initDataBinding();
        obtenerClientes();
        tableusers.getItems().clear();
        tableusers.setItems(listaUser);
        listenerSelection();
    }
    private void initDataBinding() {
        tcCedula.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().cedula()));
        tcNombreCompleto.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().nombreCompleto()));
        tcTelefono.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().telefono()));
        tcCorreo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().correo()));
        tcDireccion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().direccion()));
        tcSaldoDispo.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().saldoDispo())));
    }
    private void obtenerClientes() {
        listaUser.addAll(usersController.obtenerUsers());
    }
    private void listenerSelection() {
        tableusers.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            userSeleccionado = newSelection;
            mostrarInformacionCliente(userSeleccionado);
        });
    }

    private void mostrarInformacionCliente(UsuarioDto userSelecc) {
        if(userSelecc != null){
            txtNombreCompleto.setText(userSelecc.nombreCompleto());
            txtCorreo.setText(userSelecc.correo());
            txtContrasena.setText(userSelecc.contrasena());
            txtDireccion.setText(userSelecc.direccion());
            txtCedula.setText(userSelecc.cedula());
            txtTelefono.setText(userSelecc.telefono());
        }
    }
    public void actualizarUserAction(ActionEvent actionEvent) {
        if (userSeleccionado != null) {

            UsuarioDto usuarioActualizado = contruirRegistroAdmin();
            usuarioActualizado = new UsuarioDto(
                    userSeleccionado.idUsuario(),
                    usuarioActualizado.cedula(),
                    usuarioActualizado.nombreCompleto(),
                    usuarioActualizado.telefono(),
                    usuarioActualizado.correo(),
                    usuarioActualizado.contrasena(),
                    usuarioActualizado.direccion(),
                    userSeleccionado.saldoDispo(),
                    userSeleccionado.cuentasBancarias()
            );

            boolean resultado = usersController.actualizarUser(userSeleccionado.idUsuario(),usuarioActualizado);

            if (resultado) {
                usersController.mostrarMensaje("El Usuario ha sido actualizado con éxito", "Notificación Usuario", Alert.AlertType.INFORMATION);

                listaUser.remove(userSeleccionado);
                listaUser.add(usuarioActualizado);
                tableusers.refresh();
                limpiarCamposUsuario();
            } else {
                usersController.mostrarMensaje("No se pudo actualizar el Usuario", "Error", Alert.AlertType.ERROR);
            }
        } else {
            usersController.mostrarMensaje("Debe seleccionar un usuario para actualizar", "Error", Alert.AlertType.WARNING);
        }
    }

    public void limpiarUserAction(ActionEvent actionEvent) {
        txtNombreCompleto.setText("");
        txtCorreo.setText("");
        txtContrasena.setText("");
        txtDireccion.setText("");
        txtCedula.setText("");
        txtTelefono.setText("");
    }

    public void agregarUserAction(ActionEvent actionEvent) {
        UsuarioDto userDto =contruirRegistroAdmin();
        if(datosValidos(userDto)){
            if(usersController.agregarUsuario(userDto)){
                usersController.mostrarMensaje("El Usuario se ha creado con éxito", "Notificación Usuario", Alert.AlertType.INFORMATION);
                listaUser.add(userDto);

                // Refrescar la tabla
                tableusers.refresh();
                limpiarCamposUsuario();
                usersController.obtenerUsers();
            }
            else{
                usersController.mostrarMensaje("Los datos ingresados son invalidos", "Usuario no creado", Alert.AlertType.ERROR);
            }
        }
    }

    public void eliminarUserAction(ActionEvent actionEvent) {
        try {
            String idUser = userSeleccionado.idUsuario();

            // Llamada al método eliminar del controlador
            boolean userEliminado = usersController.eliminarUser(idUser);

            if (userEliminado) {
                usersController.mostrarMensaje("Notificación Cliente", "Usuario eliminado correctamente.", Alert.AlertType.INFORMATION);
                listaUser.remove(userSeleccionado);
                tableusers.getSelectionModel().clearSelection();
                tableusers.refresh();
                limpiarCamposUsuario();
            } else {
                usersController.mostrarMensaje("Notificación Cliente", "No se pudo eliminar el usuario.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            usersController.mostrarMensaje("Error", "Ocurrió un error al eliminar el usuario.", Alert.AlertType.ERROR);
        }
    }

    private UsuarioDto contruirRegistroAdmin(){
        Float saldo = 0.0f;
        String id = UUID.randomUUID().toString();
        List<Cuenta> cuentasBancarias = new ArrayList<>();
        return new UsuarioDto(
                id,
                txtCedula.getText(),
                txtNombreCompleto.getText(),
                txtTelefono.getText(),
                txtCorreo.getText(),
                txtContrasena.getText(),
                txtDireccion.getText(),
                saldo,
                cuentasBancarias
        );
    }
    private boolean datosValidos(UsuarioDto userDto) {
        String mensaje = "";
        if(userDto.nombreCompleto() == null || userDto.nombreCompleto().equals(""))
            mensaje += "El nombre es invalido \n" ;
        if(userDto.cedula() == null || userDto.cedula().equals(""))
            mensaje += "El documento es invalido \n" ;
        if(mensaje.equals("")){
            return true;
        }else{
            usersController.mostrarMensaje("Notificación", mensaje, Alert.AlertType.WARNING);
            return false;
        }
    }
    public void limpiarCamposUsuario(){
        txtCedula.setText("");
        txtNombreCompleto.setText("");
        txtCorreo.setText("");
        txtTelefono.setText("");
        txtContrasena.setText("");
        txtDireccion.setText("");
    }
}
