package co.urwallet.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Usuario extends Persona {
    private  String idUsuario;
    private String telefono;
    private  String direccion;
    private Float saldoDispo;

    public Usuario(String cedula, String nombreCompleto, String correo, String contrasena, String idUsuario, String telefono, String direccion, Float saldoDispo) {
        super(cedula, nombreCompleto, correo, contrasena);
        this.idUsuario = idUsuario;
        this.telefono = telefono;
        this.direccion = direccion;
        this.saldoDispo = saldoDispo;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public  String generaridUsuario() {
        this.idUsuario = UUID.randomUUID().toString();
        System.out.println("ID GENERADOOOOOOOOOOOOOOOO"+ this.idUsuario);
        return null;
    }

}
