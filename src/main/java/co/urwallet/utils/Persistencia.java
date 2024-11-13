package co.urwallet.utils;

import co.urwallet.model.*;

import java.util.Locale;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

public class Persistencia {
    private static final String RUTA_ARCHIVO_USUARIOS = "src/main/resources/co/urwallet/Persistencia/archivos/Usuarios.txt";
    private static final String RUTA_ARCHIVO_LOG = "src/main/resources/co/urwallet/Persistencia/Log/UrWalletLog.txt";
    private static final String RUTA_ARCHIVO_MODELO_urWallet_BINARIO = "src/main/resources/co/urwallet/Persistencia/model.dat";
    private static final String RUTA_ARCHIVO_MODELO_urWallet_XML = "src/main/resources/co/urwallet/Persistencia/model.xml";
    private static final String RUTA_RESPALDO = "src/main/resources/co/urwallet/Persistencia/respaldo/";
    private static final String RUTA_ARCHIVO_CUENTAS = "src/main/resources/co/urwallet/Persistencia/archivos/Cuentas.txt";
    private static final String RUTA_ARCHIVO_TRANSSACCION = "src/main/resources/co/urwallet/Persistencia/archivos/Transsaciones.txt";
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
            String cuentasBancarias = usuarios.getCuentasBancarias().stream()
                    .map(cuenta -> cuenta.getIdCuenta() + ":" + cuenta.getNombreCuenta() + ":" + cuenta.getNumeCuenta() + ":" + cuenta.getTipoCuenta() + ":" + cuenta.getSaldo())
                    .collect(Collectors.joining("@@"));
            contenido += usuarios.getIdUsuario() + "@@" + usuarios.getCedula() + "@@" + usuarios.getNombreCompleto() + "@@" + usuarios.getTelefono()
                    + "@@" + usuarios.getCorreo() + "@@" + usuarios.getContrasena() + "@@" + usuarios.getDireccion() + "@@" + usuarios.getSaldoDispo() + "@@" + cuentasBancarias + "\n";
        }
        ArchivoUtils.guardarArchivo(RUTA_ARCHIVO_USUARIOS, contenido, false);
    }


    public static ArrayList<Usuario> cargarUsers() throws FileNotFoundException, IOException {
//        ArrayList<Usuario> users = new ArrayList<Usuario>();
//        ArrayList<String> contenido = ArchivoUtils.leerArchivo(RUTA_ARCHIVO_USUARIOS);
//        String linea = "";
//        for (int i = 0; i < contenido.size(); i++) {
//            linea = contenido.get(i);
//            Usuario Usuario = new Usuario();
//            Usuario.setIdUsuario(linea.split("@@")[0]);
//            Usuario.setCedula(linea.split("@@")[1]);
//            Usuario.setNombreCompleto(linea.split("@@")[2]);
//            Usuario.setTelefono(linea.split("@@")[3]);
//            Usuario.setCorreo(linea.split("@@")[4]);
//            Usuario.setContrasena(linea.split("@@")[5]);
//            Usuario.setDireccion(linea.split("@@")[6]);
//            Usuario.setSaldoDispo(Float.valueOf(linea.split("@@")[7]));
//
//            users.add(Usuario);
//        }
//        return users;
        ArrayList<Usuario> users = new ArrayList<>();
        ArrayList<String> contenido = ArchivoUtils.leerArchivo(RUTA_ARCHIVO_USUARIOS);

        for (String linea : contenido) {
            if (linea.trim().isEmpty()) {
                continue;
            }

            String[] partes = linea.split("@@");
            if (partes.length < 8) {
                System.err.println("Línea mal formateada: " + linea);
                continue;
            }

            Usuario usuario = new Usuario();
            usuario.setIdUsuario(partes[0]);
            usuario.setCedula(partes[1]);
            usuario.setNombreCompleto(partes[2]);
            usuario.setTelefono(partes[3]);
            usuario.setCorreo(partes[4]);
            usuario.setContrasena(partes[5]);
            usuario.setDireccion(partes[6]);
            usuario.setSaldoDispo(Float.valueOf(partes[7]));

            if (partes.length > 8 && !partes[8].isEmpty()) {
                String[] cuentasArray = partes[8].split("@@");
                for (String cuentaStr : cuentasArray) {
                    String[] detalles = cuentaStr.split(":");
                    if (detalles.length == 5) {
                        Cuenta cuenta = new Cuenta();
                        cuenta.setIdCuenta(detalles[0]);
                        cuenta.setNombreCuenta(detalles[1]);
                        cuenta.setNumeCuenta(detalles[2]);
                        cuenta.setTipoCuenta(TipoCuenta.valueOf(detalles[3]));
                        cuenta.setSaldo(Float.valueOf(detalles[4]));
                        usuario.agregarCuenta(cuenta);
                    }
                }
            }

            users.add(usuario);
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

    public static void guardarTransacciones(ArrayList<Transaccion> listaTransaccion) throws IOException {
        StringBuilder contenido = new StringBuilder();
        for (Transaccion transaccion : listaTransaccion) {
            contenido.append(transaccion.getIdTransaccion()).append("@@")
                    .append(transaccion.getDescripcion()).append("@@")
                    .append(transaccion.getTipoTransaccion()).append("@@")
                    .append(transaccion.getCategoria()).append("@@");

            Cuenta cuentaOrigen = transaccion.getCuentaOrigen();
            contenido.append("numeCuentaOrigen=").append(cuentaOrigen.getNumeCuenta()).append("@@");

            Cuenta cuentaDestino = transaccion.getCuentaDestino();
            contenido.append("numeCuentaDestino=").append(cuentaDestino.getNumeCuenta()).append("@@");

            contenido.append(transaccion.getFecha()).append("@@")
                    .append(transaccion.getMonto()).append("\n");
        }
        ArchivoUtils.guardarArchivo(RUTA_ARCHIVO_TRANSSACCION, contenido.toString(), false);
    }

    /*
    public static void guardarTransacciones(ArrayList<Transaccion> listaTransaccion) throws IOException {
        // TODO Auto-generated method stub
        String contenido = "";
        for (Transaccion transacciones : listaTransaccion) {
            contenido += transacciones.getIdTransaccion() + "@@" + transacciones.getDescripcion() + "@@" + transacciones.getTipoTransaccion()
                    + "@@" + transacciones.getCategoria() + "@@" + transacciones.getCuentaOrigen() + "@@" + transacciones.getCuentaDestino()
                    + "@@" + transacciones.getFecha() + "@@" + transacciones.getMonto() + "\n" ;
        }
        ArchivoUtils.guardarArchivo(RUTA_ARCHIVO_TRANSSACCION, contenido, false);
    }

     */

    public static void cargarDatosArchivosTransacciones(UrWallet urWallet) throws FileNotFoundException, IOException {
        //cargar archivo de cuentas
        ArrayList<Transaccion> transaccionesCargados = cargarTransacciones();
        if (transaccionesCargados.size() > 0)
            urWallet.getListaTransaccion().addAll(transaccionesCargados);

    }

    public static ArrayList<Transaccion> cargarTransacciones() throws FileNotFoundException, IOException {
        ArrayList<Transaccion> transacciones = new ArrayList<>();
        ArrayList<String> contenido = ArchivoUtils.leerArchivo(RUTA_ARCHIVO_TRANSSACCION);
        String linea;

        for (String item : contenido) {
            linea = item;
            Transaccion transaccion = new Transaccion();

            // Separar por los delimitadores "@@"
            String[] partes = linea.split("@@");

            transaccion.setIdTransaccion(partes[0]);
            transaccion.setDescripcion(partes[1]);

            // Establecer tipo de transacción
            String tipoTransStr = partes[2];
            TipoTransaccion tipoTransaccion;
            try {
                tipoTransaccion = TipoTransaccion.valueOf(tipoTransStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                tipoTransaccion = TipoTransaccion.Deposito;  // Valor por defecto
                System.out.println("Tipo de transacción no válido: " + tipoTransStr + ". Usando valor por defecto.");
            }
            transaccion.setTipoTransaccion(tipoTransaccion);

            // Establecer categoría
            String categoriaStr = partes[3];
            Categoria categoria;
            try {
                categoria = Categoria.valueOf(categoriaStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                categoria = Categoria.Entretenimiento;  // Valor por defecto
                System.out.println("Categoría no válida: " + categoriaStr + ". Usando valor por defecto.");
            }
            transaccion.setCategoria(categoria);

            // Establecer cuenta origen
            Cuenta cuentaOrigen = new Cuenta();
            String[] cuentaOrigenParts = partes[4].replace("Cuenta(idCuenta=", "").replace(")", "").split(", ");
            cuentaOrigen.setNumeCuenta(cuentaOrigenParts[1].split("=")[1]);
            transaccion.setCuentaOrigen(cuentaOrigen);

            // Establecer cuenta destino
            Cuenta cuentaDestino = new Cuenta();
            String[] cuentaDestinoParts = partes[5].replace("Cuenta(idCuenta=", "").replace(")", "").split(", ");
            cuentaDestino.setNumeCuenta(cuentaDestinoParts[1].split("=")[1]);
            transaccion.setCuentaDestino(cuentaDestino);

            try {
                Date fecha = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH).parse(partes[6]);
                transaccion.setFecha(fecha);
            } catch (ParseException e) {
                System.out.println("Formato de fecha no válido: " + partes[6]);
            }

            try {
                float monto = Float.parseFloat(partes[7]);
                transaccion.setMonto(monto);
            } catch (NumberFormatException e) {
                System.out.println("Formato de monto no válido: " + partes[7]);
            }

            transacciones.add(transaccion);
        }

        return transacciones;
    }






/*
    public static ArrayList<Transaccion> cargarTransacciones() throws FileNotFoundException, IOException {
        ArrayList<Transaccion> transacciones = new ArrayList<>();
        ArrayList<String> contenido = ArchivoUtils.leerArchivo(RUTA_ARCHIVO_TRANSSACCION);

        for (String linea : contenido) {
            Transaccion transaccion = new Transaccion();

            String[] partes = linea.split("@@");

            if (partes.length < 8) {
                System.out.println("Error en el formato de la línea: " + linea);
                continue;
            }

            transaccion.setIdTransaccion(partes[0]);
            transaccion.setDescripcion(partes[1]);
            TipoTransaccion tipoTransaccion;
            try {
                tipoTransaccion = TipoTransaccion.valueOf(partes[2].toUpperCase());
            } catch (IllegalArgumentException e) {
                tipoTransaccion = TipoTransaccion.Deposito;
                System.out.println("Tipo de transacción no válido: " + partes[2] + ". Usando valor por defecto.");
            }
            transaccion.setTipoTransaccion(tipoTransaccion);

            Categoria categoria;
            try {
                categoria = Categoria.valueOf(partes[3].toUpperCase());
            } catch (IllegalArgumentException e) {
                categoria = Categoria.Entretenimiento;
                System.out.println("Categoría no válida: " + partes[3] + ". Usando valor por defecto.");
            }
            transaccion.setCategoria(categoria);

            Cuenta cuentaOrigen = new Cuenta();
            cuentaOrigen.setIdCuenta(partes[4]);
            transaccion.setCuentaOrigen(cuentaOrigen);

            Cuenta cuentaDestino = new Cuenta();
            cuentaDestino.setIdCuenta(partes[5]);
            transaccion.setCuentaDestino(cuentaDestino);

            String fechaStr = partes[6];
            try {
                Date fecha = new SimpleDateFormat("yyyy-MM-dd").parse(fechaStr);
                transaccion.setFecha(fecha);
            } catch (ParseException e) {
                System.out.println("Formato de fecha no válido: " + fechaStr);
            }

            String montoStr = partes[7];
            try {
                float monto = Float.parseFloat(montoStr);
                transaccion.setMonto(monto);
            } catch (NumberFormatException e) {
                System.out.println("Formato de monto no válido: " + montoStr);
            }

            transacciones.add(transaccion);
        }
        return transacciones;
    }

 */


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
