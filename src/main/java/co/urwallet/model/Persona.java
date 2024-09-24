package co.urwallet.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Persona {
    private String cedula;
    private String nombreCompleto;
    private String correo;
    private String contrasena;

}