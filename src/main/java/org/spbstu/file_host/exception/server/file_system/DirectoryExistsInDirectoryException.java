package org.spbstu.file_host.exception.server.file_system;

import java.nio.file.Path;

public class DirectoryExistsInDirectoryException extends FileSystemException {
    public DirectoryExistsInDirectoryException(Path path) {
        super("Директория уже существует: " + path.toAbsolutePath());
    }
}
