package co.urwallet.viewController;

import co.urwallet.controller.CuentaControllers;
import co.urwallet.controller.NoficaControllers;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class SolicitudesAdmnViewControllers {
    @FXML
    public TextArea SoliTArea;
    CuentaControllers cuentaControllers;
    private static SolicitudesAdmnViewControllers instance;
    public SolicitudesAdmnViewControllers() {
        if (instance == null) {
            instance = this;
        }
    }

    public static SolicitudesAdmnViewControllers getInstance() {
        return instance;
    }
    @FXML
    private void initialize() {
        cuentaControllers = new CuentaControllers();
    }

    public void recibirSolicitud(String mensaje) {
        SoliTArea.appendText(mensaje);
    }
}
