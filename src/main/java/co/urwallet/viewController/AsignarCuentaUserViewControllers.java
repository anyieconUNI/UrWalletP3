package co.urwallet.viewController;

import co.urwallet.controller.AsignarCuentaUserControllers;
import co.urwallet.controller.CuentaControllers;
import co.urwallet.controller.TrasaccionControllers;
import co.urwallet.controller.UsersController;
import co.urwallet.mapping.dto.CuentaDto;
import co.urwallet.mapping.dto.UsuarioDto;
import co.urwallet.mapping.mappers.UrWalletMapper;
import co.urwallet.model.Usuario;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;
import java.util.stream.Collectors;

public class AsignarCuentaUserViewControllers {
    public TableView<UsuarioDto> tableusersCuenta;
    public TableColumn<UsuarioDto, String> tcCedula;
    public TableColumn<UsuarioDto, String> tcNombreCompleto;
    public TableColumn<UsuarioDto, String> tcCorreo;
    public TableColumn<UsuarioDto, String> tcSaldoDispo;
    public TableColumn<UsuarioDto, String> tcCuenta;
    public TableColumn<UsuarioDto, String> tcNombreCuenta;

    AsignarCuentaUserControllers asignarCuentaUserControllers = new AsignarCuentaUserControllers();
    private static AsignarCuentaUserViewControllers instance;
    ObservableList<UsuarioDto> listaUserCuen = FXCollections.observableArrayList();
    CuentaDto cuentaSeleccionada;
    @FXML
    public TextField txtBucarUser;
    @FXML
    public ComboBox<String> cbCuenta;
    @FXML
    public ComboBox cmbCliente;
    CuentaControllers cuentaControllers = new CuentaControllers();
    UsersController clientes = new UsersController();
    public AsignarCuentaUserViewControllers() {
        if (instance == null) {
            instance = this;
        }
    }
    public static AsignarCuentaUserViewControllers getInstance() {
        return instance;
    }
    @FXML
    public void initialize() {
        cargarCuentas();
        cargarClientes();
        initDataBinding();
        obtenerClientes();
        tableusersCuenta.getItems().clear();
        tableusersCuenta.setItems(listaUserCuen);
    }
    private void obtenerClientes() {
        List<UsuarioDto> usuariosConCuenta = clientes.obtenerUsers().stream()
                .filter(usuario -> usuario.cuentasBancarias() != null && !usuario.cuentasBancarias().isEmpty())
                .collect(Collectors.toList());
        listaUserCuen.setAll(usuariosConCuenta);
//        listaUserCuen.addAll(clientes.obtenerUsers());
    }
    private void initDataBinding() {
        tcCedula.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().cedula()));
        tcNombreCompleto.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().nombreCompleto()));
        tcCorreo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().correo()));
        tcSaldoDispo.setCellValueFactory(cellData -> {
            UsuarioDto usuario = cellData.getValue();
            if (usuario.cuentasBancarias() != null && !usuario.cuentasBancarias().isEmpty()) {
                return new SimpleStringProperty(String.valueOf(usuario.cuentasBancarias().get(0).getSaldo()));
            } else {
                return new SimpleStringProperty("0.0");
            }
        });
        tcNombreCuenta.setCellValueFactory(cellData -> {
            UsuarioDto usuario = cellData.getValue();
            if (usuario.cuentasBancarias() != null && !usuario.cuentasBancarias().isEmpty()) {
                String numeroCuenta = usuario.cuentasBancarias().get(0).getNombreCuenta();
                return new SimpleStringProperty(numeroCuenta);
            } else {
                return new SimpleStringProperty("Sin nobre");
            }
        });
        tcCuenta.setCellValueFactory(cellData -> {
            UsuarioDto usuario = cellData.getValue();
            if (usuario.cuentasBancarias() != null && !usuario.cuentasBancarias().isEmpty()) {
                String numeroCuenta = usuario.cuentasBancarias().get(0).getNumeCuenta();
                return new SimpleStringProperty(numeroCuenta);
            } else {
                return new SimpleStringProperty("Sin cuenta");
            }
        });
    }
    public  void cargarCuentas() {
        List<CuentaDto> cuentasNoAsignadas = asignarCuentaUserControllers.obtenerCuentasNoAsignadas();
        ObservableList<String> nombresCuentasNoAsignadas = FXCollections.observableArrayList(
                cuentasNoAsignadas.stream().map(CuentaDto::numeCuenta).collect(Collectors.toList())
        );
        cbCuenta.setItems(nombresCuentasNoAsignadas);
    }
    public  void cargarClientes() {
        List<UsuarioDto> usuarioDto = clientes.obtenerUsers();
        ObservableList<String> nombresClientes = FXCollections.observableArrayList(
                usuarioDto.stream().map(UsuarioDto::cedula).collect(Collectors.toList())
        );
        cmbCliente.setItems(nombresClientes);
    }

    public void buscarAcyion(ActionEvent actionEvent) {
        String textoBusqueda = txtBucarUser.getText().toLowerCase();
        List<UsuarioDto> usuariosFiltrados = clientes.obtenerUsers().stream()
                .filter(usuario -> {

                    boolean coincideConCedula = usuario.cedula().toLowerCase().contains(textoBusqueda);
                    boolean coincideConCuenta = usuario.cuentasBancarias().stream()
                            .anyMatch(cuenta -> cuenta.getNumeCuenta().toLowerCase().contains(textoBusqueda));

                    return coincideConCedula || coincideConCuenta;
                })
                .collect(Collectors.toList());
        listaUserCuen.setAll(usuariosFiltrados);
        tableusersCuenta.refresh();
    }

    public void agregarAsignacion(ActionEvent actionEvent) {
        String cedulaUsuario = cmbCliente.getValue().toString();
        String numeroCuenta = cbCuenta.getValue();

        boolean asignado = asignarCuentaUserControllers.asignarCuentaAUsuario(cedulaUsuario, numeroCuenta);

        if (asignado) {
            Usuario user = asignarCuentaUserControllers.obtenerUsuarioPorCedula(cedulaUsuario);
            UsuarioDto userDto = UrWalletMapper.INSTANCE.usuarioToUsuarioDto(user);
            listaUserCuen.add(userDto);
            tableusersCuenta.refresh();
            clientes.obtenerUsers();
            cargarCuentas();
            asignarCuentaUserControllers.mostrarMensaje("Éxito", "Cuenta asignada al usuario correctamente", Alert.AlertType.INFORMATION);
        } else {
            asignarCuentaUserControllers.mostrarMensaje("Error", "No se pudo asignar la cuenta", Alert.AlertType.ERROR);
        }
    }
}
