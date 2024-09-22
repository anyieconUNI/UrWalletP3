package co.urwallet.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Persona {
    private String cedula;
    private String nombreCompleto;
    private String correo;
    private String Contrasena;

    public Persona() {
    }
}