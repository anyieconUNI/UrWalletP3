package co.urwallet.mapping.mappers;
import co.urwallet.mapping.dto.CuentaDto;
import co.urwallet.mapping.dto.PresupuestoDto;
import co.urwallet.mapping.dto.TransaccionDto;
import co.urwallet.model.Cuenta;
import co.urwallet.model.Presupuesto;
import co.urwallet.model.Transaccion;
import co.urwallet.model.Usuario;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import co.urwallet.mapping.dto.UsuarioDto;


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


    CuentaDto cuentaToCuentaDto(Cuenta cuenta);

    Cuenta cuentaToCuentaDto(CuentaDto cuentaDto);

    PresupuestoDto presupuestoToPresupuestoDto(Presupuesto presupuesto);

    Presupuesto presupuestoDtoToPresupuesto(PresupuestoDto presupuestoDto);

    List<PresupuestoDto>getPresupuestosDto(List<Presupuesto> listaPresupuestos);
    List<CuentaDto> getCuentaDto(List<Cuenta> listaCuentas);

    // Métodos personalizados para mapear entre Cuenta y String
    default String map(Cuenta cuenta) {
        return cuenta != null ? cuenta.getNumeCuenta() : null;
    }
    @Named("stringToCuenta")
    default Cuenta stringToCuenta(String value) {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeCuenta(value);
        return cuenta;
    }

    // Mapeo de TransaccionDto a Transaccion, aplicando la conversión de String a Cuenta
    @Mapping(source = "cuentaOrigen", target = "cuentaOrigen", qualifiedByName = "stringToCuenta")
    @Mapping(source = "cuentaDestino", target = "cuentaDestino", qualifiedByName = "stringToCuenta")
    Transaccion transaccionDtoToTransaccion(TransaccionDto transaccionDto);

    // Mapeo de Transaccion a TransaccionDto, aplicando la conversión de Cuenta a String
    @Mapping(source = "cuentaOrigen.numeCuenta", target = "cuentaOrigen")
    @Mapping(source = "cuentaDestino.numeCuenta", target = "cuentaDestino")
    TransaccionDto transaccionToTransaccionDto(Transaccion transaccion);

    // Método para mapear una lista de Transaccion a TransaccionDto
    List<TransaccionDto> transaccionesToTransaccionesDto(List<Transaccion> listaTransaccion);
}