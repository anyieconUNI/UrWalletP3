package co.urwallet.mapping.dto;

import co.urwallet.model.TipoCuenta;

public record CuentaDto(
        String idCuenta,
        String numeCuenta,
        String nombreCuenta,
        String tipoCuenta,
        Float saldo
) {
}
