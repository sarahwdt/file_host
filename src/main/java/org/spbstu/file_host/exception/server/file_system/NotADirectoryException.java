package org.spbstu.file_host.exception.server.file_system;

import java.nio.file.Path;

public class NotADirectoryException extends FileSystemException {
    private final Path path;

    public NotADirectoryException(Path path) {
        super("'" + path.toAbsolutePath() + "' не директория.");
        this.path = path;
    }

    public Path getPath() {
        return path;
    }
}
