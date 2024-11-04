package co.urwallet.viewController;

import co.urwallet.controller.CuentaControllers;
import co.urwallet.controller.TrasaccionControllers;
import co.urwallet.mapping.dto.TransaccionDto;
import co.urwallet.model.Cuenta;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TransferenciasViewsControllers {
    TrasaccionControllers trasaccionControllers;
    CuentaControllers cuentaControllers;
    TransaccionDto transaccionSeleccionada;
    ObservableList<TransaccionDto> listaTransaccion = FXCollections.observableArrayList();
    @FXML
    public TextArea tareaDescripcion;
    @FXML
    public TextField txtMonto;
    @FXML
    public ComboBox<Cuenta> cmbCuentaOrigen;
    @FXML
    public ComboBox<Cuenta> cmbCuentaDestino;
    @FXML
    public ComboBox cmbTipoTrnsaccion;
    @FXML
    public ComboBox cmbCategoria;
    @FXML
    public TableView<TransaccionDto> tableTransaccion;
    @FXML
    public TableColumn<TransaccionDto, String> tcFecha;
    @FXML
    public TableColumn<TransaccionDto, String> tcTipoTrasacc;
    @FXML
    public TableColumn<TransaccionDto, String> tcMonto;
    @FXML
    public TableColumn<TransaccionDto, String> tcDescripcion;
    @FXML
    public TableColumn<TransaccionDto, String> tcCuetaOrigen;
    @FXML
    public TableColumn<TransaccionDto, String> tcCuentaDestino;
    @FXML
    public TableColumn<TransaccionDto, String> tcCategoria;

    @FXML
    public void initialize() {
        trasaccionControllers = new TrasaccionControllers();

    }
    public void limpiarTransferenciaAction(ActionEvent actionEvent) {
    }
    private void cargarCuentasEnComboBox() {
        // Obtén la lista de cuentas desde el controlador de cuentas
        Cuenta cuentas = (Cuenta) cuentaControllers.obtenerCuenta();

        // Convierte la lista a un ObservableList
        ObservableList<Cuenta> cuentasObservableList = FXCollections.observableArrayList(cuentas);

        // Establece la lista en ambos ComboBox
        cmbCuentaOrigen.setItems(cuentasObservableList);
        cmbCuentaDestino.setItems(cuentasObservableList);
    }

    public void agregarTransferenciaAction(ActionEvent actionEvent) {
        TransaccionDto transaccionDto  = contruirRegistroTrasaccion();
        if(datosValidos(transaccionDto)){
            if(trasaccionControllers.agregarTrasaccion(transaccionDto)){
                trasaccionControllers.mostrarMensaje("La Transacción se ha creado con éxito", "Notificación Usuario", Alert.AlertType.INFORMATION);
//                listatransaccionDto.add(transaccionDto);

//                tableCuentas.refresh();
//                limpiarCuenta();
//                trasaccionControllers.obtenerCuenta();
            }
            else{
                trasaccionControllers.mostrarMensaje("Los datos ingresados son invalidos", "Usuario no creado", Alert.AlertType.ERROR);
            }
        }
    }
    private TransaccionDto contruirRegistroTrasaccion(){
        float monto = Float.parseFloat(txtMonto.getText());
        Date fecha = new Date(System.currentTimeMillis());
        String id = UUID.randomUUID().toString();
        return new TransaccionDto(
                id,
                fecha,
                cmbTipoTrnsaccion.getValue().toString(),
                monto,
                tareaDescripcion.getText(),
                (Cuenta) cmbCuentaOrigen.getValue(),
                (Cuenta) cmbCuentaDestino.getValue(),
                cmbCategoria.getValue().toString()
        );
    }
    private boolean datosValidos(TransaccionDto transaccionDto) {
        String mensaje = "";
        if (transaccionDto.monto() < 0 || transaccionDto.monto() == 0) {
            mensaje += "El monto es inválido \n";
        }
        if(transaccionDto.tipoTransaccion() == null || transaccionDto.tipoTransaccion().equals(""))
            mensaje += "El Tipo Transacción es invalido \n" ;
        if(transaccionDto.cuentaOrigen() == null || transaccionDto.cuentaOrigen().equals(""))
            mensaje += "La cuenta Origen es invalido \n" ;
        if(transaccionDto.cuentaDestino() == null || transaccionDto.cuentaDestino().equals(""))
            mensaje += "La cuenta Destino es invalido \n";
        if(transaccionDto.categoria() == null || transaccionDto.categoria().equals(""))
            mensaje += "La categoria es invalido \n";
        if(mensaje.equals("")){
            return true;
        }else{
            trasaccionControllers.mostrarMensaje("Notificación", mensaje, Alert.AlertType.WARNING);
            return false;
        }
    }
}
