package co.urwallet.controller;

import co.urwallet.controller.service.IModelFactoryControllerService;
import co.urwallet.controller.service.ISaldoControllerService;
import co.urwallet.mapping.dto.PresupuestoDto;
import co.urwallet.model.Presupuesto;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

import java.util.List;

public class SaldoClienteController implements ISaldoControllerService {
    IModelFactoryControllerService modelFactoryService;

    public List<PresupuestoDto> obtenerPresupuesto(){return modelFactoryService.obtenerPresupuestos();}
    public SaldoClienteController() {
        modelFactoryService = ModelFactoryController.getInstance();
    }

    @Override
    public boolean agregarPresupuesto(PresupuestoDto presupuestoDto) {
        return modelFactoryService.agregarPresupuesto(presupuestoDto);
    }

    @Override
    public boolean actualizarPresupuesto(String idPresupuesto, PresupuestoDto presupuestoDto) {
        return modelFactoryService.actualizarPresupuesto(idPresupuesto, presupuestoDto);
    }

    @Override
    public boolean eliminarPresupuesto(String idPresupuesto) {
        return modelFactoryService.eliminarPresupuesto(idPresupuesto);
    }

    @Override
    public void cerrarVentana(ActionEvent actionEvent) {
        modelFactoryService.cerrarVentana(actionEvent);
    }

    @Override
    public void mostrarMensaje(String titulo, String mensaje, Alert.AlertType tipo) {
        modelFactoryService.mostrarMensaje(mensaje, titulo, tipo);
    }
}
