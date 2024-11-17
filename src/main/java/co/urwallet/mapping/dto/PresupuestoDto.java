package co.urwallet.mapping.dto;

import co.urwallet.model.Categoria;

public record PresupuestoDto(
        String idPresupuesto,
        String nombre,
        Float montoTotal,
        Float montoGasto,
        Categoria categoria
) {
}
