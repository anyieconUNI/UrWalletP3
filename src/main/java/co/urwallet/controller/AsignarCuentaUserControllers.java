package co.urwallet.controller;

import co.urwallet.controller.service.AsignarCuentaUserService;
import co.urwallet.controller.service.IModelFactoryControllerService;
import co.urwallet.mapping.dto.CuentaDto;
import co.urwallet.mapping.dto.UsuarioDto;
import co.urwallet.model.Usuario;
import co.urwallet.viewController.AsignarCuentaUserViewControllers;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

import java.util.List;

public class AsignarCuentaUserControllers implements AsignarCuentaUserService {
    IModelFactoryControllerService modelFactoryService;

    public AsignarCuentaUserControllers() {
        modelFactoryService = ModelFactoryController.getInstance();
    }

    CuentaControllers cuentas = new CuentaControllers();
    UsersController clientes = new UsersController();
    @Override
    public List<CuentaDto> obtenerCuentas() {
        List<CuentaDto> cuentasDos = cuentas.obtenerCuenta();
        System.out.println("Clientes : " + cuentasDos);
        AsignarCuentaUserViewControllers.getInstance().cargarCuentas();
        return cuentasDos;
    }
    @Override
    public List<UsuarioDto> obtenerUsuario() {
        List<UsuarioDto> usuariosDos = clientes.obtenerUsers();
        System.out.println("Clientes : " + usuariosDos);
        AsignarCuentaUserViewControllers.getInstance().cargarCuentas();
        return usuariosDos;
    }
    @Override
    public void mostrarMensaje(String mensaje, String titulo, Alert.AlertType tipo) {
        modelFactoryService.mostrarMensaje(mensaje, titulo, tipo);
    }
    @Override
    public void cerrarVentana(ActionEvent actionEvent) {
        modelFactoryService.cerrarVentana(actionEvent);
    }

    public boolean asignarCuentaAUsuario(String cedulaUsuario, String numeroCuenta){
        return modelFactoryService.asignarCuentaAUsuario(cedulaUsuario, numeroCuenta);
    }
    public List<CuentaDto> obtenerCuentasNoAsignadas() {
        return modelFactoryService.obtenerCuentasNoAsignadas();
    }
    public Usuario obtenerUsuarioPorCedula(String cedulaUsuario){
        return  modelFactoryService.obtenerUsuarioPorCedula(cedulaUsuario);
    }

}
