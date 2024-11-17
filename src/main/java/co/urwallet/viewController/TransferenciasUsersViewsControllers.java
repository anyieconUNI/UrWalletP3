package co.urwallet.viewController;

import co.urwallet.controller.CuentaControllers;
import co.urwallet.controller.TrasaccionControllers;
import co.urwallet.controller.UsersController;
import co.urwallet.mapping.dto.CuentaDto;
import co.urwallet.mapping.dto.TransaccionDto;
import co.urwallet.mapping.dto.UsuarioDto;
import co.urwallet.model.Categoria;
import co.urwallet.model.Cuenta;
import co.urwallet.model.TipoTransaccion;
import co.urwallet.model.Usuario;
import co.urwallet.utils.Persistencia;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class TransferenciasUsersViewsControllers {
    @FXML
    public ComboBox<String> cbBuscarPorCategory;
    @FXML
    public TextField txtCuentaDestino;
    @FXML
    public ComboBox<String> cbBuscarPorType;
    @FXML
    public DatePicker dpFechaTransaccion;
    TrasaccionControllers trasaccionControllers;
    CuentaControllers cuentaControllers = new CuentaControllers();
    TransaccionDto transaccionSeleccionada;
    private static TransferenciasUsersViewsControllers instance;
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
    private Usuario usuarioLogeado;

    public TransferenciasUsersViewsControllers() {
        if (instance == null) {
            instance = this;
        }
    }

    public static TransferenciasUsersViewsControllers getInstance() {
        return instance;
    }


    @FXML
    public void initialize() {
        trasaccionControllers = new TrasaccionControllers();

    }

    public void setUsuarioLogueado(Usuario usuarioLog) {
        this.usuarioLogeado = usuarioLog;
        cargarCuentas();
        initDataBinding();
        obtenerTransaccion();
        tableTransaccion.getItems().clear();
        tableTransaccion.setItems(listaTransaccion);
    }

    private void obtenerTransaccion() {
        List<TransaccionDto> todasLasTransacciones = trasaccionControllers.obtenerTrasaccion();
        List<TransaccionDto> transaccionesFiltradas = todasLasTransacciones.stream()
                .filter(transaccion -> usuarioLogeado.getCuentasBancarias().stream()
                        .anyMatch(cuentaBancaria -> cuentaBancaria.getNumeCuenta().equals(transaccion.cuentaOrigen().getNumeCuenta())))
                .collect(Collectors.toList());
        listaTransaccion.addAll(transaccionesFiltradas);
        ObservableList<String> categorias = FXCollections.observableArrayList(
                Arrays.stream(Categoria.values())
                        .map(Enum::name)
                        .collect(Collectors.toList())
        );

        cbBuscarPorCategory.setItems(categorias);
        ObservableList<String> tipos = FXCollections.observableArrayList(
                Arrays.stream(TipoTransaccion.values())
                        .map(Enum::name)
                        .collect(Collectors.toList())
        );

        cbBuscarPorType.setItems(tipos);
    }

    private void initDataBinding() {
        tcFecha.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().fecha()));
        tcTipoTrasacc.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().tipoTransaccion().toString()));
        tcMonto.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().monto())));
        tcDescripcion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().descripcion()));
        tcCuetaOrigen.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().cuentaOrigen().getNumeCuenta()));
        tcCuentaDestino.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().cuentaDestino().getNumeCuenta()));
        tcCategoria.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().categoria().toString()));
    }

    public void cargarCuentas() {

        List<CuentaDto> todasLasCuentas = cuentaControllers.obtenerCuenta();

        List<CuentaDto> cuentasOrigen = todasLasCuentas.stream()
                .filter(cuenta -> usuarioLogeado.getCuentasBancarias().stream()
                        .anyMatch(cuentaBancaria -> cuentaBancaria.getIdCuenta().equals(cuenta.idCuenta())))
                .collect(Collectors.toList());
        ObservableList<String> nombresClientesOrigen = FXCollections.observableArrayList(
                cuentasOrigen.stream().map(CuentaDto::numeCuenta).collect(Collectors.toList())
        );
        cmbCuentaOrigen.setItems(nombresClientesOrigen);

        ObservableList<String> nombresClientesDestino = FXCollections.observableArrayList(
                todasLasCuentas.stream()
                        .filter(cuenta -> usuarioLogeado.getCuentasBancarias().stream()
                                .noneMatch(cuentaBancaria -> cuentaBancaria.getIdCuenta().equals(cuenta.idCuenta())))
                        .map(CuentaDto::numeCuenta)
                        .collect(Collectors.toList())
        );

        cmbCuentaDestino.setItems(nombresClientesDestino);
    }

    public void limpiarTransferenciaAction(ActionEvent actionEvent) {
        limpiar();
    }

    public void limpiar() {
        tareaDescripcion.setText("");
        txtMonto.setText("");
        cmbCuentaOrigen.setValue("");
        cmbCuentaDestino.setValue("");
        cmbTipoTrnsaccion.setValue("");
        cmbCategoria.setValue("");
    }

    public void agregarTransferenciaAction(ActionEvent actionEvent) {
        TransaccionDto transaccionDto = contruirRegistroTrasaccion();
        if (datosValidos(transaccionDto)) {
            if (trasaccionControllers.agregarTrasaccion(transaccionDto)) {
                trasaccionControllers.mostrarMensaje("La Transacción se ha creado con éxito", "Notificación Usuario", Alert.AlertType.INFORMATION);
                listaTransaccion.add(transaccionDto);
                tableTransaccion.refresh();
                trasaccionControllers.obtenerTrasaccion();
                limpiar();
                trasaccionControllers.SumarSaldo(transaccionDto);
                trasaccionControllers.RestarrSaldo(transaccionDto);
            } else {
                trasaccionControllers.mostrarMensaje("Los datos ingresados son invalidos", "Usuario no creado", Alert.AlertType.ERROR);
            }
        }
    }

    private TransaccionDto contruirRegistroTrasaccion() {
        float monto = Float.parseFloat(txtMonto.getText());
        Date fechaActual = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String fechaFormateada = sdf.format(fechaActual);
        String id = UUID.randomUUID().toString();
        String cuentaOrigenStr = cmbCuentaOrigen.getValue();
        String cuentaDestinoStr = cmbCuentaDestino.getValue();
        String tipoSeleccionado = cmbTipoTrnsaccion.getValue().toString();
        TipoTransaccion tipoTransaccion = TipoTransaccion.valueOf(tipoSeleccionado);

        String cateSelec = cmbCategoria.getValue().toString();
        Categoria catego = Categoria.valueOf(cateSelec);
        Cuenta cuentaOrigen = trasaccionControllers.obtenerbyid(cuentaOrigenStr);
        Cuenta cuentaDestino = trasaccionControllers.obtenerbyid(cuentaDestinoStr);
        return new TransaccionDto(
                id,
                fechaFormateada,
                tipoTransaccion,
                monto,
                tareaDescripcion.getText(),
                cuentaOrigen,
                cuentaDestino,
                catego
        );
    }

    private boolean datosValidos(TransaccionDto transaccionDto) {
        String mensaje = "";
        if (transaccionDto.monto() < 0 || transaccionDto.monto() == 0) {
            mensaje += "El monto es inválido \n";
        }
        if (transaccionDto.tipoTransaccion() == null || transaccionDto.tipoTransaccion().equals(""))
            mensaje += "El Tipo Transacción es invalido \n";
        if (transaccionDto.cuentaOrigen() == null || transaccionDto.cuentaOrigen().equals(""))
            mensaje += "La cuenta Origen es invalido \n";
        if (transaccionDto.cuentaDestino() == null || transaccionDto.cuentaDestino().equals(""))
            mensaje += "La cuenta Destino es invalido \n";
        if (transaccionDto.categoria() == null || transaccionDto.categoria().equals(""))
            mensaje += "La categoria es invalido \n";
        if (transaccionDto.cuentaOrigen() != null && transaccionDto.cuentaOrigen().equals(transaccionDto.cuentaDestino()) && transaccionDto.cuentaDestino().equals(transaccionDto.cuentaOrigen())) {
            mensaje += "La cuenta Origen y la cuenta Destino no pueden ser la misma \n";
        }
        if (mensaje.equals("")) {
            return true;
        } else {
            trasaccionControllers.mostrarMensaje("Notificación", mensaje, Alert.AlertType.WARNING);
            return false;
        }
    }


    public void buscarAction(ActionEvent actionEvent) {
        String categoriaSeleccionada = cbBuscarPorCategory.getValue();
        String tipoTransaccion = cbBuscarPorType.getValue();
        String cuentaDestinoTexto = txtCuentaDestino.getText().trim();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fechaSeleccionada;
        if (dpFechaTransaccion.getValue() != null){
            fechaSeleccionada = dpFechaTransaccion.getValue().format(formatter);
        } else {
            fechaSeleccionada = "";
        }

        List<TransaccionDto> transaccionesFiltradas = trasaccionControllers.obtenerTrasaccion().stream()
                .filter(transaccionDto -> {
                    boolean coincideCategoria = categoriaSeleccionada == null || categoriaSeleccionada.isEmpty() ||
                            transaccionDto.categoria().name().equalsIgnoreCase(categoriaSeleccionada);
                    boolean coincideTipo = tipoTransaccion == null || tipoTransaccion.isEmpty() ||
                            transaccionDto.tipoTransaccion().name().equalsIgnoreCase(categoriaSeleccionada);
                    boolean coincideCuentaDestino = cuentaDestinoTexto.isEmpty() ||
                            transaccionDto.cuentaDestino().getNumeCuenta().contains(cuentaDestinoTexto);
                    boolean coincideConCuentaOrigen = usuarioLogeado.getCuentasBancarias().stream()
                            .anyMatch(cuentaBancaria -> cuentaBancaria.getNumeCuenta().equals(transaccionDto.cuentaOrigen().getNumeCuenta()));
                    boolean coincideFecha = fechaSeleccionada.isEmpty() ||
                            transaccionDto.fecha().equals(fechaSeleccionada);

                    return coincideCategoria && coincideCuentaDestino && coincideConCuentaOrigen && coincideFecha && coincideTipo;
                })
                .collect(Collectors.toList());
        listaTransaccion.clear();
        listaTransaccion.setAll(transaccionesFiltradas);
        tableTransaccion.refresh();
    }

    public void limparFiltre(ActionEvent actionEvent) {
       cbBuscarPorCategory.setValue(null);
       cbBuscarPorType.setValue(null);
       txtCuentaDestino.setText("");
       dpFechaTransaccion.setValue(null);
        listaTransaccion.clear();
       tableTransaccion.refresh();
        obtenerTransaccion();
    }

    public void actualizarTablaTransacciones(TransaccionDto nuevaTransaccion) {
        if (usuarioLogeado.getCuentasBancarias().stream()
                .anyMatch(cuenta -> cuenta.getNumeCuenta().equals(nuevaTransaccion.cuentaOrigen().getNumeCuenta()))) {
            trasaccionControllers.mostrarMensaje("Notificación","Haz hecho una transferencia", Alert.AlertType.INFORMATION);
            listaTransaccion.add(nuevaTransaccion);
            tableTransaccion.refresh();
        }
    }
}
