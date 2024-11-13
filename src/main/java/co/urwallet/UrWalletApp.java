package co.urwallet;

import co.urwallet.viewController.HomeViewsController;
import co.urwallet.viewController.RegistroUsersView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Image;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;


public class UrWalletApp  extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;

    @Override
    public void start(Stage stage) throws IOException {
        this.primaryStage = stage;
        this.primaryStage.setTitle("UrWallet");
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


    public static void main(String[] args) throws FileNotFoundException, MalformedURLException {
        launch();

        String pdfPath = "src/main/resources/co/urwallet/Pdf/presupuesto.pdf"; // donde se va a guaardar el pdf
        String imagePath = "src/main/resources/co/urwallet/img/logo.png";
        String imagePath1 = "src/main/resources/co/urwallet/img/grafica.png";

        // Crear el escritor de PDF
        PdfWriter pdfWriter = new PdfWriter(pdfPath);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);

        // Agregar logo
        ImageData imageData = ImageDataFactory.create(imagePath);
        com.itextpdf.layout.element.Image logo = new Image(imageData);
        logo.scaleToFit(100, 50);
        document.add(logo);

        // Agregar párrafo
        Paragraph paragraph = new Paragraph("Hola Usuario, Aquí encontrará una lista de todos sus movimientos y las gráficas de consumo.");
        document.add(paragraph);


        //Agregar gráfica
        ImageData imageData1 = ImageDataFactory.create(imagePath1);
        Image grafica = new Image(imageData1);
        grafica.scaleToFit(600, 300);
        document.add(grafica);
        // Cerrar el documento
        document.close();

        System.out.println("PDF creado exitosamente en: " + pdfPath);

    }
}
