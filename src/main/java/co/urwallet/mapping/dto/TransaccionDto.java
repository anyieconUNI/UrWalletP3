package co.urwallet.mapping.dto;
import co.urwallet.model.Categoria;
import co.urwallet.model.Cuenta;
import co.urwallet.model.TipoTransaccion;

import java.util.Date;
import java.util.List;

public record TransaccionDto(
        String idTransaccion,
        String fecha,
        TipoTransaccion tipoTransaccion,
        float monto,
        String descripcion,
        Cuenta cuentaOrigen,
        Cuenta cuentaDestino,
        Categoria categoria
) {
}

