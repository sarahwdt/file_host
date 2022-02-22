package org.spbstu.file_host.exception.server;

import org.spbstu.file_host.exception.server.file_system.FileSystemException;

public class CannotFindFreeNumberForFileNameException extends FileSystemException {
    public CannotFindFreeNumberForFileNameException() {
        super("Сервис генерации имени файла не может подобрать индекс для файла.");
    }
}
