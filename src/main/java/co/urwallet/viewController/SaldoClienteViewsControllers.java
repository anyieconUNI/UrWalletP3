package co.urwallet.viewController;

import co.urwallet.controller.CuentaControllers;
import co.urwallet.controller.SaldoClienteController;
import co.urwallet.mapping.dto.CuentaDto;
import co.urwallet.mapping.dto.PresupuestoDto;
import co.urwallet.mapping.dto.TransaccionDto;
import co.urwallet.model.Categoria;
import co.urwallet.model.Cuenta;
import co.urwallet.model.Transaccion;
import co.urwallet.model.Usuario;
import co.urwallet.utils.Persistencia;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SaldoClienteViewsControllers {

    SaldoClienteController saldoClienteController;
    PresupuestoDto presupuestoSeleccionado;
    //private Mes mesSeleccionado;
    private ObservableList<Transaccion> listaTransacciones = FXCollections.observableArrayList();
    ObservableList<PresupuestoDto> listaPresupuestoDto = FXCollections.observableArrayList();

    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnLimpiar;
    @FXML
    public TableView<PresupuestoDto> tablePresupuestos;
    @FXML
    public TableColumn<PresupuestoDto, String> tcNombre;
    @FXML
    public TableColumn<PresupuestoDto, String> tcMontoGasto;
    @FXML
    public TableColumn<PresupuestoDto, String> tcMontoTotal;
    @FXML
    public TableColumn<PresupuestoDto, String> tcCategoria;

    @FXML
    public TextField txtNombre;
    @FXML
    public TextField txtMontoGasto;
    @FXML
    public TextField txtMontoTotal;
    @FXML
    public ComboBox cmbCategoria;

    @FXML
    public TableView<CuentaDto> tableCuentaUser;
    @FXML
    public TableColumn<CuentaDto, String> tcNumeroCuenta;
    @FXML
    public TableColumn<CuentaDto, String>  tcNombreCuenta;
    @FXML
    public TableColumn<CuentaDto, String>  tcTipo;
    @FXML
    public TableColumn<CuentaDto, Float>  tcSaldo;
    @FXML
    public TextField txtNombreBlanco;


    ObservableList<CuentaDto> listaCuentaDto = FXCollections.observableArrayList();
    CuentaControllers cuentaControllers;
    private Usuario usuarioLogeado;
    private static SaldoClienteViewsControllers instance;
    public SaldoClienteViewsControllers() {
        if (instance == null) {
            instance = this;
        }
    }

    public static SaldoClienteViewsControllers getInstance() {
        return instance;
    }

    @FXML
    public Label Saldo;
    @FXML
    private AnchorPane panelSaldo;

    @FXML
    private AnchorPane panelPresupuesto;

    @FXML
    private TextArea txtPresupuestoDetalle;

    @FXML
    private Button btnSaldo;

    @FXML
    private Button btnPresupuesto;

    @FXML
    private void initialize() {
        cuentaControllers = new CuentaControllers();
    }
    public void setUsuario(Usuario user) {
        saldoClienteController = new SaldoClienteController();
        this.usuarioLogeado = user;
        actualizarSaldo();
        initDataBinding();
        obtenerCuenta();
        intDataBindingP();
        obtenerPresupuesto();
        listenerPresupuesto();
        tableCuentaUser.setItems(listaCuentaDto);
        tableCuentaUser.setItems(listaCuentaDto);
        mostrarPanelSaldo();
    }
    public void obtenerCuenta() {
        List<CuentaDto> todasLasCuentas = cuentaControllers.obtenerCuenta();
        List<CuentaDto> cuentasUsers = todasLasCuentas.stream()
                .filter(cuenta -> usuarioLogeado.getCedula().equals(cuenta.clienteId()))
                .collect(Collectors.toList());
        listaCuentaDto.clear();
        listaCuentaDto.addAll(cuentasUsers);
        tableCuentaUser.refresh();
    }
    private void initDataBinding() {
        tcNumeroCuenta.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().numeCuenta()));
        tcNombreCuenta.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().nombreCuenta()));
        tcTipo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().tipoCuenta().toString()));
        tcSaldo.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().saldo()).asObject());
    }
    @FXML
    private void mostrarPanelSaldo() {
        panelSaldo.setVisible(true);
        panelPresupuesto.setVisible(false);
    }

    @FXML
    private void mostrarPanelPresupuesto() {
        panelPresupuesto.setVisible(true);
        panelSaldo.setVisible(false);
    }
    public void actualizarSaldo() {
        if (usuarioLogeado != null) {
            List<CuentaDto> todasLasCuentas = cuentaControllers.obtenerCuenta();
            List<CuentaDto> cuentasDelUsuario = todasLasCuentas.stream()
                    .filter(cuenta -> usuarioLogeado.getCedula().equals(cuenta.clienteId()))
                    .collect(Collectors.toList());
            double saldoTotal = cuentasDelUsuario.stream()
                    .mapToDouble(CuentaDto::saldo)
                    .sum();
            Saldo.setText(String.format("Saldo Total: $ %.2f", saldoTotal));
        } else {
            Saldo.setText("Usuario no logueado.");
        }
    }
    public void solicitarCuenta(ActionEvent actionEvent) {
        if(txtNombreBlanco.getText() != null){
            String mensaje = String.format(
                    "Tienes una solicitud DE CUENTA:\nUsuario: %s\nCC: %s\nNombre de banco: %s",
                    usuarioLogeado.getNombreCompleto(),
                    usuarioLogeado.getCedula(),
                    txtNombreBlanco.getText()
            );
            cuentaControllers.enviarSolicitud(mensaje);
        }
    }


    public void actualizarTablaCuentasUser(CuentaDto cuentaDto) {
        if (cuentaDto != null) {
            boolean cuentaExistente = listaCuentaDto.stream()
                    .anyMatch(cuenta -> cuenta.numeCuenta().equals(cuentaDto.numeCuenta()));

            if (!cuentaExistente) {
                actualizarSaldo();
                listaCuentaDto.add(cuentaDto);
                tableCuentaUser.refresh();

            } else {
                System.out.println("La cuenta ya existe en la tabla.");
            }
        } else {
            System.out.println("Cuenta nula, no se puede agregar.");
        }
    }

    // Crud presupuesto

    private void obtenerPresupuesto(){listaPresupuestoDto.addAll(saldoClienteController.obtenerPresupuesto());}

    private void listenerPresupuesto() {
        tablePresupuestos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            presupuestoSeleccionado = newSelection;
            mostrarInformacionPresupuesto(presupuestoSeleccionado);
        });
    }

    private void intDataBindingP(){
        tcNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().nombre()));
        tcMontoGasto.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().montoGasto()).asObject().asString());
        tcMontoTotal.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().montoTotal()).asObject().asString());
        tcCategoria.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().categoria().toString()));
    }

    private void mostrarInformacionPresupuesto(PresupuestoDto presupuestoSeleccionado) {
        if(presupuestoSeleccionado != null){
            txtNombre.setText(presupuestoSeleccionado.nombre());
            txtMontoGasto.setText(presupuestoSeleccionado.montoGasto().toString());
            txtMontoTotal.setText(presupuestoSeleccionado.montoTotal().toString());
            cmbCategoria.setValue(presupuestoSeleccionado.categoria());
        }
    }

    public void limpiarPresupuestoAction(ActionEvent actionEvent) {limpiarPresupuesto();}

    public void limpiarPresupuesto(){
        txtNombre.setText("");
        txtMontoGasto.setText("");
        txtMontoTotal.setText("");
        cmbCategoria.getSelectionModel().clearSelection();
    }

    public void agregarPresupuestoAction(ActionEvent actionEvent) {
        PresupuestoDto presupuestoDto = construirRegistroPresupuesto();
        if(datosValidos(presupuestoDto)){
            if(saldoClienteController.agregarPresupuesto(presupuestoDto)){
                saldoClienteController.mostrarMensaje("El presumpuesto se creo correctamente", "Notificación usuario", Alert.AlertType.INFORMATION);
                listaPresupuestoDto.add(presupuestoDto);

                tablePresupuestos.refresh();
                limpiarPresupuesto();
                saldoClienteController.obtenerPresupuesto();
            }
            else{
                saldoClienteController.mostrarMensaje("Los datos ingresados son invalidos", "Presupuesto no creado", Alert.AlertType.ERROR);
            }
        }
    }

    public void actualizarPresupuestoAction(ActionEvent actionEvent) {
        if (presupuestoSeleccionado != null) {

            PresupuestoDto presupuestoActualizado = construirRegistroPresupuesto();
            presupuestoActualizado = new PresupuestoDto(
                    presupuestoActualizado.idPresupuesto(),
                    presupuestoActualizado.nombre(),
                    presupuestoActualizado.montoTotal(),
                    presupuestoActualizado.montoGasto(),
                    presupuestoActualizado.categoria()
            );

            boolean resultado = saldoClienteController.actualizarPresupuesto(presupuestoSeleccionado.idPresupuesto(),presupuestoActualizado);

            if (resultado) {
                saldoClienteController.mostrarMensaje("El presupuesto ha sido actualizado con éxito", "Notificación Usuario", Alert.AlertType.INFORMATION);
                listaPresupuestoDto.remove(presupuestoSeleccionado);
                listaPresupuestoDto.add(presupuestoActualizado);
                tablePresupuestos.refresh();
                limpiarPresupuesto();
            } else {
                saldoClienteController.mostrarMensaje("No se pudo actualizar el Presupuesto", "Error", Alert.AlertType.ERROR);
            }
        } else {
            saldoClienteController.mostrarMensaje("Debe seleccionar un presupuesto para actualizar", "Error", Alert.AlertType.WARNING);
        }
    }

    public void eliminarPresupuestoAction(ActionEvent actionEvent) {
        try {
            String idPresupuesto = presupuestoSeleccionado.idPresupuesto();
            boolean PresupuestoEliminado = saldoClienteController.eliminarPresupuesto(idPresupuesto);

            if (PresupuestoEliminado) {
                saldoClienteController.mostrarMensaje("Notificación Cliente", "Presupuesto eliminada correctamente.", Alert.AlertType.INFORMATION);
                listaPresupuestoDto.remove(presupuestoSeleccionado);
                tablePresupuestos.getSelectionModel().clearSelection();
                tablePresupuestos.refresh();
                limpiarPresupuesto();
            } else {
                saldoClienteController.mostrarMensaje("Notificación Cliente", "No se pudo eliminar el presupuesto.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            saldoClienteController.mostrarMensaje("Error", "Ocurrió un error al eliminar el presupuesto.", Alert.AlertType.ERROR);
        }
    }

    private PresupuestoDto construirRegistroPresupuesto() {
        float montoTotal = Float.parseFloat(txtMontoTotal.getText());
        float montoGasto = Float.parseFloat(txtMontoGasto.getText());
        String cateSelec = cmbCategoria.getValue().toString();
        Categoria catego = Categoria.valueOf(cateSelec);

        String id = UUID.randomUUID().toString();
        return new PresupuestoDto(
                id,
                txtNombre.getText(),
                montoTotal,
                montoGasto,
                catego
        );
    }

    private boolean datosValidos(PresupuestoDto presupuestoDto){
        String mensaje = "";
        if(presupuestoDto.nombre() == null || presupuestoDto.nombre().equals(""))
            mensaje += "El nombre es invalido \n" ;
        if(presupuestoDto.montoTotal() == null || presupuestoDto.montoTotal().equals(""))
            mensaje += "El monto total es invalido \n" ;
        if(presupuestoDto.categoria() == null || presupuestoDto.categoria().equals(""))
            mensaje += "La categoría es invalida \n" ;
        if(mensaje.equals("")){
            return true;
        }else{
            saldoClienteController.mostrarMensaje("Notificación", mensaje, Alert.AlertType.WARNING);
            return false;
        }
    }

}