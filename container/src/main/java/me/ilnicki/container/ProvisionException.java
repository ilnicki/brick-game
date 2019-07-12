package me.ilnicki.container;

public class ProvisionException extends RuntimeException {
    public ProvisionException(Throwable cause) {
        super(cause);
    }

    public ProvisionException(String message) {
        super(message);
    }

    public ProvisionException(String message, Throwable cause) {
        super(message, cause);
    }
}
