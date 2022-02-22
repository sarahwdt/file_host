package org.spbstu.file_host.exception.server.file_system;

import java.nio.file.Path;
import java.util.Locale;

public class NoAccessException extends FileSystemException {
    private final Path path;
    private final String accessType;

    public NoAccessException(Path path, String accessType) {
        super("Отсутствуют права на " + accessType.toLowerCase(Locale.ROOT) + " для '" + path.toAbsolutePath() + "'");
        this.path = path;
        this.accessType = accessType;
    }

    public String getAccessType() {
        return accessType;
    }

    public Path getPath() {
        return path;
    }
}
