package co.urwallet.controller;

import co.urwallet.UrWalletApp;
import co.urwallet.controller.service.IModelFactoryControllerService;
import co.urwallet.exceptions.LoginException;
import co.urwallet.exceptions.UsuarioException;
import co.urwallet.mapping.dto.LoginDto;
import co.urwallet.mapping.dto.UsuarioDto;
import co.urwallet.mapping.mappers.UrWalletMapper;
import co.urwallet.model.UrWallet;
import co.urwallet.model.Usuario;
import co.urwallet.utils.UrWalletUtils;
import co.urwallet.viewController.HomeViewsUsers;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ModelFactoryController implements IModelFactoryControllerService {
    UrWallet urWallet;
    UrWalletMapper mapper = UrWalletMapper.INSTANCE;
    private static class SingletonHolder {
        private final static ModelFactoryController eINSTANCE = new ModelFactoryController();
    }
    public static ModelFactoryController getInstance() {
        return SingletonHolder.eINSTANCE;
    }
    public ModelFactoryController() {
        cargarDatosBase();

    }
    private void cargarDatosBase() {
        urWallet = UrWalletUtils.inicializarDatos();
    }
    public UrWallet getUrWallet(){
        return urWallet;
    }
    @Override
    public List<UsuarioDto> obtenerUser() {
        return  mapper.getUsuariosDto(urWallet.getListaUsers());
    }

    @Override
    public boolean agregarUsers(UsuarioDto usuarioDto) {
        try{
            System.out.println("MODELLLL"+ usuarioDto.correo());
            if(!urWallet.verificarUsuarioExistente(usuarioDto.idUsuario())){
                Usuario user = mapper.usuarioDtoToUsuario(usuarioDto);
                System.out.println("MODELLLL22"+ user.getCorreo());

                getUrWallet().agregarUsuario(user);
            }
            return true;
        }catch (UsuarioException e){
            e.printStackTrace();
            e.getMessage();
            return false;
        }
    }
    @Override
    public Usuario iniciarSesion(LoginDto loginDto) throws LoginException {
        Usuario users = getUrWallet().buscarUserLogin(loginDto.email());
        if (users != null && users.getContrasena().equals(loginDto.contrasena())) {
            return users;
        } else {
            throw new LoginException("Email o contraseña incorrecta");
        }
    }

    @Override
    public boolean actualizarUser(String idUser,UsuarioDto usuarioDto){
        try{
            System.out.println("actualizar"+idUser);
            Usuario user = mapper.usuarioDtoToUsuario(usuarioDto);
            System.out.println("MQAPERRRR ID"+ user.getIdUsuario());
            getUrWallet().actualizarUsuario(idUser,user);
            return true;
        }catch (UsuarioException e){
            e.getMessage();
            return false;
        }
    }
    @Override
    public boolean eliminarUsuario(String idUser){
        boolean flagExiste = false;
        try {
            flagExiste = getUrWallet().eliminarUsuario(idUser);
        } catch (UsuarioException e) {
            throw new RuntimeException(e);
        }
        return flagExiste;
    }
    public FXMLLoader navegarVentana(String nombreArchivoFxml, String tituloVentana, Usuario usuarioLogueado) {
        FXMLLoader loader = null;
        try {
            URL fxmlLocation = UrWalletApp.class.getResource(nombreArchivoFxml);

            if (fxmlLocation == null) {
                throw new IOException("No se pudo localizar el archivo FXML: " + nombreArchivoFxml);
            }

            loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            // Obtener el controlador de la siguiente ventana
            Object controller = loader.getController();

            // Si el controlador es de la pantalla HomeUser, le pasamos el usuario logueado
            if (controller instanceof HomeViewsUsers) {
                ((HomeViewsUsers) controller).setUsuarioLogueado(usuarioLogueado);
            }

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle(tituloVentana);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarMensaje("Error al cargar la ventana", "No se pudo cargar " + nombreArchivoFxml + ": " + e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarMensaje("Error inesperado", "Ocurrió un error al cargar la ventana: " + e.getMessage(), Alert.AlertType.ERROR);
        }
        return loader;
    }
    @Override
    public void mostrarMensaje(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    @Override
    public void cerrarVentana(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

}
