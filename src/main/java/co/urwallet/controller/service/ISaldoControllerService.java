package co.urwallet.controller.service;

import co.urwallet.mapping.dto.PresupuestoDto;
import co.urwallet.model.Presupuesto;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

public interface ISaldoControllerService {
    boolean agregarPresupuesto(PresupuestoDto presupuestoDto);

    boolean actualizarPresupuesto(String idPresupuesto, PresupuestoDto presupuestoDto);

    boolean eliminarPresupuesto(String idPresupuesto);

    void cerrarVentana(ActionEvent actionEvent);

    void mostrarMensaje(String titulo, String mensaje, Alert.AlertType tipo);



}
