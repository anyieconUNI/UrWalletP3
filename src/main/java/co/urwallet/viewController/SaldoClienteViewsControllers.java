package co.urwallet.viewController;

import co.urwallet.controller.CuentaControllers;
import co.urwallet.mapping.dto.CuentaDto;
import co.urwallet.mapping.dto.TransaccionDto;
import co.urwallet.model.Cuenta;
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
import java.util.stream.Collectors;

public class SaldoClienteViewsControllers {
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
        this.usuarioLogeado = user;
        actualizarSaldo();
        initDataBinding();
        obtenerCuenta();
        tableCuentaUser.setItems(listaCuentaDto);
        mostrarPanelSaldo();
    }
    private void obtenerCuenta() {
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

    public void buscarCuenta(ActionEvent actionEvent) {
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
}