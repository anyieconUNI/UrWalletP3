package co.urwallet.model;

import co.urwallet.exceptions.CuentaException;
import co.urwallet.exceptions.PresupuestoException;
import co.urwallet.exceptions.TransaccionException;
import co.urwallet.exceptions.UsuarioException;
import co.urwallet.model.Services.IUrWalletService;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;

@Getter
public class UrWallet implements IUrWalletService , Serializable {

    private static final long serialVersionUID = 1L;
    ArrayList<Usuario> listaUsuarios = new ArrayList<>();
    ArrayList<Cuenta> listaCuentas = new ArrayList<>();
    ArrayList<Transaccion> listaTransaccion = new ArrayList<>();
    ArrayList<Presupuesto> listaPresupuestos = new ArrayList<>();


    public UrWallet() {}

    public ArrayList<Usuario> getListaUsers() {
        return listaUsuarios;
    }

    public  void setListaUsers(ArrayList<Usuario> listaClientes){
        this.listaUsuarios = listaUsuarios;
    }

    public ArrayList<Cuenta> getListaCuentas() {
        return listaCuentas;
    }

    public void setListaCuentas(ArrayList<Cuenta> listaCuentas) {
        this.listaCuentas = listaCuentas;
    }

    public ArrayList<Presupuesto> getListaPresupuestos() {return listaPresupuestos;}

    public void setListaPresupuestos(ArrayList<Presupuesto> listaPresupuestos) {}

    public ArrayList<Transaccion> getListaTransaccion() {
        return listaTransaccion;
    }

    public void setListaTransaccion(ArrayList<Transaccion> listaTransaccion) {this.listaTransaccion = listaTransaccion;}

    public void agregarUsuario(Usuario nuevoUsuario) throws UsuarioException {
        getListaUsers().add(nuevoUsuario);
    }

    @Override
    public boolean verificarUsuarioExistente(String idUser) throws UsuarioException {
        if (usuarioExiste(idUser)) {
            throw new UsuarioException("El usuario ya existe");
        } else {
            return false;
        }
    }


    public boolean usuarioExiste(String id) {
        boolean usuarioEncontrado = false;
        for (Usuario usuario : getListaUsers()) {
            if (usuario.getCedula().equalsIgnoreCase(id)) {
                usuarioEncontrado = true;
                break;
            }
        }
        return usuarioEncontrado;
    }

