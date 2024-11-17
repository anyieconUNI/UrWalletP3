package co.urwallet.viewController;

import co.urwallet.controller.SaldoClienteController;
import co.urwallet.mapping.dto.CuentaDto;
import co.urwallet.mapping.dto.PresupuestoDto;
import co.urwallet.model.Categoria;
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
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;


public class SaldoClienteViewsControllers implements Initializable {


    SaldoClienteController saldoClienteController;
    PresupuestoDto presupuestoSeleccionado;

    private Mes mesSeleccionado;
    private ObservableList<Transaccion> listaTransacciones = FXCollections.observableArrayList();
    ObservableList<PresupuestoDto> listaPresupuestoDto = FXCollections.observableArrayList();

    @FXML
    private AnchorPane panelSaldo;

    @FXML
    private AnchorPane panelPresupuesto;

    @FXML
    private Button btnSaldo;

    @FXML
    private Button btnPresupuesto;


    @FXML
    private Button btnActualizar;

    @FXML
    private Button btnAgregar;


    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnLimpiar;

    @FXML
    private ComboBox cmbMes;

    @FXML
    private Button btnReporte;

    @FXML
    public TableView<PresupuestoDto> tablePresupuestos;

    @FXML
    public TableColumn<PresupuestoDto, String> tcNombre;

    @FXML
    public TableColumn<PresupuestoDto, String> tcMontoGasto;

    @FXML
    public TableColumn<PresupuestoDto, String> tcMontoTotal;

    @FXML
    public TableColumn<PresupuestoDto, String> tcCategoria;

    @FXML
    public TextField txtNombre;
    @FXML
    public TextField txtMontoGasto;
    @FXML
    public TextField txtMontoTotal;
    @FXML
    public ComboBox cmbCategoria;


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
        saldoClienteController = new SaldoClienteController();
        intDataBinding();
        obtenerPresupuesto();
        tablePresupuestos.getItems().clear();
        tablePresupuestos.setItems(listaPresupuestoDto);
        listenerPresupuesto();
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

    private void obtenerPresupuesto(){listaPresupuestoDto.addAll(saldoClienteController.obtenerPresupuesto());}

