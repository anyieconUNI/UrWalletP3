package co.urwallet.model.Services;

import co.urwallet.exceptions.UsuarioException;
import co.urwallet.model.Usuario;

public interface IUrWalletService {

    boolean verificarUsuarioExistente(String id) throws UsuarioException;

    boolean eliminarUsuario(String id) throws UsuarioException;

    //    @Override
    //    public boolean eliminarUsuario(String id) throws UsuarioException {
    //        Usuario usuario = null;
    //        boolean flag = false;
    //        usuario = obtenerUsuario(id);
    //        if (usuario == null)
    //            throw new UsuarioException("El usuario no existe");
    //        else {
    //            getListaUsuarios().remove(usuario);
    //            flag = true;
    //        }
    //        return flag;
    //    }
    //
    Usuario obtenerUsuario(String id);
    boolean actualizarUsuario(String idActual, Usuario usuario) throws UsuarioException;
}
