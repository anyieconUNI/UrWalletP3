package co.urwallet.utils;

import co.urwallet.model.Cuenta;
import co.urwallet.model.TipoCuenta;
import co.urwallet.model.UrWallet;
import co.urwallet.model.Usuario;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Persistencia {
    private static final String RUTA_ARCHIVO_USUARIOS = "src/main/resources/co/urwallet/Persistencia/archivos/Usuarios.txt";
    private static final String RUTA_ARCHIVO_LOG = "src/main/resources/co/urwallet/Persistencia/Log/UrWalletLog.txt";
    private static final String RUTA_ARCHIVO_MODELO_urWallet_BINARIO = "src/main/resources/co/urwallet/Persistencia/model.dat";
    private static final String RUTA_ARCHIVO_MODELO_urWallet_XML = "src/main/resources/co/urwallet/Persistencia/model.xml";
    private static final String RUTA_RESPALDO = "src/main/resources/co/urwallet/Persistencia/respaldo/";
    private static final String RUTA_ARCHIVO_CUENTAS = "src/main/resources/co/urwallet/Persistencia/archivos/Cuentas.txt";

    public static void cargarDatosArchivos(UrWallet urWallet) throws FileNotFoundException, IOException {
        //cargar archivo de users
        ArrayList<Usuario> usersCargados = cargarUsers();
        if (usersCargados.size() > 0)
            urWallet.getListaUsers().addAll(usersCargados);

    }

    public static void guardarUsuario(ArrayList<Usuario> listaUsers) throws IOException {
        // TODO Auto-generated method stub
        String contenido = "";
        for (Usuario usuarios : listaUsers) {
            contenido += usuarios.getIdUsuario() + "@@" + usuarios.getCedula() + "@@" + usuarios.getNombreCompleto() + "@@" + usuarios.getTelefono()
                    + "@@" + usuarios.getCorreo() + "@@" + usuarios.getContrasena() + "@@" + usuarios.getDireccion() + "@@" + usuarios.getSaldoDispo() + "\n";
        }
        ArchivoUtils.guardarArchivo(RUTA_ARCHIVO_USUARIOS, contenido, false);
    }

    public static ArrayList<Usuario> cargarUsers() throws FileNotFoundException, IOException {
        ArrayList<Usuario> users = new ArrayList<Usuario>();
        ArrayList<String> contenido = ArchivoUtils.leerArchivo(RUTA_ARCHIVO_USUARIOS);
        String linea = "";
        for (int i = 0; i < contenido.size(); i++) {
            linea = contenido.get(i);
            Usuario Usuario = new Usuario();
            Usuario.setIdUsuario(linea.split("@@")[0]);
            Usuario.setCedula(linea.split("@@")[1]);
            Usuario.setNombreCompleto(linea.split("@@")[2]);
            Usuario.setTelefono(linea.split("@@")[3]);
            Usuario.setCorreo(linea.split("@@")[4]);
            Usuario.setContrasena(linea.split("@@")[5]);
            Usuario.setDireccion(linea.split("@@")[6]);
            Usuario.setSaldoDispo(Float.valueOf(linea.split("@@")[7]));
            users.add(Usuario);
        }
        return users;
    }


    public static void copiarArchivos() throws IOException {
        String archivoOrigen = RUTA_ARCHIVO_USUARIOS;
        ArchivoUtils.copiarArchivos(archivoOrigen, RUTA_RESPALDO, "Usuarios_", ".txt");

        String archivoOrigenXML = RUTA_ARCHIVO_MODELO_urWallet_XML;
        ArchivoUtils.copiarArchivos(archivoOrigenXML, RUTA_RESPALDO, "Usuarios_", ".xml");

    }

    // Persistencia cuentas

    public static void cargarDatosArchivosCuentas(UrWallet urWallet) throws FileNotFoundException, IOException {
        //cargar archivo de cuentas
        ArrayList<Cuenta> cuentasCargados = cargarCuentas();
        if (cuentasCargados.size() > 0)
            urWallet.getListaCuentas().addAll(cuentasCargados);

    }

    public static void guardarCuentas(ArrayList<Cuenta> listaCuentas) throws IOException {
        // TODO Auto-generated method stub
        String contenido = "";
        for (Cuenta cuentas : listaCuentas) {
            contenido += cuentas.getIdCuenta() + "@@" + cuentas.getNombreCuenta() + "@@" + cuentas.getNumeCuenta() + "@@" + cuentas.getTipoCuenta()
                    + "@@" + cuentas.getSaldo() + "\n";
        }
        ArchivoUtils.guardarArchivo(RUTA_ARCHIVO_CUENTAS, contenido, false);
    }


    public static ArrayList<Cuenta> cargarCuentas() throws FileNotFoundException, IOException {
        ArrayList<Cuenta> cuentas = new ArrayList<Cuenta>();
        ArrayList<String> contenido = ArchivoUtils.leerArchivo(RUTA_ARCHIVO_CUENTAS);
        String linea = "";
        for (int i = 0; i < contenido.size(); i++) {
            linea = contenido.get(i);
            Cuenta cuenta = new Cuenta();
            cuenta.setIdCuenta(linea.split("@@")[0]);
            cuenta.setNombreCuenta(linea.split("@@")[1]);
            cuenta.setNumeCuenta(linea.split("@@")[2]);
            String tipoCuentaStr = linea.split("@@")[3];
            TipoCuenta tipoCuenta;
            try {
                tipoCuenta = TipoCuenta.valueOf(tipoCuentaStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                tipoCuenta = TipoCuenta.AHORROS;
                System.out.println("Tipo de cuenta no válido: " + tipoCuentaStr + ". Usando valor por defecto.");
            }
            cuenta.setTipoCuenta(tipoCuenta);
            Float saldo = Float.parseFloat(linea.split("@@")[4]);
            cuenta.setSaldo(saldo);

            cuentas.add(cuenta);
        }
        return cuentas;
    }



    // LOG, BINARIO Y XML

    public static void guardaRegistroLog(String mensajeLog, int nivel, String accion) {
        ArchivoUtils.guardarRegistroLog(mensajeLog, nivel, accion, RUTA_ARCHIVO_LOG);
    }

    public static void guardarRecursourWalletBinario(UrWallet urWallet) {
        try {
            ArchivoUtils.salvarRecursoSerializado(RUTA_ARCHIVO_MODELO_urWallet_BINARIO, urWallet);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static UrWallet cargarRecursoUrWalletBinario() {

        UrWallet urWallet = null;

        try {
            urWallet = (UrWallet) ArchivoUtils.cargarRecursoSerializado(RUTA_ARCHIVO_MODELO_urWallet_BINARIO);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return urWallet;
    }

    public static void guardarRecursoBancoXML(UrWallet urWallet) {
        try {
            ArchivoUtils.salvarRecursoSerializadoXML(RUTA_ARCHIVO_MODELO_urWallet_XML, urWallet);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static UrWallet cargarRecursoUrWalletXML() {

        UrWallet urWallet = null;

        try {
            urWallet = (UrWallet) ArchivoUtils.cargarRecursoSerializadoXML(RUTA_ARCHIVO_MODELO_urWallet_XML);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return urWallet;

    }



}
