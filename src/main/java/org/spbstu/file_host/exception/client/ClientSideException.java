package org.spbstu.file_host.exception.client;

public class ClientSideException extends RuntimeException {
    public ClientSideException() {
    }

    public ClientSideException(String message) {
        super(message);
    }

    public ClientSideException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientSideException(Throwable cause) {
        super(cause);
    }

    public ClientSideException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
