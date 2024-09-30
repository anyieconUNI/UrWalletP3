package co.urwallet.exceptions;

public class UsuarioException extends Exception {

    // Constructor que acepta solo un mensaje
    public UsuarioException(String message) {
        super(message);
    }

    // Constructor que acepta un mensaje y una causa
    public UsuarioException(String message, Throwable cause) {
        super(message, cause);
    }
}

//public class UsuarioException extends Exception {
//    public UsuarioException(String msg) {super(msg);}

//}
