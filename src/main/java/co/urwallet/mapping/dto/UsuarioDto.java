package co.urwallet.mapping.dto;

import co.urwallet.model.Usuario;

public record UsuarioDto (
        String idUsuario,
        String cedula,
        String nombreCompleto,
        String telefono,
        String contrasena,
         String direccion,
         Float saldoDispo
) extends UsuarioDto(String correo) {
}
