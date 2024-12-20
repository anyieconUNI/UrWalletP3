package co.urwallet.utils;

import co.urwallet.mapping.dto.UsuarioDto;
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
    public static ArrayList<UsuarioDto> cargarUsersConCuentas() {
        ArrayList<UsuarioDto> usuariosConCuentas = new ArrayList<>();
        try {
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

                String idUsuario = partes[0];
                String cedula = partes[1];
                String nombreCompleto = partes[2];
                String telefono = partes[3];
                String correo = partes[4];
                String contrasena = partes[5];
                String direccion = partes[6];
                Float saldoDispo = Float.valueOf(partes[7]);

                if (partes.length > 8 && !partes[8].isEmpty()) {
                    String[] cuentasArray = partes[8].split("@@");
                    for (String cuentaStr : cuentasArray) {
                        String[] detalles = cuentaStr.split(":");
                        if (detalles.length == 5) {
                            UsuarioDto usuarioConCuenta = new UsuarioDto(
                                    idUsuario,
                                    cedula,
                                    nombreCompleto,
                                    telefono,
                                    correo,
                                    contrasena,
                                    direccion,
                                    Float.valueOf(detalles[4]),
                                    null
                            );
                            usuariosConCuentas.add(usuarioConCuenta);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error al cargar los usuarios:");
            e.printStackTrace();
        }

        return usuariosConCuentas;
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
    private static final Object FILE_LOCK = new Object();
    public static void guardarCuentas(ArrayList<Cuenta> listaCuentas) throws IOException {
        synchronized (FILE_LOCK) {
            StringBuilder contenido = new StringBuilder();
            for (Cuenta cuenta : listaCuentas) {
                contenido.append(cuenta.getIdCuenta()).append("@@")
                        .append(cuenta.getNombreCuenta()).append("@@")
                        .append(cuenta.getNumeCuenta()).append("@@")
                        .append(cuenta.getTipoCuenta()).append("@@")
                        .append(cuenta.getSaldo()).append("@@")
                        .append(cuenta.getClienteId() == null ? "" : cuenta.getClienteId()).append("\n");
            }

            ArchivoUtils.guardarArchivo(RUTA_ARCHIVO_CUENTAS, contenido.toString(), false);
            System.out.println("Cuentas guardadas con bloqueo para asegurar persistencia inmediata.");
        }
    }


    public static ArrayList<Cuenta> cargarCuentas() throws IOException {
        ArrayList<Cuenta> cuentas = new ArrayList<>();
        ArrayList<String> contenido = ArchivoUtils.leerArchivo(RUTA_ARCHIVO_CUENTAS);

        for (String linea : contenido) {
            if (linea.trim().isEmpty()) {
                continue; // Ignorar líneas vacías
            }

            String[] partes = linea.split("@@");
            if (partes.length < 5) {
                System.err.println("Línea mal formateada: " + linea);
                continue; // Saltar líneas mal formateadas
            }

            Cuenta cuenta = new Cuenta();
            cuenta.setIdCuenta(partes[0]);
            cuenta.setNombreCuenta(partes[1]);
            cuenta.setNumeCuenta(partes[2]);
            cuenta.setTipoCuenta(TipoCuenta.valueOf(partes[3]));
            cuenta.setSaldo(Float.valueOf(partes[4]));

            // Manejar el clienteId (puede ser nulo o vacío)
            if (partes.length > 5 && !partes[5].trim().isEmpty()) {
                cuenta.setClienteId(partes[5]); // Asignar clienteId si está presente
            } else {
                cuenta.setClienteId(""); // Dejarlo como null si no está presente
            }

            cuentas.add(cuenta);
        }

        return cuentas;
    }



    public static void guardarTransacciones(ArrayList<Transaccion> listaTransaccion) throws IOException {
        // TODO Auto-generated method stub
        synchronized (FILE_LOCK) {
            String contenido = "";
            for (Transaccion transacciones : listaTransaccion) {
                contenido += transacciones.getIdTransaccion() + "@@" + transacciones.getDescripcion() + "@@" + transacciones.getTipoTransaccion()
                        + "@@" + transacciones.getCategoria() + "@@" + transacciones.getCuentaOrigen() + "@@" + transacciones.getCuentaDestino()
                        + "@@" + transacciones.getMonto() + "@@" + transacciones.getFecha() + "\n";
            }
            ArchivoUtils.guardarArchivo(RUTA_ARCHIVO_TRANSSACCION, contenido, false);
        }
    }

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
            transaccion.setFecha(partes[7]);

            String tipoTransStr = partes[2];
            TipoTransaccion tipoTransaccion = TipoTransaccion.valueOf(tipoTransStr);
            transaccion.setTipoTransaccion(tipoTransaccion);
            String categoriaStr = partes[3];
            Categoria cate = Categoria.valueOf(categoriaStr);
            transaccion.setCategoria(cate);

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
                float monto = Float.parseFloat(partes[6]);
                transaccion.setMonto(monto);
            } catch (NumberFormatException e) {
                System.out.println("Formato de monto no válido: " + partes[6]);
            }

            transacciones.add(transaccion);
        }

        return transacciones;
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
