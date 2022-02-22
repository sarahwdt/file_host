package org.spbstu.file_host.exception.server.file_system;

import java.nio.file.Path;

public class FileDoesNotExistsException extends FileSystemException {
    private final Path path;

    public FileDoesNotExistsException(Path path) {
        super("Отсутствует файл: " + path.toAbsolutePath());
        this.path = path;
    }

    public Path getPath() {
        return path;
    }
}
