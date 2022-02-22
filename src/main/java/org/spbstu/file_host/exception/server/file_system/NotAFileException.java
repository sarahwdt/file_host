package org.spbstu.file_host.exception.server.file_system;

import java.nio.file.Path;

public class NotAFileException extends FileSystemException {
    private final Path path;

    public NotAFileException(Path path) {
        super("'" + path.toAbsolutePath() + "' не файл.");
        this.path = path;
    }

    public Path getPath() {
        return path;
    }
}
