package org.spbstu.file_host.service.file_system;

import org.spbstu.file_host.exception.server.ConfigurationFatalError;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class VirtualFileSystemInitializerServiceTest {
    private static Path notExists;

    private static Path notDirectory;

    @BeforeAll
    static void setUp() throws IOException {
        notDirectory = Paths.get("not_directory");
        Files.createFile(notDirectory);
        notExists = Paths.get("not_exists");
    }

    @AfterAll
    static void tearDown() throws IOException {
        Files.deleteIfExists(notDirectory);
    }

    @Test
    void constructor_SuccessThrowDoesNotExists() {
        Assertions.assertThrows(ConfigurationFatalError.class,
                () -> new VirtualFileSystemInitializerService(notExists.toString()));
    }

    @Test
    void constructor_SuccessThrowNotADirectory() {
        Assertions.assertThrows(ConfigurationFatalError.class,
                () -> new VirtualFileSystemInitializerService(notDirectory.toString()));
    }
}