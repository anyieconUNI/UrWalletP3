package co.urwallet.model;

import java.io.Serializable;
import java.util.UUID;

public class Presupuesto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String idPresupuesto;
    private String nombre;
    private float montoTotal;
    private float montoGasto;
    private Categoria categoria;

    public Presupuesto(String idPresupuesto, String nombre, float montoTotal, float montoGasto, Categoria categoria) {
        this.idPresupuesto = idPresupuesto;
        this.nombre = nombre;
        this.montoTotal = montoTotal;
        this.montoGasto = montoGasto;
        this.categoria = categoria;
    }
    public Presupuesto(){

    }

    public String getIdPresupuesto() {
        return idPresupuesto;
    }

    public void setIdPresupuesto(String idPresupuesto) {
        this.idPresupuesto = idPresupuesto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(float montoTotal) {
        this.montoTotal = montoTotal;
    }

    public float getMontoGasto() {
        return montoGasto;
    }

    public void setMontoGasto(float montoGasto) {
        this.montoGasto = montoGasto;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
    public String generaridPresupuesto() {
        this.idPresupuesto = UUID.randomUUID().toString();
        return idPresupuesto;
    }
}
