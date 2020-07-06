package me.ilnicki.container;

public class ProvisionException extends RuntimeException {
  ProvisionException(String message) {
    super(message);
  }

  ProvisionException(String message, Throwable cause) {
    super(message, cause);
  }
}
