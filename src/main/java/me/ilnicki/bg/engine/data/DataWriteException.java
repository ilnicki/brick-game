package me.ilnicki.bg.engine.data;

public class DataWriteException extends Exception {
    public DataWriteException() {
    }

    public DataWriteException(String message) {
        super(message);
    }

    public DataWriteException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataWriteException(Throwable cause) {
        super(cause);
    }
}
