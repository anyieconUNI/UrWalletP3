package co.urwallet.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class Usuario implements Serializable {
    private String idUsuario;
    private String cedula;
    private String nombreCompleto;
    private String telefono;
    private String correo;
    private String contrasena;
    private String direccion;
    private Float saldoDispo;

    public Usuario(){

    }

    public Usuario(String cedula, String nombreCompleto, String correo, String contrasena, String idUsuario, String telefono, String direccion, Float saldoDispo) {
        this.idUsuario = idUsuario;
        this.cedula = cedula;
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.contrasena = contrasena;
        this.telefono = telefono;
        this.direccion = direccion;
        this.saldoDispo = saldoDispo;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTelefono() {
        return telefono;
    }

    public String generaridUsuario() {
        this.idUsuario = UUID.randomUUID().toString();
        return idUsuario;
    }
}
