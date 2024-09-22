package co.urwallet;

import co.urwallet.viewController.HomeViewsController;
import co.urwallet.viewController.RegistroUsersView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class UrWalletApp  extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;

    @Override
    public void start(Stage stage) throws IOException {
        this.primaryStage = stage;
        this.primaryStage.setTitle("Banco UQ");
        mostrarVentanaPrincipal();
    }

    public void mostrarVentanaPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(UrWalletApp.class.getResource("Home.fxml"));
            AnchorPane rootLayout = (AnchorPane) loader.load();
            HomeViewsController clienteViewController = loader.getController();
            // Mostrar la escena que contiene el layout principal.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
