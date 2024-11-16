package co.urwallet.controller;

import co.urwallet.UrWalletApp;
import co.urwallet.controller.service.IModelFactoryControllerService;
import co.urwallet.exceptions.CuentaException;
import co.urwallet.exceptions.LoginException;
import co.urwallet.exceptions.TransaccionException;
import co.urwallet.exceptions.UsuarioException;
import co.urwallet.mapping.dto.CuentaDto;
import co.urwallet.mapping.dto.LoginDto;
import co.urwallet.mapping.dto.TransaccionDto;
import co.urwallet.mapping.dto.UsuarioDto;
import co.urwallet.mapping.mappers.UrWalletMapper;
import co.urwallet.model.Cuenta;
import co.urwallet.model.Transaccion;
import co.urwallet.model.UrWallet;
import co.urwallet.model.Usuario;
import co.urwallet.utils.Persistencia;
import co.urwallet.utils.UrWalletUtils;
import co.urwallet.viewController.PrincipalUserViewsControllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public class ModelFactoryController implements IModelFactoryControllerService {
    UrWallet urWallet;
    UrWalletMapper mapper = UrWalletMapper.INSTANCE;
    private final ClientController clientController;

    private static class SingletonHolder {
        private final static ModelFactoryController eINSTANCE = new ModelFactoryController();
    }

    public static ModelFactoryController getInstance() {
        return SingletonHolder.eINSTANCE;
    }

    public ModelFactoryController() {
        urWallet = new UrWallet();
//        cargarDatosBase();
        clientController = new ClientController();
        clientController.connect(); // Conectar al servidor
//        guardarPerUsers();

        cargarDatosDesdeArchivos();
        cargarDatosArchivosCuentas();
        cargarDatosArchivosTransacciones();
//        guardarRecursourWalletBinario();
//        cargarRecursoUrWalletBinario();
//        guardarRecursoBancoXML();

//        copiarArchivos();
//        cargarRecursoUrWalletXML();
//        if (urWallet == null) {
//            cargarDatosBase();
////            guardarRecursoBancoXML();
//        }
        guardaRegistroLog("Inicio de sesión", 1, "inicioSesión");

    }


    public void enviarTransferencia(String mensaje) {
        clientController.sendMessage(mensaje);
    }
    private void cargarDatosDesdeArchivos() {
        urWallet = new UrWallet();
        try {
            Persistencia.cargarDatosArchivos(urWallet);
            guardaRegistroLog("Se han cargado los datos correctamente", 1, "Get");
        } catch (IOException e) {
            guardaRegistroLog("No se han cargado los datos", 2, "Warning");
            throw new RuntimeException(e);
        }
    }

    private void cargarDatosArchivosCuentas(){
        try {
            Persistencia.cargarDatosArchivosCuentas(urWallet);
            guardaRegistroLog("Se han cargado las cuentas", 1, "Get");
        }catch (IOException e){
            guardaRegistroLog("No se han cargado las cuentas", 2, "Warning");
            throw new RuntimeException(e);
        }
    }
    private void cargarDatosArchivosTransacciones(){
        try {
            Persistencia.cargarDatosArchivosTransacciones(urWallet);
            guardaRegistroLog("Se han cargado las cuentas", 1, "Get");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void guardaRegistroLog(String mensajeLog, int nivel, String accion) {
        Persistencia.guardaRegistroLog(mensajeLog, nivel, accion);
    }

    private void guardarRecursourWalletBinario() {
        Persistencia.guardarRecursourWalletBinario(urWallet);
    }

    private void cargarRecursoUrWalletBinario() {
        urWallet = Persistencia.cargarRecursoUrWalletBinario();
    }

    private void cargarRecursoUrWalletXML() {
        urWallet = Persistencia.cargarRecursoUrWalletXML();
    }

    private void guardarRecursoBancoXML() {
        Persistencia.guardarRecursoBancoXML(urWallet);
    }

    private void copiarArchivos() {
        try {
            Persistencia.copiarArchivos();
            guardaRegistroLog("Se creo una copia en respaldo", 1, "Copy");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void cargarDatosBase() {
        urWallet = UrWalletUtils.inicializarDatos();
    }
    @Override
    public UrWallet getUrWallet() {
        return urWallet;
    }
    @Override
    public Cuenta obtenerCuentaPorId(String id) {
        return getUrWallet().obtenerCuenta(id);
    }

    @Override
    public List<UsuarioDto> obtenerUser() {
        return mapper.getUsuariosDto(urWallet.getListaUsers());
    }
    @Override
    public List<CuentaDto> obtenerCuentas() {
        return mapper.getCuentaDto(urWallet.getListaCuentas());
    }

    @Override
    public List<TransaccionDto> obtenerTrasaccion() {
        return mapper.gettransaccionesToTransaccionesDto(urWallet.getListaTransaccion());
    }

    private void agregarDatosGenerales(){
        guardarPerUsers();
        guardarPerCuentas();
        guardarRecursourWalletBinario();
        guardarRecursoBancoXML();
    }
    @Override
    public boolean agregarUsers(UsuarioDto usuarioDto) {
        try {
            System.out.println("MODELLLL" + usuarioDto.correo());
            if (!urWallet.verificarUsuarioExistente(usuarioDto.cedula())) {
                Usuario user = mapper.usuarioDtoToUsuario(usuarioDto);
                System.out.println("MODELLLL22" + user.getCorreo());

                getUrWallet().agregarUsuario(user);
                agregarDatosGenerales();
                guardaRegistroLog("Se han agregado un nuevo usuario", 1, "Create");
                copiarArchivos();
            }
            return true;
        } catch (UsuarioException e) {
            e.getMessage();
            guardaRegistroLog("No se ha creado", 2, "Warning");
            return false;
        }
    }

    public void guardarPerUsers() {
        try {
            Persistencia.guardarUsuario(getUrWallet().getListaUsers());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void guardarPerCuentas() {
        try{
            Persistencia.guardarCuentas(getUrWallet().getListaCuentas());
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public void guardarTransferencia() {
        try {
            Persistencia.guardarTransacciones(getUrWallet().getListaTransaccion());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Usuario iniciarSesion(LoginDto loginDto) throws LoginException {
        Usuario users = getUrWallet().buscarUserLogin(loginDto.email());
        if (users != null && users.getContrasena().equals(loginDto.contrasena())) {
            guardaRegistroLog("Se ha iniciado corecctamente la sesión", 1, "Get");
            return users;
        } else {
            guardaRegistroLog("Email o contraseña incorrecta", 2, "Warning");
            throw new LoginException("Email o contraseña incorrecta");
        }
    }

    @Override
    public boolean actualizarUser(String idUser, UsuarioDto usuarioDto) {
        try {
            System.out.println("actualizar" + idUser);
            Usuario user = mapper.usuarioDtoToUsuario(usuarioDto);
            System.out.println("MQAPERRRR ID" + user.getIdUsuario());
            getUrWallet().actualizarUsuario(idUser, user);
            agregarDatosGenerales();
            guardaRegistroLog("Se han actualizado correctamente los datos", 1, "Update");
//            copiarArchivos();
            return true;
        } catch (UsuarioException e) {
            e.getMessage();
            guardaRegistroLog("No se pudo actualizar el dato", 2, "Warning");
            return false;
        }
    }

    @Override
    public boolean eliminarUsuario(String idUser) {
        boolean flagExiste = false;
        try {
            flagExiste = getUrWallet().eliminarUsuario(idUser);
            agregarDatosGenerales();
            guardaRegistroLog("Se ha eliminado el usuario", 1, "Delete");
//            copiarArchivos();
        } catch (UsuarioException e) {
            guardaRegistroLog("No se pudo eliminar", 2, "Warning");
            throw new RuntimeException(e);
        }
        return flagExiste;
    }
    @Override
    public boolean agregarCuenta(CuentaDto cuentaDto) {
        try {
            System.out.println("MODELLLL" + cuentaDto.numeCuenta());
            if (!urWallet.verificarCuentaExistente(cuentaDto.numeCuenta())) {
                Cuenta cuenta = mapper.cuentaToCuentaDto(cuentaDto);
                System.out.println("MODELLLL22" + cuenta.getIdCuenta());
                getUrWallet().agregarCuenta(cuenta);
                agregarDatosGenerales();
                guardaRegistroLog("Se ha agregado un nuevo cuenta", 1, "Create");
                copiarArchivos();
            }
            return true;
        } catch (CuentaException e) {
            e.getMessage();
            guardaRegistroLog("No se ha agregado un nuevo cuenta", 2, "Warning");
            return false;
        }
    }


    @Override
    public boolean actualizarCuenta(String idCuenta, CuentaDto cuentaDto) {
        try {
            System.out.println("actualizar" + idCuenta);
            Cuenta cuenta = mapper.cuentaToCuentaDto(cuentaDto);
            System.out.println("MQAPERRRR ID" + cuenta.getNumeCuenta());
            getUrWallet().actualizarCuenta(idCuenta, cuenta);
            agregarDatosGenerales();
            copiarArchivos();
            guardaRegistroLog("Se ha actualizado la cuenta", 1, "Update");
            return true;
        } catch (CuentaException e) {
            e.getMessage();
            guardaRegistroLog("No se pudo actualizar el dato", 2, "Warning");
            return false;
        }
    }
    @Override
    public boolean eliminarCuenta(String idCuenta) {
        boolean flagExiste = false;
        try {
            flagExiste = getUrWallet().eliminarCuenta(idCuenta);
            agregarDatosGenerales();
            guardaRegistroLog("Se ha eliminado la cuenta", 1, "Delete");
//            copiarArchivos();
        } catch (CuentaException e) {
            guardaRegistroLog("No se pudo eliminar la cuenta", 2, "Warning");
            throw new RuntimeException(e);
        }
        return flagExiste;
    }

    public void shutdownClient() {
        clientController.disconnect(); // Cerrar conexión con el servidor
    }
    @Override
    public boolean agregarTrasaccion(TransaccionDto transaccionDto) {
        try {
            System.out.println("HOILAAAAAAAAAAAAAAAAAAAAAAA AAAAA" + transaccionDto.cuentaOrigen());
//            if (!urWallet.verificarCuentaExistenteTrans(transaccionDto.cuentaOrigen())) {
//                if (!urWallet.verificarCuentaExistenteTrans(transaccionDto.cuentaDestino())) {
                    Transaccion transaccion = mapper.transaccionToTransaccionDto(transaccionDto);
                    System.out.println("transss ferererere" + transaccion.getIdTransaccion());
                    getUrWallet().agregarTransaccion(transaccion);
                    guardarTransferencia();
//            String mensaje = String.format(
//                    "Nueva transferencia creada:\nCuenta Origen: %s\nCuenta Destino: %s\nMonto: %.2f\nFecha: %s",
//                    transaccionDto.cuentaOrigen().getNumeCuenta(),
//                    transaccionDto.cuentaDestino().getNumeCuenta(),
//                    transaccionDto.monto(),
//                    transaccionDto.fecha()
//            );
            enviarTransferencia(transaccionDto.toString());
//                }
//            }
            return true;
        } catch (TransaccionException e) {
            e.getMessage();
            return false;
        }
    }
    @Override
    public void SumarSaldo(TransaccionDto transaccionDto) {
        try {
            if (!urWallet.verificarCuentaExistenteTrans(transaccionDto.cuentaOrigen().getNumeCuenta())) {
                Cuenta cuenta = getUrWallet().obtenerCuenta(transaccionDto.cuentaOrigen().getNumeCuenta());
                getUrWallet().agregarPrecioACuenta(transaccionDto.monto(), cuenta);
            }
        } catch (TransaccionException e) {
            e.getMessage();
        }
    }
    @Override
    public void RestarSaldo(TransaccionDto transaccionDto) {
        try {
            if (!urWallet.verificarCuentaExistenteTrans(transaccionDto.cuentaOrigen().getNumeCuenta())) {
                Cuenta cuenta = getUrWallet().obtenerCuenta(transaccionDto.cuentaOrigen().getNumeCuenta());

                getUrWallet().restarPrecioACuenta(transaccionDto.monto(), cuenta);
                System.out.println(cuenta.getNumeCuenta() + cuenta.getSaldo());
            }
        } catch (TransaccionException e) {
            e.getMessage();
        }
    }

    public FXMLLoader navegarVentana(String nombreArchivoFxml, String tituloVentana, Usuario usuarioLogueado) {
        FXMLLoader loader = null;
        try {
            URL fxmlLocation = UrWalletApp.class.getResource(nombreArchivoFxml);

            if (fxmlLocation == null) {
                guardaRegistroLog("No se pudo localizar el archivo FXML", 2, "Warning");
                throw new IOException("No se pudo localizar el archivo FXML: " + nombreArchivoFxml);
            }

            loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            // Obtener el controlador de la siguiente ventana
            Object controller = loader.getController();

            // Si el controlador es de la pantalla HomeUser, le pasamos el usuario logueado
            if (controller instanceof PrincipalUserViewsControllers) {
                ((PrincipalUserViewsControllers) controller).setUsuarioLogueado(usuarioLogueado);
            }
//            if (controller instanceof HomeViewsUsers) {
//                ((HomeViewsUsers) controller).setUsuarioLogueado(usuarioLogueado);
//            }
//            if (controller instanceof AsistenteUsersViewControllers) {
//                ((AsistenteUsersViewControllers) controller).setUsuarioLogueado(usuarioLogueado);
//            }
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle(tituloVentana);
            stage.show();
            guardaRegistroLog("Se ha Ingresado a una nueva pantalla", 1, "New");

        } catch (IOException e) {
            e.printStackTrace();
            mostrarMensaje("Error al cargar la ventana", "No se pudo cargar " + nombreArchivoFxml + ": " + e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarMensaje("Error inesperado", "Ocurrió un error al cargar la ventana: " + e.getMessage(), Alert.AlertType.ERROR);
        }
        return loader;
    }

    @Override
    public boolean asignarCuentaAUsuario(String cedulaUsuario, String numeroCuenta) {

        Usuario usuario = urWallet.obtenerUsuarioPorCedula(cedulaUsuario);
        if (usuario == null) {
            return false;
        }

        Cuenta cuenta = urWallet.obtenerCuenta(numeroCuenta);
        if (cuenta == null) {
            return false;
        }

        if (urWallet.cuentaYaAsignada(numeroCuenta)) {
            return false;
        }

        usuario.agregarCuenta(cuenta);
        agregarDatosGenerales();
        return true;
    }
    @Override
    public List<CuentaDto> obtenerCuentasNoAsignadas() {
        List<CuentaDto> todasLasCuentas = obtenerCuentas();
        return todasLasCuentas.stream()
                .filter(cuenta -> !urWallet.cuentaYaAsignada(cuenta.numeCuenta()))
                .collect(Collectors.toList());
    }
    @Override
    public Usuario obtenerUsuarioPorCedula(String cedula) {
        Usuario usuario = urWallet.obtenerUsuarioPorCedula(cedula);
        return usuario;
    }

    @Override
    public void mostrarMensaje(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @Override
    public void cerrarVentana(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

}
