package me.ilnicki.bg.engine.system.container;

public class ProvisionException extends RuntimeException {
    public ProvisionException(Throwable cause) {
        super(cause);
    }

    public ProvisionException(String cause) {
        super(cause);
    }
}
