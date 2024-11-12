package co.urwallet.viewController;

import co.urwallet.controller.ModelFactoryController;
import co.urwallet.controller.service.IModelFactoryControllerService;
import co.urwallet.mapping.dto.UsuarioDto;
import co.urwallet.model.Categoria;
import co.urwallet.model.Transaccion;
import co.urwallet.model.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EstadistecasViewsControllers {
    private final IModelFactoryControllerService modelFactoryService;

    @FXML
    private ComboBox<String> cmbEstadistica;

    @FXML
    private BarChart<String, Number> barChart;

    public EstadistecasViewsControllers() {
        modelFactoryService = ModelFactoryController.getInstance();
    }

    @FXML
    public void buscarAction(ActionEvent event) {
        String seleccion = cmbEstadistica.getValue();
        barChart.getData().clear();

        if ("Gastos mas comunes".equals(seleccion)) {
            mostrarGastosMasComunes();
        } else if ("Usuarios con más transacciones".equals(seleccion)) {
            mostrarUsuariosConMasTransacciones();
        } else if ("Saldo promedio de usuarios".equals(seleccion)) {
            mostrarSaldoPromedioUsuarios();
        } else {
            modelFactoryService.mostrarMensaje("Selección no válida", "Error", javafx.scene.control.Alert.AlertType.WARNING);
        }
    }

    private void mostrarGastosMasComunes() {
        Map<Categoria, Double> gastosPorCategoria = modelFactoryService.getUrWallet().getListaTransaccion().stream()
                .collect(Collectors.groupingBy(
                        Transaccion::getCategoria,
                        Collectors.summingDouble(Transaccion::getMonto)
                ));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Gastos por Categoría");

        gastosPorCategoria.forEach((categoria, monto) ->
                series.getData().add(new XYChart.Data<>(categoria.name(), monto))
        );

        barChart.getData().add(series);
    }

    private void mostrarUsuariosConMasTransacciones() {
        Map<String, Long> transaccionesPorUsuario = modelFactoryService.getUrWallet().getListaTransaccion().stream()
                .collect(Collectors.groupingBy(
                        transaccion -> {
                            Usuario usuario = modelFactoryService.getUrWallet().getListaUsers().stream()
                                    .filter(user -> user.getCuentasBancarias().contains(transaccion.getCuentaOrigen()))
                                    .findFirst()
                                    .orElse(null);
                            return (usuario != null) ? usuario.getNombreCompleto() : "Usuario Desconocido";
                        },
                        Collectors.counting()
                ));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Usuarios con Más Transacciones");
        transaccionesPorUsuario.forEach((usuario, cantidadTransacciones) ->
                series.getData().add(new XYChart.Data<>(usuario, cantidadTransacciones))
        );

//        barChart.getData().clear();
        barChart.getData().add(series);
    }


    private void mostrarSaldoPromedioUsuarios() {
        //sacamos el saldo promedio
        double saldoPromedio = modelFactoryService.obtenerUser().stream()
                .mapToDouble(usuario -> usuario.saldoDispo().doubleValue())
                .average()
                .orElse(0.0);

        Map<String, Double> saldoPorUsuario = modelFactoryService.obtenerUser().stream()
                .collect(Collectors.toMap(
                        UsuarioDto::nombreCompleto,
                        usuario -> usuario.saldoDispo().doubleValue()
                ));
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Saldo por Usuario");
        saldoPorUsuario.forEach((nombre, saldo) ->
                series.getData().add(new XYChart.Data<>(nombre, saldo))
        );
        barChart.getData().add(series);
        modelFactoryService.mostrarMensaje(
                "Saldo Promedio",
                "Saldo promedio de usuarios: " + saldoPromedio,
                javafx.scene.control.Alert.AlertType.INFORMATION
        );
    }
}
