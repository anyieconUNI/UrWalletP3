package co.urwallet.model;

import co.urwallet.exceptions.UsuarioException;
import co.urwallet.model.Services.IUrWalletService;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class UrWallet implements IUrWalletService {
    ArrayList<Usuario> listaUsuarios = new ArrayList<>();
//    ArrayList<Admin> listaAdmins = new ArrayList<>();
//    ArrayList<Cuenta> listaCuentas = new ArrayList<>();

//
    public UrWallet() {
    }
    public ArrayList<Usuario> getListaUsers(){
        return listaUsuarios;
    }
    public void agregarUsuario(Usuario nuevoUsuario) throws UsuarioException {
        if (nuevoUsuario.getCorreo() == null) {
            throw new UsuarioException("El correo no puede ser nulo");
        }
        getListaUsers().add(nuevoUsuario);
    }

    @Override
    public boolean verificarUsuarioExistente(String idUser) throws UsuarioException {
        try {
            if (usuarioExiste(idUser)) {
                throw new UsuarioException("El usuario ya existe");
            } else {
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error inesperado en verificarUsuarioExistente: " + e.getMessage());
            throw new UsuarioException("Error inesperado al verificar la existencia del usuario");
        }
    }


    public boolean usuarioExiste(String id) {
        boolean usuarioEncontrado = false;
        for (Usuario usuario : getListaUsers()) {
            if (usuario.getIdUsuario().equalsIgnoreCase(id)) {
                usuarioEncontrado = true;
                break;
            }
        }
        return usuarioEncontrado;
    }
    public Usuario buscarUserLogin(String email) {
        for (Usuario user : getListaUsers()) {
            System.out.println("AQQQQQQQQQQQQQQQQQQ"+user.getCorreo() +email);
            // Verificar si el correo es nulo antes de hacer la comparación
            if (user.getCorreo() != null && user.getCorreo().equalsIgnoreCase(email)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public boolean eliminarUsuario(String id) throws UsuarioException {
        try {
            Usuario usuario = obtenerUsuario(id);

            if (usuario == null) {
                throw new UsuarioException("El usuario no existe");
            } else {
                getListaUsuarios().remove(usuario);
                return true;
            }
        } catch (UsuarioException e) {
            System.err.println("Error en eliminarUsuario: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            throw new UsuarioException("Error inesperado al eliminar el usuario");
        }
    }

    @Override
    public Usuario obtenerUsuario(String id) {
        Usuario usuarioEncontrado = null;
        for (Usuario usuario : getListaUsuarios()) {
            if (usuario.getIdUsuario().equalsIgnoreCase(id)) {
                usuarioEncontrado = usuario;
                break;
            }
        }
        return usuarioEncontrado;
    }

    @Override
    public boolean actualizarUsuario(String idActual, Usuario usuario) throws UsuarioException {
        try {
            System.out.print("AQUIIIIIIIIIIIIIII" + idActual);
            Usuario usuarioActual = obtenerUsuario(idActual);

            if (usuarioActual == null) {
                throw new UsuarioException("El usuario no existe");
            } else {
                usuarioActual.setNombreCompleto(usuario.getNombreCompleto());
                usuarioActual.setCedula(usuario.getCedula());
                usuarioActual.setTelefono(usuario.getTelefono());
                usuarioActual.setDireccion(usuario.getDireccion());
                return true;
            }
        } catch (UsuarioException e) {
            // Manejo específico de UsuarioException
            System.err.println("Error en actualizarUsuario: " + e.getMessage());
            throw e; // Vuelve a lanzar la excepción
        } catch (Exception e) {
            // Manejo de cualquier otra excepción no prevista
            System.err.println("Error inesperado: " + e.getMessage());
            throw new UsuarioException("Error inesperado al actualizar el usuario");
        }
    }

    /*
    @Override

    public boolean actualizarUsuario(String idActual, Usuario usuario) throws UsuarioException {
        System.out.print("AQUIIIIIIIIIIIIIII"+ idActual);
        Usuario usuarioActual = obtenerUsuario(idActual);
        System.out.print("AQUIIIIIIIIIIIIIII"+ usuarioActual.getIdUsuario());
        if (usuarioActual == null) {

            throw new UsuarioException("El usuario no existe");

        }
        else {
            usuarioActual.setNombreCompleto(usuario.getNombreCompleto());
            usuarioActual.setCedula(usuario.getCedula());
            usuarioActual.setTelefono(usuario.getTelefono());
            usuarioActual.setDireccion(usuario.getDireccion());
            return true;
        }
    }
    @Override
    public boolean eliminarUsuario(String id) throws UsuarioException {
        Usuario usuario = null;
        boolean flag = false;
        usuario = obtenerUsuario(id);
        if (usuario == null)
            throw new UsuarioException("El usuario no existe");
        else {
            getListaUsuarios().remove(usuario);
            flag = true;
        }
        return flag;
    }
        @Override
    public boolean verificarUsuarioExistente(String idUser) throws UsuarioException {
        if (usuarioExiste(idUser)) {
            throw new UsuarioException("El usuario ya existe");
        } else {
            return false;
        }
    }



     */


//    public Usuario validarUsuario(String numeroIdentificacion, String contraseña) throws Exception {
//        Usuario usuario = obtenerUsuario(numeroIdentificacion);
//        if (usuario != null) {
//            if (usuario.getContrasena().equals(contraseña)) {
//                return usuario;
//            }
//        }
//        throw new Exception("Los datos de acceso son incorrectos");
//    }
//
//    // Proceso de cuentas
//
//    // generar numero random de cuenta
//    private String generarNumCuenta() {
//        return generarNumCuentaRecursivo(new StringBuilder(), 10);
//    }
//
//    private String generarNumCuentaRecursivo(StringBuilder numeroCuenta, int longitudRestante) {
//        if (longitudRestante == 0) {
//            return numeroCuenta.toString();
//        }
//        int numero = new Random().nextInt(10);
//        numeroCuenta.append(numero);
//        return generarNumCuentaRecursivo(numeroCuenta, longitudRestante - 1);
//    }
//
//    // Crear numero de cuenta
//    private String crearNumCuenta() {
//
//        String numCuenta = generarNumCuenta();
//
//        while (obtenerCuenta(numCuenta) != null) {
//            numCuenta = generarNumCuenta();
//        }
//        return numCuenta;
//    }
//
//    // Obtener cuenta
//
//    public Cuenta obtenerCuenta(String numCuenta) {
//        return obtenerCuentaRecursivo(numCuenta, 0);
//    }
//
//    private Cuenta obtenerCuentaRecursivo(String numeroCuenta, int indice) {
//
//        if (indice >= listaCuentas.size()) {
//            return null;
//        }
//        if (listaCuentas.get(indice).getNumCuenta().equals(numeroCuenta)) {
//            return listaCuentas.get(indice);
//        }
//        return obtenerCuentaRecursivo(numeroCuenta, indice + 1);
//    }
//
//    //Agregamos cuanta al usuario
//    public String agregarCuenta(String id, float saldoInicial) throws Exception {
//        Usuario propietario = obtenerUsuario(id);
//
//        if (propietario != null) {
//            String numCuenta = crearNumCuenta();
//            Cuenta cuentaAhorros = new Cuenta(numCuenta, saldoInicial, propietario);
//            listaCuentas.add(cuentaAhorros);
//            return numCuenta;
//        }
//        throw new Exception("No se encontró el usuario con el número de identificación: " + id);
//    }
//
//    //consultar cuentas de un usuario
//
//    public List<Cuenta> consultarCuentasUsuario(String identificacion, String contraseña) throws Exception {
//        Usuario usuario = validarUsuario(identificacion, contraseña);
//        if (usuario != null) {
//            return consultarCuentasUsuario(identificacion, 0, new ArrayList<>());
//        }
//        return null;
//    }
//
//    // Método que consulta el saldo de las cuentas de ahorros de un usuario
//    private List<Cuenta> consultarCuentasUsuario(String id, int indice, List<Cuenta> cuentas) {
//        if (indice >= cuentas.size()) {
//            return cuentas;
//        }
//        Cuenta cuentaActual = cuentas.get(indice);
//        if (cuentaActual.getPropietario().getIdUsuario().equals(id)) {
//            cuentas.add(cuentaActual);
//        }
//        return consultarCuentasUsuario(id, indice + 1, cuentas);
//    }
//
//    //Transferencias
//
//    public void realizarTransferencia(String numCuentaOrigen, String numCuentaDestino, float monto) throws Exception {
//        Cuenta cuentaOrigen = obtenerCuenta(numCuentaOrigen);
//        Cuenta cuentaDestino = obtenerCuenta(numCuentaDestino);
//
//        if (cuentaOrigen != null && cuentaDestino != null) {
//            cuentaOrigen.transferir(monto, cuentaDestino);
//
//
//        } else {
//            throw new Exception("Error con los números de cuenta");
//        }
//    }
//    // Consultar saldo
//    public Float ConsultarSaldo(String identificacion, String contrasena) throws Exception {
//
//        Usuario usuario = validarUsuario(identificacion, contrasena);
//        if (usuario != null) {
//            return consultarSaldoRecursivo(identificacion, 0, 0f);
//        }
//        return null;
//    }
//
//    private Float consultarSaldoRecursivo(String identificacion, int index, float saldo) {
//
//        if (index >= listaCuentas.size()) {
//            return saldo;
//        }
//        if (listaCuentas.get(index).getPropietario().getIdUsuario().equals(identificacion)) {
//            saldo = listaCuentas.get(index).getSaldo(); // actualizar el saldo
//        }
//        return consultarSaldoRecursivo(identificacion, index + 1, saldo);
//    }



}
