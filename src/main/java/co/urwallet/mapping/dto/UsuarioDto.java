package co.urwallet.mapping.dto;

import co.urwallet.model.Cuenta;

import java.util.List;

public record UsuarioDto (
        String idUsuario,
        String cedula,
        String nombreCompleto,
        String telefono,
        String correo,
        String contrasena,
        String direccion,
        Float saldoDispo,
        List<Cuenta> cuentasBancarias
){
}