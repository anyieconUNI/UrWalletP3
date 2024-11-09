package co.urwallet.mapping.dto;

import java.util.Date;

public record TransaccionDto(
         String idTransaccion,
         Date fecha,
         String tipoTransaccion,
         float monto,
         String descripcion,
         String cuentaOrigen,
         String cuentaDestino,
         String categoria
) {
}
