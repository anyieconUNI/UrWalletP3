package co.urwallet.model;
import co.urwallet.model.Services.Transaccion;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Getter
@ToString
public class Cuenta {
    private final String numCuenta;
    private float saldo;
    private final List<Transaccion> transacciones;
    private final Usuario propietario;

    //constructor
    public Cuenta(String numCuenta, float saldoInicial, Usuario propietario) {
        this.numCuenta=numCuenta;
        this.saldo=saldoInicial;
        this.transacciones=new ArrayList<>();
        this.propietario=propietario;
    }
//    public  void depositar(float cantidad, Usuario emisor) {
//        saldo+=cantidad;
//        Transaccion transaccion=Transaccion.builder()
//                .tipo(TipoTransaccion.Deposito)
//                .monto(cantidad)
//                .usuario(emisor)
//                .fecha(LocalDateTime.now())
//                .build();
//        transacciones.add(transaccion);
//    }
//
//    // metodo que realiza el retiro de la cuenta de ahorros y registra la transaccion
//    public void transferir(float cantidad, Cuenta cuentaDestino) throws Exception{
//
//        // Se valida que el saldo sea suficiente
//        if (saldo >= cantidad) {
//
//            // Se realiza el retiro
//            saldo -= cantidad;
//            cuentaDestino.depositar(cantidad,propietario);
//            Transaccion transaccion=Transaccion.builder()
//                    .tipo(TipoTransaccion.Retiro)
//                    .monto(cantidad)
//                    .usuario(cuentaDestino.getPropietario())
//                    .fecha(LocalDateTime.now())
//                    .build();
//
//            // Se registra la transacción de retiro en la cuenta de origen
//            transacciones.add(transaccion);
//
//        } else {
//            throw new Exception("Saldo insuficiente");
//        }
//    }
//    // Método que obtiene las transaccionnes de un mes o año especifico con recursividad
//    public List<Transaccion> obtenerTransaccionesPeriodo(int mes, int anio, int indice, List<Transaccion> transaccionesMes) {
//        if (indice >= transacciones.size()) {
//            return transaccionesMes;
//        }
//        Transaccion transaccion = transacciones.get(indice);
//        if (transaccion.getFecha().getMonthValue() == mes && transaccion.getFecha().getYear() == anio) {
//            transaccionesMes.add(transaccion);
//        }
//        return obtenerTransaccionesPeriodo(mes, anio, indice + 1, transaccionesMes);
//    }
//    public List<Transaccion> obtenerTransaccionesPeriodo(int mes, int anio) {
//        return obtenerTransaccionesPeriodo(mes, anio, 0, new ArrayList<>());
//    }

}
