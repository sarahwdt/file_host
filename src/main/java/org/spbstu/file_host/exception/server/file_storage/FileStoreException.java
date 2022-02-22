package org.spbstu.file_host.exception.server.file_storage;

import org.spbstu.file_host.exception.server.ServerSideException;

public class FileStoreException extends ServerSideException {
    public FileStoreException() {
        super();
    }

    public FileStoreException(String message) {
        super(message);
    }

    public FileStoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileStoreException(Throwable cause) {
        super(cause);
    }

    public FileStoreException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
