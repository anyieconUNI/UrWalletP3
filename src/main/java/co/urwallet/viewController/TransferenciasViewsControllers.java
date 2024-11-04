package co.urwallet.viewController;

import co.urwallet.controller.CuentaControllers;
import co.urwallet.controller.TrasaccionControllers;
import co.urwallet.mapping.dto.CuentaDto;
import co.urwallet.mapping.dto.TransaccionDto;
import co.urwallet.model.Cuenta;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TransferenciasViewsControllers {
    TrasaccionControllers trasaccionControllers = new TrasaccionControllers();
    CuentaControllers cuentaControllers = new CuentaControllers();
    TransaccionDto transaccionSeleccionada;
    private static TransferenciasViewsControllers instance;
    ObservableList<TransaccionDto> listaTransaccion = FXCollections.observableArrayList();
    @FXML
    public TextArea tareaDescripcion;
    @FXML
    public TextField txtMonto;
    @FXML
    public ComboBox<String> cmbCuentaOrigen;
    @FXML
    public ComboBox<String> cmbCuentaDestino;
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
    public TransferenciasViewsControllers() {
        if (instance == null) {
            instance = this;
        }
    }
    public static TransferenciasViewsControllers getInstance() {
        return instance;
    }
    @FXML
    public void initialize() {
        cargarCuentas();
        initDataBinding();
        obtenerTransaccion();
        tableTransaccion.getItems().clear();
        tableTransaccion.setItems(listaTransaccion);
    }
    private void obtenerTransaccion() {
        listaTransaccion.addAll(trasaccionControllers.obtenerTrasaccion());
    }
//    private void listenerSelection() {
//        tableTransaccion.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
//            transaccionSeleccionada = newSelection;
//            mostrarInformacionTransaccion(transaccionSeleccionada);
//        });
//    }
    private void initDataBinding() {
        tcFecha.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().fecha().toString()));
        tcTipoTrasacc.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().tipoTransaccion()));
        tcMonto.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().monto())));
        tcDescripcion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().descripcion()));
        tcCuetaOrigen.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().cuentaOrigen()));
        tcCuentaDestino.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().cuentaDestino()));
        tcCategoria.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().categoria()));
    }
//    private void mostrarInformacionTransaccion(TransaccionDto transaccionDto) {
//        if(transaccionDto != null){
//            tareaDescripcion.setText(transaccionDto.descripcion());
//            txtMonto.setText(String.valueOf(transaccionDto.monto()));
//            cmbCuentaOrigen.setValue(transaccionDto.cuentaOrigen());
//            cmbCuentaDestino.setValue(transaccionDto.cuentaDestino());
//            cmbTipoTrnsaccion.setValue(transaccionDto.tipoTransaccion());
//            cmbCategoria.setValue(transaccionDto.categoria());
//        }
//    }
    public  void cargarCuentas() {
        List<CuentaDto> cuentasOrigen = cuentaControllers.obtenerCuenta();
        ObservableList<String> nombresClientes = FXCollections.observableArrayList(
                cuentasOrigen.stream().map(CuentaDto::numeCuenta).collect(Collectors.toList())
        );
        cmbCuentaOrigen.setItems(nombresClientes);
        cmbCuentaDestino.setItems(nombresClientes);
    }
    public void limpiarTransferenciaAction(ActionEvent actionEvent) {
        limpiar();
    }
    public void limpiar(){
        tareaDescripcion.setText("");
        txtMonto.setText("");
        cmbCuentaOrigen.setValue("");
        cmbCuentaDestino.setValue("");
        cmbTipoTrnsaccion.setValue("");
        cmbCategoria.setValue("");
    }
    public void agregarTransferenciaAction(ActionEvent actionEvent) {
        TransaccionDto transaccionDto  = contruirRegistroTrasaccion();
        if(datosValidos(transaccionDto)){
            if(trasaccionControllers.agregarTrasaccion(transaccionDto)){
                trasaccionControllers.mostrarMensaje("La Transacción se ha creado con éxito", "Notificación Usuario", Alert.AlertType.INFORMATION);
                listaTransaccion.add(transaccionDto);
                tableTransaccion.refresh();
                trasaccionControllers.obtenerTrasaccion();
                limpiar();
                trasaccionControllers.SumarSaldo(transaccionDto);
                trasaccionControllers.RestarrSaldo(transaccionDto);
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
                cmbCuentaOrigen.getValue(),
                cmbCuentaDestino.getValue(),
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
//        if(transaccionDto.cuentaDestino() ==  transaccionDto.cuentaDestino())
//            mensaje += "La cuenta Destino y la cuenta origen no pueden ser igual \n";
        if(mensaje.equals("")){
            return true;
        }else{
            trasaccionControllers.mostrarMensaje("Notificación", mensaje, Alert.AlertType.WARNING);
            return false;
        }
    }
}
