package co.urwallet.model;
import co.urwallet.model.Services.Transaccion;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@ToString
public class Cuenta implements Serializable {
    private static final long serialVersionUID = 1L;

    private String idCuenta;
    private String numeCuenta;
    private String nombreCuenta;
    private TipoCuenta tipoCuenta;
    private Float saldo;
    private String clienteId;


    public Cuenta(String idCuenta, String numeCuenta, String nombreCuenta, TipoCuenta tipoCuenta, Float saldo,String clienteId) {
        this.idCuenta = idCuenta;
        this.numeCuenta = numeCuenta;
        this.nombreCuenta = nombreCuenta;
        this.tipoCuenta = tipoCuenta;
        this.saldo = saldo;
        this.clienteId = clienteId;
    }
    public Cuenta(){

    }

    public String getIdCuenta() {
        return idCuenta;
    }

    public String getNombreCuenta() {
        return nombreCuenta;
    }

    public String getNumeCuenta() {
        return numeCuenta;
    }

    public TipoCuenta getTipoCuenta() {
        return tipoCuenta;
    }

    public void setIdCuenta(String idCuenta) {
        this.idCuenta = idCuenta;
    }

    public void setNombreCuenta(String nombreCuenta) {
        this.nombreCuenta = nombreCuenta;
    }

    public void setNumeCuenta(String numeCuenta) {
        this.numeCuenta = numeCuenta;
    }

    public void setTipoCuenta(TipoCuenta tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public Float getSaldo() {
        return saldo;
    }

    public void setSaldo(Float saldo) {
        this.saldo = saldo;
    }
    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public String getClienteId() {
        return clienteId;
    }

    public String generaridCuenta() {
        this.idCuenta = UUID.randomUUID().toString();
        return idCuenta;
    }
}


