package co.urwallet.mapping.dto;
import co.urwallet.model.Cuenta;
import co.urwallet.model.TipoTransaccion;

import java.util.Date;
import java.util.List;

public record TransaccionDto(
         String idTransaccion,
         Date fecha,
         String tipoTransaccion,
         float monto,
         String descripcion,
         Cuenta cuentaOrigen,
         Cuenta cuentaDestino,
         String categoria
) {
}
