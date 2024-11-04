package co.urwallet.viewController;

import co.urwallet.controller.CuentaControllers;
import co.urwallet.controller.TrasaccionControllers;
import co.urwallet.mapping.dto.CuentaDto;
import co.urwallet.mapping.dto.UsuarioDto;
import co.urwallet.model.TipoCuenta;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.UUID;

public class CuentaViewsControllers {
    CuentaControllers cuentaControllers;
    ObservableList<CuentaDto> listaCuentaDto = FXCollections.observableArrayList();
    CuentaDto cuentaSeleccionada;
    TrasaccionControllers trasaccionControllers = new TrasaccionControllers();
    @FXML
    public TextField txtNombreCuenta;
    @FXML
    public TextField txtNumeroCuenta;
    @FXML
    public ComboBox cmbTipoCuenta;
    @FXML
    public TextField txtSaldo;
    @FXML
    public TableView<CuentaDto> tableCuentas;
    @FXML
    public TableColumn<CuentaDto, String> tcNumeroCuenta;
    @FXML
    public TableColumn<CuentaDto, String> tcNombreCuenta;
    @FXML
    public TableColumn <CuentaDto, String> tcTipo;
    @FXML
    public TableColumn <CuentaDto, Float> tcSaldo;

    @FXML
    public void initialize() {
        cuentaControllers = new CuentaControllers();
        initDataBinding();
        obtenerCuenta();
        tableCuentas.getItems().clear();
        tableCuentas.setItems(listaCuentaDto);
        listenerSelection();
    }
    private void obtenerCuenta() {
        listaCuentaDto.addAll(cuentaControllers.obtenerCuenta());
    }
    private void listenerSelection() {
        tableCuentas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            cuentaSeleccionada = newSelection;
            mostrarInformacionCuenta(cuentaSeleccionada);
        });
    }
    private void initDataBinding() {
        tcNumeroCuenta.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().numeCuenta()));
        tcNombreCuenta.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().nombreCuenta()));
        tcTipo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().tipoCuenta().toString()));
        tcSaldo.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().saldo()).asObject());
    }
    private void mostrarInformacionCuenta(CuentaDto cuentaSelecc) {
        if(cuentaSelecc != null){
            txtNumeroCuenta.setText(cuentaSelecc.numeCuenta());
            txtNombreCuenta.setText(cuentaSelecc.nombreCuenta());
            cmbTipoCuenta.setValue(cuentaSelecc.tipoCuenta());
            txtSaldo.setText(cuentaSelecc.saldo().toString());
        }
    }
    public void limpiarCuentaAction(ActionEvent actionEvent) {
        limpiarCuenta();
    }
    public void limpiarCuenta() {
        txtNumeroCuenta.setText("");
        txtNombreCuenta.setText("");
        cmbTipoCuenta.getSelectionModel().clearSelection();
        txtSaldo.setText("");
    }

    public void agregarCuentaAction(ActionEvent actionEvent) {
        CuentaDto cuentaDto =contruirRegistroCuenta();
        if(datosValidos(cuentaDto)){
            if(cuentaControllers.agregarCuenta(cuentaDto)){
                cuentaControllers.mostrarMensaje("La Cuenta se ha creado con éxito", "Notificación Usuario", Alert.AlertType.INFORMATION);
                listaCuentaDto.add(cuentaDto);

                tableCuentas.refresh();
                limpiarCuenta();
                cuentaControllers.obtenerCuenta();
                trasaccionControllers.obtenerCuentas();
            }
            else{
                cuentaControllers.mostrarMensaje("Los datos ingresados son invalidos", "Usuario no creado", Alert.AlertType.ERROR);
            }
        }
    }

    public void eliminarCuentaAction(ActionEvent actionEvent) {
        try {
            String idCuenta = cuentaSeleccionada.numeCuenta();

            // Llamada al método eliminar del controlador
            boolean CuentaEliminada = cuentaControllers.eliminarCuenta(idCuenta);

            if (CuentaEliminada) {
                cuentaControllers.mostrarMensaje("Notificación Cliente", "Cuenta eliminada correctamente.", Alert.AlertType.INFORMATION);
                listaCuentaDto.remove(cuentaSeleccionada);
                tableCuentas.getSelectionModel().clearSelection();
                tableCuentas.refresh();
                limpiarCuenta();
                trasaccionControllers.obtenerCuentas();
            } else {
                cuentaControllers.mostrarMensaje("Notificación Cliente", "No se pudo eliminar el usuario.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            cuentaControllers.mostrarMensaje("Error", "Ocurrió un error al eliminar el usuario.", Alert.AlertType.ERROR);
        }
    }

    public void actualizarCuentaAction(ActionEvent actionEvent) {
        if (cuentaSeleccionada != null) {

            CuentaDto cuentaActualizada = contruirRegistroCuenta();
            cuentaActualizada = new CuentaDto(
                    cuentaActualizada.idCuenta(),
                    cuentaActualizada.numeCuenta(),
                    cuentaActualizada.nombreCuenta(),
                    cuentaActualizada.tipoCuenta(),
                    cuentaActualizada.saldo()
            );

            boolean resultado = cuentaControllers.actualizarCuenta(cuentaSeleccionada.numeCuenta(),cuentaActualizada);

            if (resultado) {
                cuentaControllers.mostrarMensaje("La cuenta ha sido actualizado con éxito", "Notificación Usuario", Alert.AlertType.INFORMATION);
                listaCuentaDto.remove(cuentaSeleccionada);
                listaCuentaDto.add(cuentaActualizada);
                tableCuentas.refresh();
                limpiarCuenta();
                trasaccionControllers.obtenerCuentas();
            } else {
                cuentaControllers.mostrarMensaje("No se pudo actualizar el Usuario", "Error", Alert.AlertType.ERROR);
            }
        } else {
            cuentaControllers.mostrarMensaje("Debe seleccionar un usuario para actualizar", "Error", Alert.AlertType.WARNING);
        }
    }
    private CuentaDto contruirRegistroCuenta(){
        float saldo = Float.parseFloat(txtSaldo.getText());
        String id = UUID.randomUUID().toString();
        return new CuentaDto(
                id,
                txtNumeroCuenta.getText(),
                txtNombreCuenta.getText(),
                cmbTipoCuenta.getValue().toString(),
                saldo
        );
    }
    private boolean datosValidos(CuentaDto cuentaDto) {
        String mensaje = "";
        if(cuentaDto.nombreCuenta() == null || cuentaDto.nombreCuenta().equals(""))
            mensaje += "El nombre es invalido \n" ;
        if(cuentaDto.numeCuenta() == null || cuentaDto.numeCuenta().equals(""))
            mensaje += "El numero es invalido \n" ;
        if(cuentaDto.tipoCuenta() == null || cuentaDto.tipoCuenta().equals(""))
            mensaje += "El tipo es invalido \n" ;
        if(mensaje.equals("")){
            return true;
        }else{
            cuentaControllers.mostrarMensaje("Notificación", mensaje, Alert.AlertType.WARNING);
            return false;
        }
    }
}
