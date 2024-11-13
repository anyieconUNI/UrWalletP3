package co.urwallet.viewController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

public class SaldoClienteViewsControllers {

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
        mostrarPanelSaldo(); // Muestra el panel de saldo al iniciar
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

}