    private void listenerPresupuesto() {
        tablePresupuestos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            presupuestoSeleccionado = newSelection;
            mostrarInformacionPresupuesto(presupuestoSeleccionado);
        });
    }

    private void intDataBinding(){
        tcNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().nombre()));
        tcMontoGasto.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().montoGasto()).asObject().asString());
        tcMontoTotal.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().montoTotal()).asObject().asString());
        tcCategoria.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().categoria().toString()));
    }

    private void mostrarInformacionPresupuesto(PresupuestoDto presupuestoSeleccionado) {
        if(presupuestoSeleccionado != null){
            txtNombre.setText(presupuestoSeleccionado.nombre());
            txtMontoGasto.setText(presupuestoSeleccionado.montoGasto().toString());
            txtMontoTotal.setText(presupuestoSeleccionado.montoTotal().toString());
            cmbCategoria.setValue(presupuestoSeleccionado.categoria());
        }
    }

    public void limpiarPresupuestoAction(ActionEvent actionEvent) {limpiarPresupuesto();}

    public void limpiarPresupuesto(){
        txtNombre.setText("");
        txtMontoGasto.setText("");
        txtMontoTotal.setText("");
        cmbCategoria.getSelectionModel().clearSelection();
    }

    public void agregarPresupuestoAction(ActionEvent actionEvent) {
        PresupuestoDto presupuestoDto = construirRegistroPresupuesto();
        if(datosValidos(presupuestoDto)){
            if(saldoClienteController.agregarPresupuesto(presupuestoDto)){
                saldoClienteController.mostrarMensaje("El presumpuesto se creo correctamente", "Notificación usuario", Alert.AlertType.INFORMATION);
                listaPresupuestoDto.add(presupuestoDto);

                tablePresupuestos.refresh();
                limpiarPresupuesto();
                saldoClienteController.obtenerPresupuesto();
            }
            else{
                saldoClienteController.mostrarMensaje("Los datos ingresados son invalidos", "Presupuesto no creado", Alert.AlertType.ERROR);
            }
        }
    }

    public void actualizarPresupuestoAction(ActionEvent actionEvent) {
        if (presupuestoSeleccionado != null) {

            PresupuestoDto presupuestoActualizado = construirRegistroPresupuesto();
            presupuestoActualizado = new PresupuestoDto(
                    presupuestoActualizado.idPresupuesto(),
                    presupuestoActualizado.nombre(),
                    presupuestoActualizado.montoTotal(),
                    presupuestoActualizado.montoGasto(),
                    presupuestoActualizado.categoria()
            );

            boolean resultado = saldoClienteController.actualizarPresupuesto(presupuestoSeleccionado.idPresupuesto(),presupuestoActualizado);

            if (resultado) {
                saldoClienteController.mostrarMensaje("El presupuesto ha sido actualizado con éxito", "Notificación Usuario", Alert.AlertType.INFORMATION);
                listaPresupuestoDto.remove(presupuestoSeleccionado);
                listaPresupuestoDto.add(presupuestoActualizado);
                tablePresupuestos.refresh();
                limpiarPresupuesto();
                } else {
                saldoClienteController.mostrarMensaje("No se pudo actualizar el Presupuesto", "Error", Alert.AlertType.ERROR);
            }
        } else {
            saldoClienteController.mostrarMensaje("Debe seleccionar un presupuesto para actualizar", "Error", Alert.AlertType.WARNING);
        }
    }

    public void eliminarPresupuestoAction(ActionEvent actionEvent) {
        try {
            String idPresupuesto = presupuestoSeleccionado.idPresupuesto();

            // Llamada al método eliminar del controlador
            boolean PresupuestoEliminado = saldoClienteController.eliminarPresupuesto(idPresupuesto);

            if (PresupuestoEliminado) {
                saldoClienteController.mostrarMensaje("Notificación Cliente", "Presupuesto eliminada correctamente.", Alert.AlertType.INFORMATION);
                listaPresupuestoDto.remove(presupuestoSeleccionado);
                tablePresupuestos.getSelectionModel().clearSelection();
                tablePresupuestos.refresh();
                limpiarPresupuesto();
            } else {
                saldoClienteController.mostrarMensaje("Notificación Cliente", "No se pudo eliminar el presupuesto.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            saldoClienteController.mostrarMensaje("Error", "Ocurrió un error al eliminar el presupuesto.", Alert.AlertType.ERROR);
        }
    }

    private PresupuestoDto construirRegistroPresupuesto() {
        float montoTotal = Float.parseFloat(txtMontoTotal.getText());
        float montoGasto = Float.parseFloat(txtMontoGasto.getText());
        String cateSelec = cmbCategoria.getValue().toString();
        Categoria catego = Categoria.valueOf(cateSelec);

        String id = UUID.randomUUID().toString();
        return new PresupuestoDto(
                id,
                txtNombre.getText(),
                montoTotal,
                montoGasto,
                catego
        );
    }

    private boolean datosValidos(PresupuestoDto presupuestoDto){
        String mensaje = "";
        if(presupuestoDto.nombre() == null || presupuestoDto.nombre().equals(""))
            mensaje += "El nombre es invalido \n" ;
        if(presupuestoDto.montoTotal() == null || presupuestoDto.montoTotal().equals(""))
            mensaje += "El monto total es invalido \n" ;
        if(presupuestoDto.categoria() == null || presupuestoDto.categoria().equals(""))
            mensaje += "La categoría es invalida \n" ;
        if(mensaje.equals("")){
            return true;
        }else{
            saldoClienteController.mostrarMensaje("Notificación", mensaje, Alert.AlertType.WARNING);
            return false;
        }
    }



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
