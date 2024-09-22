package co.urwallet.utils;

import co.urwallet.model.UrWallet;
import co.urwallet.model.Usuario;

public class UrWalletUtils {
    public static UrWallet inicializarDatos(){

        UrWallet urWallet = new UrWallet();

        Usuario usuario = new Usuario();
        usuario.setNombreCompleto("Choki Ramírez");
        usuario.setIdUsuario("222222");
        usuario.setCedula("999");
        usuario.setCorreo("choki@gmail.com");
        usuario.setContrasena("111");
        usuario.setTelefono("55555");
        usuario.setDireccion("MZN");
        usuario.setSaldoDispo(0.0f);
        return urWallet;
    }
}