    public Usuario buscarUserLogin(String email) {
        for (Usuario user : getListaUsers()) {
            System.out.println("AQQQQQQQQQQQQQQQQQQ" + user.getCorreo() + email);
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
                getListaUsers().remove(usuario);
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
        for (Usuario usuario : getListaUsers()) {
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
    public void agregarCuenta(Cuenta nuevaCuenta) throws CuentaException {
        getListaCuentas().add(nuevaCuenta);
    }
    @Override
    public boolean actualizarCuenta(String idActual, Cuenta cuenta) throws CuentaException {
        try {
            System.out.print("AQUIIIIIIIIIIIIIII" + idActual);
            Cuenta cuentaActual = obtenerCuenta(idActual);

            if (cuentaActual == null) {
                throw new CuentaException("La cuenta no existe");
            } else {
                cuentaActual.setNombreCuenta(cuenta.getNombreCuenta());
                cuentaActual.setTipoCuenta(cuenta.getTipoCuenta());
                cuentaActual.setSaldo(cuenta.getSaldo());
                return true;
            }
        } catch (CuentaException e) {
            // Manejo específico de UsuarioException
            System.err.println("Error en actualizarUsuario: " + e.getMessage());
            throw e; // Vuelve a lanzar la excepción
        } catch (Exception e) {
            // Manejo de cualquier otra excepción no prevista
            System.err.println("Error inesperado: " + e.getMessage());
            throw new CuentaException("Error inesperado al actualizar el usuario");
        }
    }

    @Override
    public boolean eliminarCuenta(String id) throws CuentaException {
        try {
            Cuenta cuenta = obtenerCuenta(id);

            if (cuenta == null) {
                throw new CuentaException("La cuenta no existe");
            } else {
                getListaCuentas().remove(cuenta);
                return true;
            }
        } catch (CuentaException e) {
            System.err.println("Error en eliminar : " + e.getMessage());
            throw e;
        }
    }
    @Override
    public Cuenta obtenerCuenta(String id) {
        Cuenta cuentaEncontrado = null;
        for (Cuenta cuenta : getListaCuentas()) {
            if (cuenta.getNumeCuenta().equalsIgnoreCase(id)) {
                cuentaEncontrado = cuenta;
                break;
            }
        }
        return cuentaEncontrado;
    }

    @Override
    public boolean verificarCuentaExistente(String idCuenta) throws CuentaException {
        if (cuentaExiste(idCuenta)) {
            throw new CuentaException("La cuenta ya existe");
        } else {
            return false;
        }
    }

    public boolean cuentaExiste(String id) {
        boolean cuentaEncontrado = false;
        for (Cuenta cuenta : getListaCuentas()) {
            if (cuenta.getNumeCuenta().equalsIgnoreCase(id)) {
                cuentaEncontrado = true;
                break;
            }
        }
        return cuentaEncontrado;
    }

    public void agregarTransaccion(Transaccion nuevaTransaccion) throws TransaccionException {
        getListaTransaccion().add(nuevaTransaccion);
    }

    public void agregarPrecioACuenta(float precio, Cuenta cuenta){
        float saldoActual = cuenta.getSaldo();
        cuenta.setSaldo(saldoActual+precio);
        System.out.println("ACTUALIZA");
    }

    public void restarPrecioACuenta(float precio, Cuenta cuenta){
        float saldoActual = cuenta.getSaldo();
        cuenta.setSaldo(saldoActual-precio);
    }

    @Override
    public boolean verificarCuentaExistenteTrans(String idCuenta) throws TransaccionException {
        if (!cuentaExiste(idCuenta)) {
            throw new TransaccionException("La cuenta no existe");
        } else {
            return false;
        }
    }

    public Usuario obtenerUsuarioPorCedula(String cedula) {
        for (Usuario usuario : listaUsuarios) {
            if (usuario.getCedula().equalsIgnoreCase(cedula)) {
                return usuario;
            }
        }
        return null;
    }

    // Método para verificar si la cuenta ya está asignada a un usuario
    public boolean cuentaYaAsignada(String numeroCuenta) {
        for (Usuario usuario : listaUsuarios) {
            for (Cuenta cuenta : usuario.getCuentasBancarias()) {
                if (cuenta.getNumeCuenta().equalsIgnoreCase(numeroCuenta)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Métodos de presupuesto

    public void agregarPresupuesto(Presupuesto nuevoPresupuesto){
        getListaPresupuestos().add(nuevoPresupuesto);
    }

    public Presupuesto obtenerPresupuesto(String idPresupuesto) {
        Presupuesto presupuestoEncontrado = null;
        for (Presupuesto presupuesto : getListaPresupuestos()) {
            if (presupuesto.getIdPresupuesto().equalsIgnoreCase(idPresupuesto)) {
                presupuestoEncontrado = presupuesto;
                break;
            }
        }
        return presupuestoEncontrado;
    }

    public boolean actualizarPresupuesto(String idPresupuesto, Presupuesto presupuesto) throws PresupuestoException {
        try {
            System.out.print("AQUIIIIIIIIIIIIIII" + idPresupuesto);
            Presupuesto presupuestoActual = obtenerPresupuesto(idPresupuesto);

            if (presupuestoActual == null) {
                throw new PresupuestoException("El presupuesto no existe");
            } else {
                presupuestoActual.setNombre(presupuesto.getNombre());
                presupuestoActual.setMontoTotal(presupuesto.getMontoTotal());
                presupuestoActual.setMontoGasto(presupuesto.getMontoGasto());
                presupuestoActual.setCategoria(presupuesto.getCategoria());
                return true;
            }
        } catch (PresupuestoException e) {
            System.err.println("Error en actualizarPresupuesto: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            throw new PresupuestoException("Error inesperado al actualizar el presupuesto");
        }
    }

    public boolean eliminarPresupuesto(String idPresupuesto) throws PresupuestoException {
        try {
            Presupuesto presupuesto = obtenerPresupuesto(idPresupuesto);

            if (presupuesto == null) {
                throw new PresupuestoException("El presupuesto no existe");
            } else {
                getListaPresupuestos().remove(presupuesto);
                return true;
            }
        } catch (PresupuestoException e) {
            System.err.println("Error en eliminar : " + e.getMessage());
            throw e;
        }
    }

    public boolean verificarPresupuestoExistente(String idPresupuesto) throws PresupuestoException {
        if (presupuestoExiste(idPresupuesto)) {
            throw new PresupuestoException("El presupuesto ya existe");
        } else {
            return false;
        }
    }

    public boolean presupuestoExiste(String idPresupuesto) {
        boolean presupuestoEncontrado = false;
        for (Presupuesto presupuesto : getListaPresupuestos()) {
            if (presupuesto.getIdPresupuesto().equalsIgnoreCase(idPresupuesto)) {
                presupuestoEncontrado = true;
                break;
            }
        }
        return presupuestoEncontrado;
    }
}