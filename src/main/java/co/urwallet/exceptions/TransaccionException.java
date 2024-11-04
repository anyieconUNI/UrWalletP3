package co.urwallet.exceptions;

public class TransaccionException  extends Exception {
    // Constructor que acepta solo un mensaje
    public TransaccionException(String message) {
        super(message);
    }

    // Constructor que acepta un mensaje y una causa
    public TransaccionException(String message, Throwable cause) {
        super(message, cause);
    }
}
