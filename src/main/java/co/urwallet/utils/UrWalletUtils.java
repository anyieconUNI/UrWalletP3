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

        Usuario usuario2 = new Usuario();
        usuario2.setNombreCompleto("Alex Torres");
        usuario2.setIdUsuario("333333");
        usuario2.setCedula("888");
        usuario2.setCorreo("alex@gmail.com");
        usuario2.setContrasena("222");
        usuario2.setTelefono("66666");
        usuario2.setDireccion("MZN 2");
        usuario2.setSaldoDispo(3000.0f);

        Usuario usuario3 = new Usuario();
        usuario3.setNombreCompleto("Laura García");
        usuario3.setIdUsuario("444444");
        usuario3.setCedula("777");
        usuario3.setCorreo("laura@gmail.com");
        usuario3.setContrasena("333");
        usuario3.setTelefono("77777");
        usuario3.setDireccion("MZN 3");
        usuario3.setSaldoDispo(10000.0f);


        // Crear cuentas
        Cuenta cuenta1 = new Cuenta("1", "1001", "Cuenta Ahorros", TipoCuenta.AHORROS, 5000.0f);
        Cuenta cuenta2 = new Cuenta("2", "2001", "Cuenta Corriente", TipoCuenta.CORRIENTE, 3000.0f);
        Cuenta cuenta3 = new Cuenta("3", "3001", "Cuenta Inversión", TipoCuenta.INVERSION, 10000.0f);

        usuario1.agregarCuenta(cuenta1);
        usuario2.agregarCuenta(cuenta2);
        usuario3.agregarCuenta(cuenta3);

        // Agregar usuarios y cuentas a UrWallet
        urWallet.getListaUsers().add(usuario1);
        urWallet.getListaUsers().add(usuario2);
        urWallet.getListaUsers().add(usuario3);


        urWallet.getListaCuentas().add(cuenta1);
        urWallet.getListaCuentas().add(cuenta2);
        urWallet.getListaCuentas().add(cuenta3);

        // Crear transacciones
        ArrayList<Transaccion> transacciones = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Transaccion transaccion = new Transaccion();
            transaccion.setIdTransaccion(UUID.randomUUID().toString());
            transaccion.setFecha(new Date());
            transaccion.setTipoTransaccion(i % 2 == 0 ? TipoTransaccion.Retiro : TipoTransaccion.Deposito);
            transaccion.setMonto(i * 10); // Montos incrementales
            transaccion.setDescripcion("Transacción " + i);
            transaccion.setCuentaOrigen(cuenta1);
            transaccion.setCuentaDestino(cuenta2);
            transaccion.setCategoria(i % 2 == 0 ? Categoria.Transporte : Categoria.Entretenimiento);

            transacciones.add(transaccion);
        }
        urWallet.getListaTransaccion().addAll(transacciones);

        // El return debe ir al final
        return urWallet;
    }
}
