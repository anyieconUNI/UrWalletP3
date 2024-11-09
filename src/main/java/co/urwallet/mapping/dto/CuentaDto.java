package co.urwallet.mapping.dto;

public record CuentaDto(
        String idCuenta,
        String numeCuenta,
        String nombreCuenta,
        String tipoCuenta,
        Float saldo
) {
}
