package co.urwallet.mapping.dto;

public record UsuarioDto (
        String idUsuario,
        String cedula,
        String nombreCompleto,
        String telefono,
        String correo,
        String contrasena,
        String direccion,
        Float saldoDispo
){
}