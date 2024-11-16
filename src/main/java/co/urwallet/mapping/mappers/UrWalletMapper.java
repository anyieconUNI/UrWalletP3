package co.urwallet.mapping.mappers;
import co.urwallet.mapping.dto.CuentaDto;
import co.urwallet.mapping.dto.TransaccionDto;
import co.urwallet.model.Cuenta;
import co.urwallet.model.Transaccion;
import co.urwallet.model.Usuario;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import co.urwallet.mapping.dto.UsuarioDto;


import java.util.ArrayList;
import java.util.List;
@Mapper
public interface UrWalletMapper {
    UrWalletMapper INSTANCE = Mappers.getMapper(UrWalletMapper.class);

    // Mapea de Usuario a UsuarioDto
    UsuarioDto usuarioToUsuarioDto(Usuario usuario);

    // Mapea de UsuarioDto a Usuario
    Usuario usuarioDtoToUsuario(UsuarioDto usuarioDto);

    // Mapea una lista de Usuarios a una lista de UsuarioDto
    List<UsuarioDto> getUsuariosDto(List<Usuario> listaUsuarios);

    //////////////////////////
    CuentaDto cuentaToCuentaDto(Cuenta cuenta);

    // Mapea de UsuarioDto a Usuario
    Cuenta cuentaToCuentaDto(CuentaDto cuentaDto);

    // Mapea una lista de Usuarios a una lista de UsuarioDto
    List<CuentaDto> getCuentaDto(List<Cuenta> listaCuentas);
    TransaccionDto clienteToClienteDto(Transaccion cliente);
    Transaccion transaccionToTransaccionDto(TransaccionDto transaccionDto);
    List<TransaccionDto> gettransaccionesToTransaccionesDto(ArrayList<Transaccion> listaTransaccion);


}