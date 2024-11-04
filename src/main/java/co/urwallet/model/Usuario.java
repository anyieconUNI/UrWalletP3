package co.urwallet.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;


public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    private String idUsuario;
    private String cedula;
    private String nombreCompleto;
    private String telefono;
    private String correo;
    private String contrasena;
    private String direccion;
    private Float saldoDispo;
    private List<Cuenta> cuentasBancarias;

    public Usuario(){

    }

    public Usuario(String cedula, String nombreCompleto, String correo, String contrasena, String idUsuario, String telefono, String direccion, Float saldoDispo, List<Cuenta> cuentasBancarias) {
        this.idUsuario = idUsuario;
        this.cedula = cedula;
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.contrasena = contrasena;
        this.telefono = telefono;
        this.direccion = direccion;
        this.saldoDispo = saldoDispo;
        this.cuentasBancarias = cuentasBancarias;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public String getCedula() {
        return cedula;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getCorreo() {
        return correo;
    }

    public Float getSaldoDispo() {
        return saldoDispo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public void setSaldoDispo(Float saldoDispo) {
        this.saldoDispo = saldoDispo;
    }
    public List<Cuenta> getCuentasBancarias() {
        return cuentasBancarias;
    }

    public void setCuentasBancarias(List<Cuenta> cuentasBancarias) {
        this.cuentasBancarias = cuentasBancarias;
    }
    public String generaridUsuario() {
        this.idUsuario = UUID.randomUUID().toString();
        return idUsuario;
    }
}
