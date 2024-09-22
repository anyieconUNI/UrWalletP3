package co.urwallet.model;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Usuario extends Persona {
    private  String idUsuario;
    private String telefono;
    private  String direccion;
    private Float saldoDispo;

    public Usuario() {

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
