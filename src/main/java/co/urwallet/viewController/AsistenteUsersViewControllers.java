package co.urwallet.viewController;
import co.urwallet.model.Usuario;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import co.urwallet.controller.ModelFactoryController;
import co.urwallet.model.Transaccion;
import javafx.fxml.FXML;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class AsistenteUsersViewControllers implements Runnable {
    @FXML
    private TextArea chatArea;
    @FXML
    private TextField inputField;
    @FXML
    private Button sendButton;
    private Usuario usuario;
    private final ModelFactoryController modelFactoryService;
    private final BlockingQueue<String> messageQueue;
    private volatile boolean isRunning;


    public void setUsuarioLogueado(Usuario usuarioLogueado) {
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
        double totalIncome = usuario.getCuentasBancarias().stream()
                .mapToDouble(cuenta -> cuenta.getSaldo())
                .sum();
        double totalSavings = totalIncome * 0.2;

        return  usuario.getNombreCompleto() + ", tu ahorro sugerido es del 20% de tus ingresos totales, lo que equivale a $"
                + String.format("%.2f", totalSavings);
    }

    private String gastos() {
        var usuarios = modelFactoryService.getUrWallet().getListaUsers().stream()
                .filter(user -> user.getCedula().equals(usuario.getCedula()))
                .findFirst()
                .orElse(null);

        Map<String, Double> gastosPorCategoria = modelFactoryService.getUrWallet().getListaTransaccion().stream()
                .filter(transaccion ->
                        usuarios.getCuentasBancarias().contains(transaccion.getCuentaOrigen()) ||
                                usuarios.getCuentasBancarias().contains(transaccion.getCuentaDestino()))
                .collect(Collectors.groupingBy(
                        transaccion -> transaccion.getCategoria().name(),
                        Collectors.summingDouble(Transaccion::getMonto)
                ));

        if (gastosPorCategoria.isEmpty()) {
            return usuario.getNombreCompleto() + " no tienes gastos registrados, Tienes un buen manejo de tus ingresos";
        }
        StringBuilder gastosDetalles = new StringBuilder(usuario.getNombreCompleto() +" Tus gastos son: " +  ":\n");
        gastosPorCategoria.forEach((categoria, monto) ->
                gastosDetalles.append(categoria).append(": $").append(String.format("%.2f", monto)).append("\n"));
        return gastosDetalles.toString();
    }

    private String inversiones() {
        var usuarios = modelFactoryService.getUrWallet().getListaUsers().stream()
                .filter(user -> user.getCedula().equals(usuario.getCedula()))
                .findFirst()
                .orElse(null);

        if (usuarios == null) {
            return "No se encontró un usuario con la cédula proporcionada.";
        }

        double totalBalance = usuarios.getCuentasBancarias().stream()
                .mapToDouble(cuenta -> cuenta.getSaldo())
                .sum();

        if (totalBalance > 10000) {
            return  usuario.getNombreCompleto() + ": Tienes un buen balance de $"
                    + String.format("%.2f", totalBalance) + ". Considera invertir en fondos indexados o bienes raíces.";
        } else {
            return  usuario.getNombreCompleto() + ": Tu balance es bajo ($"
                    + String.format("%.2f", totalBalance) + "). Te sugiero ahorrar más antes de invertir.";
        }
    }
    private String consultarSaldo() {
        var usuarios = modelFactoryService.getUrWallet().getListaUsers().stream()
                .filter(user -> user.getCedula().equals(usuario.getCedula()))
                .findFirst()
                .orElse(null);

        if (usuarios == null) {
            return "No se encontró un usuario con la cédula proporcionada.";
        }

        double saldoTotal = usuarios.getCuentasBancarias().stream()
                .mapToDouble(cuenta -> cuenta.getSaldo())
                .sum();

        return  usuario.getNombreCompleto() + ": Tu saldo total es de $" + String.format("%.2f", saldoTotal);
    }
    private String consultarTransferenciasRecibidas() {
        var usuarios = modelFactoryService.getUrWallet().getListaUsers().stream()
                .filter(user -> user.getCedula().equals(usuario.getCedula()))
                .findFirst()
                .orElse(null);

        if (usuarios == null) {
            return "No se encontró un usuario con la cédula proporcionada.";
        }

        var cuentasDelUsuario = usuarios.getCuentasBancarias();
        var transferenciasRecibidas = modelFactoryService.getUrWallet().getListaTransaccion().stream()
                .filter(transaccion -> cuentasDelUsuario.contains(transaccion.getCuentaDestino()))
                .collect(Collectors.toList());

        if (transferenciasRecibidas.isEmpty()) {
            return  usuario.getCedula() + " ,No se encontraron transferencias recibidas .";
        }
        StringBuilder resumen = new StringBuilder("Transferencias recibidas:\n");
        for (var transaccion : transferenciasRecibidas) {
            var cuentaOrigen = transaccion.getCuentaOrigen();
            var usuarioOrigen = modelFactoryService.getUrWallet().getListaUsers().stream()
                    .filter(user -> user.getCuentasBancarias().contains(cuentaOrigen))
                    .findFirst()
                    .orElse(null);

            String nombreOrigen = usuarioOrigen != null ? usuarioOrigen.getNombreCompleto() : "Usuario desconocido";
            resumen.append("De: ").append(nombreOrigen)
                    .append(" (Cuenta: ").append(cuentaOrigen.getIdCuenta())
                    .append(") - Monto: $").append(String.format("%.2f", transaccion.getMonto()))
                    .append("\n");
        }

        return resumen.toString();
    }
    public void stopRunning() {
        isRunning = false;
    }

}
