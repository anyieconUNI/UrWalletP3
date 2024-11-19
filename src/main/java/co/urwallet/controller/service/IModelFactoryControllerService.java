package co.urwallet.controller.service;

import co.urwallet.exceptions.LoginException;
import co.urwallet.mapping.dto.*;
import co.urwallet.model.Cuenta;
import co.urwallet.model.UrWallet;
import co.urwallet.model.Usuario;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

import java.util.List;

public interface IModelFactoryControllerService {


    void enviarSolicitud(String msg);

    UrWallet getUrWallet();

    Cuenta obtenerCuentaPorId(String id);

    List<UsuarioDto> obtenerUser();

    List<CuentaDto> obtenerCuentas();

    List<TransaccionDto> obtenerTrasaccion();

    List<PresupuestoDto> obtenerPresupuestos();

    boolean agregarUsers(UsuarioDto usuarioDto, boolean isSocket);

    boolean agregarUsersSocket(UsuarioDto usuarioDto, boolean isSocket);

    Usuario iniciarSesion(LoginDto loginDto) throws LoginException;

    boolean actualizarUser(String idUser, UsuarioDto usuarioDto);

    boolean eliminarUsuario(String idUser);

    boolean agregarCuenta(CuentaDto cuentaDto, boolean isSocket);

    boolean actualizarCuenta(String idCuenta, CuentaDto cuentaDto);

    boolean eliminarCuenta(String idCuenta);

    boolean agregarTrasaccion(TransaccionDto transaccionDto);

    boolean agregarTrasaccionDeServidor(TransaccionDto transaccionDto);

    void SumarSaldo(TransaccionDto transaccionDto);

    void RestarSaldo(TransaccionDto transaccionDto);

    boolean asignarCuentaAUsuario(String cedulaUsuario, String numeroCuenta);

    List<CuentaDto> obtenerCuentasNoAsignadas();

    Usuario obtenerUsuarioPorCedula(String cedula);

    void mostrarMensaje(String titulo, String mensaje, Alert.AlertType tipo);

    void cerrarVentana(ActionEvent actionEvent);

    boolean agregarPresupuesto(PresupuestoDto presupuestoDto);

    boolean actualizarPresupuesto(String idPresupuesto, PresupuestoDto presupuestoDto);

    boolean eliminarPresupuesto(String idPresupuesto);
}
