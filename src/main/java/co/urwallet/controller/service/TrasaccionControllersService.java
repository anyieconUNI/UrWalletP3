package co.urwallet.controller.service;

import co.urwallet.mapping.dto.CuentaDto;
import co.urwallet.mapping.dto.TransaccionDto;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

import java.util.List;

public interface TrasaccionControllersService {
    boolean agregarTrasaccion(TransaccionDto transaccionDto);

    //    public List<CuentaDto> obtenerCuenta(){
//        return modelFactoryService.obtenerCuentas();
//    }

    //    public List<CuentaDto> obtenerCuenta(){
//        return modelFactoryService.obtenerCuentas();
//    }
    List<CuentaDto> obtenerCuentas();

    void SumarSaldo(TransaccionDto transaccionDto);

    void RestarrSaldo(TransaccionDto transaccionDto);

    void mostrarMensaje(String mensaje, String titulo, Alert.AlertType tipo);

    void cerrarVentana(ActionEvent actionEvent);
}
