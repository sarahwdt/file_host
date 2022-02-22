package org.spbstu.file_host.exception.server.file_system;

import java.nio.file.Path;

public class FileExistsInDirectoryException extends FileSystemException {
    public FileExistsInDirectoryException(Path path) {
        super("Файл уже существует: " + path.toAbsolutePath());
    }
}
