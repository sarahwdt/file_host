package org.spbstu.file_host.util;

import org.spbstu.file_host.exception.server.file_system.*;

import java.nio.file.Files;
import java.nio.file.Path;

public class FileSystemValidator {
    private final boolean exists;
    private final boolean file;
    private final boolean directory;
    private final boolean readable;
    private final boolean writable;

    private FileSystemValidator(boolean exists, boolean file, boolean directory, boolean readable, boolean writable) {
        this.exists = exists;
        this.file = file;
        this.directory = directory;
        this.readable = readable;
        this.writable = writable;
    }

    public static FileSystemValidatorBuilder builder() {
        return new FileSystemValidatorBuilder();
    }

    public void validate(Path path) throws FileSystemException {
        if (exists && !Files.exists(path)) throw new DoesNotExistsException(path);
        if (directory && !Files.isDirectory(path)) throw new NotADirectoryException(path);
        if (file && !Files.isRegularFile(path)) throw new NotAFileException(path);
        if (readable && !Files.isReadable(path)) throw new NoAccessException(path, "Чтение");
        if (writable && !Files.isWritable(path)) throw new NoAccessException(path, "Запись");
    }

    public static class FileSystemValidatorBuilder {
        private boolean exists = false;
        private boolean file = false;
        private boolean directory = false;
        private boolean readable = false;
        private boolean writable = false;

        private FileSystemValidatorBuilder() {
        }

        public FileSystemValidatorBuilder exists() {
            this.exists = true;
            return this;
        }

        public FileSystemValidatorBuilder file() {
            this.file = true;
            this.directory = false;
            return this;
        }

        public FileSystemValidatorBuilder directory() {
            this.directory = true;
            this.file = false;
            return this;
        }

        public FileSystemValidatorBuilder readable() {
            this.readable = true;
            return this;
        }

        public FileSystemValidatorBuilder writable() {
            this.writable = true;
            return this;
        }

        @Override
        public String toString() {
            return "FileSystemValidatorBuilder{" +
                    "exists=" + exists +
                    ", file=" + file +
                    ", directory=" + directory +
                    ", readable=" + readable +
                    ", writable=" + writable +
                    '}';
        }

        public FileSystemValidator build() {
            return new FileSystemValidator(exists, file, directory, readable, writable);
        }
    }
}
