package co.urwallet.viewController;
import co.urwallet.controller.AsistenteControllers;
import co.urwallet.mapping.dto.CuentaDto;
import co.urwallet.mapping.dto.TransaccionDto;
import co.urwallet.model.Usuario;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import co.urwallet.controller.ModelFactoryController;
import co.urwallet.model.Transaccion;
import javafx.fxml.FXML;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class AsistenteUsersViewControllers extends Thread {
    @FXML
    private TextArea chatArea;
    @FXML
    private TextField inputField;
    @FXML
    private Button sendButton;
    private Usuario usuario;
    private AsistenteControllers asistenteControllers;
    private final ModelFactoryController modelFactoryService;
    private final BlockingQueue<String> messageQueue;
    private volatile boolean isRunning;
    private static AsistenteUsersViewControllers instance;
    public static AsistenteUsersViewControllers getInstance() {
        return instance;
    }

    public void setUsuarioLogueado(Usuario usuarioLogueado) {
        asistenteControllers = new AsistenteControllers();
        usuario = usuarioLogueado;
        chatArea.appendText("Asistente: ¡Hola!" + usuario.getNombreCompleto() + " Soy tu asistente UrWallet financiero. ¿En qué puedo ayudarte hoy?\n"+
                "Nuestro menú disponible:\n"+
                "ahorro\n"+ "gasto\n"+ "inversión\n"+"saldo\n"+"consultar\n");
        sendButton.setOnAction(event -> handleSendAction());
        Thread chatThread = new Thread(this);
        chatThread.setDaemon(true);
        chatThread.start();

    }
    public AsistenteUsersViewControllers() {
        if (instance == null) {
            instance = this;
        }
        this.modelFactoryService = ModelFactoryController.getInstance();
        this.messageQueue = new LinkedBlockingQueue<>();
        this.isRunning = true;
    }
    @Override
    public void run() {
        while (isRunning) {
            try {
                String userMessage = messageQueue.take();
                String response = processUserInput(userMessage);
                Platform.runLater(() -> chatArea.appendText("Asistente: " + response + "\n"));
            } catch (InterruptedException e) {
                e.printStackTrace();
                stopRunning();
            }
        }
    }
    private void handleSendAction() {
        String userMessage = inputField.getText().trim();
        if (!userMessage.isEmpty()) {
            chatArea.appendText("Tú: " + userMessage + "\n");
            inputField.clear();
            messageQueue.add(userMessage);
        }
    }
    private String processUserInput(String userMessage) {
        if (userMessage.toLowerCase().contains("ahorro")) {
            return ahorro();
        } else if (userMessage.toLowerCase().contains("gasto")) {
            return gastos();
        } else if (userMessage.toLowerCase().contains("inversión")) {
            return inversiones();
        } else if (userMessage.toLowerCase().contains("saldo")) {
            return consultarSaldo();
        } else if (userMessage.toLowerCase().contains("consultar")) {
            return consultarTransferenciasRecibidas();
        } else if (userMessage.toLowerCase().contains("recomendación")) {
            return "Te sugiero ahorrar al menos el 20% de tus ingresos mensuales. Si necesitas más detalles, dime tu consulta.";
        } else {
            return "Lo siento, no entiendo tu consulta. ¿Puedes reformularla?\n"+ "Nuestro menú disponible:\n"+
                    "ahorro\n"+ "gasto\n"+ "inversión\n"+"saldo\n"+"consultar\n";
        }
    }

    private String ahorro() {
        List<CuentaDto> todasLasCuentas = asistenteControllers.obtenerCuenta();
        List<CuentaDto> cuentasUsers = todasLasCuentas.stream()
                .filter(cuenta -> usuario.getCedula().equals(cuenta.clienteId()))
                .toList();
        double totalIncome = cuentasUsers.stream()
                .mapToDouble(CuentaDto::saldo)
                .sum();
        double totalSavings = totalIncome * 0.2;

        return  usuario.getNombreCompleto() + ", tu ahorro sugerido es del 20% de tus ingresos totales, lo que equivale a $"
                + String.format("%.2f", totalSavings);
    }

    private String gastos() {
        List<TransaccionDto> transaccionesUsuario = asistenteControllers.obtenerTrasaccion().stream()
                .filter(transaccion ->
                        transaccion.cuentaOrigen() != null &&
                                transaccion.cuentaOrigen().getClienteId().equals(usuario.getCedula()))
                .toList();
        if (transaccionesUsuario.isEmpty()) {
            return usuario.getNombreCompleto() + ", no tienes gastos registrados. ¡Buen manejo de tus ingresos!";
        }
        Map<String, Double> gastosPorCategoria = transaccionesUsuario.stream()
                .collect(Collectors.groupingBy(
                        transaccion -> transaccion.categoria().name(),
                        Collectors.summingDouble(TransaccionDto::monto)
                ));
        StringBuilder gastosDetalles = new StringBuilder(usuario.getNombreCompleto() + ", tus gastos por categoría son:\n");
        gastosPorCategoria.forEach((categoria, monto) ->
                gastosDetalles.append(categoria).append(": $").append(String.format("%.2f", monto)).append("\n"));

        return gastosDetalles.toString();
    }

    private String inversiones() {
        List<CuentaDto> cuentasUsuario = asistenteControllers.obtenerCuenta().stream()
                .filter(cuenta -> cuenta.clienteId().equals(usuario.getCedula()))
                .toList();
        double totalBalance = cuentasUsuario.stream()
                .mapToDouble(CuentaDto::saldo)
                .sum();
        if (totalBalance > 10000) {
            return usuario.getNombreCompleto() + ": Tienes un buen balance de $"
                    + String.format("%.2f", totalBalance) + ". Considera invertir en fondos indexados o bienes raíces.";
        } else {
            return usuario.getNombreCompleto() + ": Tu balance es bajo ($"
                    + String.format("%.2f", totalBalance) + "). Te sugiero ahorrar más antes de invertir.";
        }
    }
    private String consultarSaldo() {
        List<CuentaDto> cuentasUsuario = asistenteControllers.obtenerCuenta().stream()
                .filter(cuenta -> cuenta.clienteId().equals(usuario.getCedula()))
                .toList();
        double saldoTotal = cuentasUsuario.stream()
                .mapToDouble(CuentaDto::saldo)
                .sum();

        return usuario.getNombreCompleto() + ": Tu saldo total es de $" + String.format("%.2f", saldoTotal);
    }
    private String consultarTransferenciasRecibidas() {
        List<CuentaDto> cuentasUsuario = asistenteControllers.obtenerCuenta().stream()
                .filter(cuenta -> cuenta.clienteId().equals(usuario.getCedula()))
                .toList();
        List<Transaccion> transferenciasRecibidas = modelFactoryService.getUrWallet().getListaTransaccion().stream()
                .filter(transaccion -> cuentasUsuario.stream()
                        .anyMatch(cuenta -> cuenta.numeCuenta().equals(transaccion.getCuentaDestino().getNumeCuenta())))
                .toList();

        if (transferenciasRecibidas.isEmpty()) {
            return usuario.getNombreCompleto() + ", no se encontraron transferencias recibidas.";
        }
        StringBuilder resumen = new StringBuilder("Transferencias recibidas:\n");
        for (Transaccion transaccion : transferenciasRecibidas) {
            CuentaDto cuentaOrigen = asistenteControllers.obtenerCuenta().stream()
                    .filter(cuenta -> cuenta.numeCuenta().equals(transaccion.getCuentaOrigen().getNumeCuenta()))
                    .findFirst()
                    .orElse(null);

            String nombreOrigen = cuentaOrigen != null ? cuentaOrigen.clienteId() : "Usuario desconocido";
            resumen.append("De: ").append(nombreOrigen)
                    .append(" (Cuenta: ").append(transaccion.getCuentaOrigen().getNumeCuenta())
                    .append(") - Monto: $").append(String.format("%.2f", transaccion.getMonto()))
                    .append("\n");
        }

        return resumen.toString();
    }
    public void stopRunning() {
        isRunning = false;
    }

}
