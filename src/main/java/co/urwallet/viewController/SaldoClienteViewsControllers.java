package co.urwallet.viewController;

import co.urwallet.model.Mes;
import co.urwallet.model.Transaccion;
import co.urwallet.model.UrWallet;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;


public class SaldoClienteViewsControllers implements Initializable {

    private Mes mesSeleccionado;
    private ObservableList<Transaccion> listaTransacciones = FXCollections.observableArrayList();



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
    private ComboBox cmbMes;

    @FXML
    private Button btnReporte;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inicializarMes();
        mostrarPanelSaldo();
    }

    public void inicializarMes() {
        cmbMes.getItems().addAll(Mes.values());
        listenerMes();
    }

    public void listenerMes() {
        cmbMes.setOnAction(event -> {
            mesSeleccionado = (Mes) cmbMes.getValue();
        });
    }

    // Crud presuuesto
    // id, nombre, MontoGasto, montoTotal, Categoria


    /*
    public ObservableList<Transaccion> getListaTransacciones() {
        return listaTransacciones();
    }



    public void generaReporte(ActionEvent actionEvent) throws FileNotFoundException, MalformedURLException {
        String pdfPath = "src/main/resources/Pdf/presupuesto.pdf";
        String imagePath = "src/main/resources/co/urwallet/img/logo.png";

        PdfWriter pdfWriter = new PdfWriter(pdfPath);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);

        ImageData imageData = ImageDataFactory.create(imagePath);
        Image logo = new Image(imageData);
        logo.scaleToFit(100, 50);
        document.add(logo);

        // Título
        Paragraph paragraph = new Paragraph("Reporte de Transacciones");
        paragraph.setBold();
        paragraph.setFontSize(14);
        document.add(paragraph);

        // Agrega la tabla al PDF
        Table table = new Table(7);
        table.addCell("Fecha");
        table.addCell("Tipo");
        table.addCell("Monto");
        table.addCell("Descripción");
        table.addCell("Cuenta Origen");
        table.addCell("Cuenta Destino");
        table.addCell("Categoría");

        for (Transaccion transaccion : getListaTransacciones()) {
            table.addCell(transaccion.getFecha().toString());
            table.addCell(transaccion.getTipoTransaccion().toString()); // Convertir enum a texto
            table.addCell(String.valueOf(transaccion.getMonto()));
            table.addCell(transaccion.getDescripcion());
            table.addCell(transaccion.getCuentaOrigen().toString());
            table.addCell(transaccion.getCuentaDestino().toString());
            table.addCell(transaccion.getCategoria().toString()); // Convertir enum a texto
        }

        document.add(table);
        document.close();
    }

     */


}