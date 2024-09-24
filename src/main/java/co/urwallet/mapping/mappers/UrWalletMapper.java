package co.urwallet.mapping.mappers;
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
    @Named("usuarioToUsuarioDto")
    UsuarioDto usuarioToUsuarioDto(Usuario usuario);

    // Mapea de UsuarioDto a Usuario
    Usuario usuarioDtoToUsuario(UsuarioDto usuarioDto);

    // Mapea una lista de Usuarios a una lista de UsuarioDto
    @IterableMapping(qualifiedByName = "usuarioToUsuarioDto")
    List<UsuarioDto> getUsuariosDto(List<Usuario> listaUsuarios);

}
