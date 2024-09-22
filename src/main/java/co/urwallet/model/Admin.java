package co.urwallet.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Admin extends Persona{
    private String nombre;
    private String apellido;
    private String correo;
    private String direccion;
    private String telefono;
    private String contraseña;
    public Admin() {}
}
