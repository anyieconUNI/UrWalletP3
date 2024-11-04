package co.urwallet.exceptions;

public class CuentaException extends Exception {
    // Constructor que acepta solo un mensaje
    public CuentaException(String message) {
        super(message);
    }

    // Constructor que acepta un mensaje y una causa
    public CuentaException(String message, Throwable cause) {
        super(message, cause);
    }
}
