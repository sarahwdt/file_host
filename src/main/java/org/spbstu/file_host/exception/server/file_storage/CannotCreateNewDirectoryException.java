package org.spbstu.file_host.exception.server.file_storage;

import org.spbstu.file_host.exception.server.ServerSideException;

public class CannotCreateNewDirectoryException extends ServerSideException {
    public CannotCreateNewDirectoryException() {
        super("Не удается создать новую директорию с использованием текущей стратегией именования");
    }
}
