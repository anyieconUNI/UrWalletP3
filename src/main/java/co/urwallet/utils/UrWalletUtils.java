package co.urwallet.utils;

import co.urwallet.model.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

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
        Usuario usuario1 = new Usuario();
        usuario1.setNombreCompleto("Choki Ramírez");
        usuario1.setIdUsuario("222222");
        usuario1.setCedula("999");
        usuario1.setCorreo("choki@gmail.com");
        usuario1.setContrasena("111");
        usuario1.setTelefono("55555");
        usuario1.setDireccion("MZN");
        usuario1.setSaldoDispo(5000.0f);



        // El return debe ir al final
        return urWallet;
    }
}
