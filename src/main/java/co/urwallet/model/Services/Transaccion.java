package co.urwallet.model.Services;
import co.urwallet.model.TipoTransaccion;
import co.urwallet.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;


@Builder
@Getter
@Setter

public class Transaccion {
    private final TipoTransaccion tipo;
    private final float monto;
    private final Usuario usuario;
    private final LocalDateTime fecha;
    public Transaccion(TipoTransaccion tipo, float monto, Usuario usuario, LocalDateTime fecha){
        this.tipo = tipo;
        this.monto = monto;
        this.usuario = usuario;
        this.fecha = fecha;
    }
}