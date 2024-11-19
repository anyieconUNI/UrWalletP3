package co.urwallet.exceptions;

public class PresupuestoException extends Exception{
    // Constructor que acepta solo un mensaje
    public PresupuestoException(String message) {
        super(message);
    }

    // Constructor que acepta un mensaje y una causa
    public PresupuestoException(String message, Throwable cause) {
        super(message, cause);
    }

}
