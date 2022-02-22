package org.spbstu.file_host.exception.server.file_system;

import java.nio.file.Path;

public class DoesNotExistsException extends FileSystemException {
    private final Path path;

    public DoesNotExistsException(Path path) {
        super("Отсутствует объект: " + path.toAbsolutePath());
        this.path = path;
    }

    public Path getPath() {
        return path;
    }
}
