package org.spbstu.file_host.service.file_system;

import org.spbstu.file_host.exception.server.file_system.DirectoryExistsInDirectoryException;
import org.spbstu.file_host.exception.server.file_system.FileExistsInDirectoryException;
import org.spbstu.file_host.exception.server.file_system.NotAFileException;
import org.aspectj.apache.bcel.util.ByteSequence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@ExtendWith(MockitoExtension.class)
class FileSystemWrapperServiceTest extends FileSystemWrapperService {
    private final Path root = Paths.get("");
    private static final Path existingFile = Paths.get("existingFile"),
            existingDirectory = Paths.get("existingDirectory"),
            renamedFile = Paths.get("renamedFile"),
            candidateToCreate = Paths.get("candidateToCreate");
    @Mock
    private VirtualFileSystemInitializerService virtualFileSystemInitializerService;

    @BeforeEach
    void setUp() throws IOException {
        Mockito.when(virtualFileSystemInitializerService.getRoot()).thenReturn(root.toAbsolutePath());
        setVirtualFileSystemInitializerService(virtualFileSystemInitializerService);
        Files.createFile(existingFile);
        Files.createDirectory(existingDirectory);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(existingFile);
        Files.deleteIfExists(candidateToCreate);
        Files.deleteIfExists(renamedFile);
        Files.deleteIfExists(existingDirectory);
    }

    @Test
    void find() throws IOException {
        Assertions.assertArrayEquals(
                Files.find(root.toAbsolutePath(), Integer.MAX_VALUE, (path, attr) -> true)
                        .map(this::wrap)
                        .filter(path -> !path.equals(root))
                        .toArray(),
                this.find(root, Integer.MAX_VALUE, (path, atte) -> true)
                        .toArray());
    }

    @Test
    void findAll() throws IOException {
        Assertions.assertArrayEquals(
                Files.find(root.toAbsolutePath(), Integer.MAX_VALUE, (path, attr) -> true)
                        .map(this::wrap)
                        .filter(path -> !path.equals(root))
                        .toArray(),
                this.findAll(root, (path, atte) -> true)
                        .toArray());
    }

    @Test
    void createFileProtected_createdObjectExists() throws IOException {
        this.createFileProtected(candidateToCreate);
        Assertions.assertTrue(Files.exists(candidateToCreate));
    }

    @Test
    void createFileProtected_createdObjectIsFile() throws IOException {
        this.createFileProtected(candidateToCreate);
        Assertions.assertTrue(Files.isRegularFile(candidateToCreate));
    }

    @Test
    void createFileProtected_returnWritableStream() throws IOException {
        try (FileChannel fileChannel = this.createFileProtected(candidateToCreate);
             ReadableByteChannel inputStream = Channels.newChannel(new ByteSequence(new byte[]{1, 2, 7}))) {
            fileChannel.transferFrom(inputStream, 0, Long.MAX_VALUE);
        }
        Assertions.assertArrayEquals(new byte[]{1, 2, 7}, Files.readAllBytes(candidateToCreate));
    }

    @Test
    void createFileProtected_butAlreadyExists_throwException() {
        Assertions.assertThrows(FileExistsInDirectoryException.class,
                () -> this.createFileProtected(existingFile));
    }

    @Test
    void createDirectoryProtected_createdObjectExists() {
        this.createDirectoryProtected(candidateToCreate);
        Assertions.assertTrue(Files.exists(candidateToCreate));
    }

    @Test
    void createDirectoryProtected_createdObjectIsDirectory() {
        this.createDirectoryProtected(candidateToCreate);
        Assertions.assertTrue(Files.isDirectory(candidateToCreate));
    }

    @Test
    void createDirectoryProtected_butAlreadyExists_throwException() {
        Assertions.assertThrows(DirectoryExistsInDirectoryException.class,
                () -> this.createDirectoryProtected(existingFile));
    }

    @Test
    void renameFileProtected_oldNameDoesNotExistsAndNewNameExists() {
        this.renameFileProtected(existingFile, renamedFile.getFileName().toString());
        Assertions.assertTrue(!Files.exists(existingFile) && Files.exists(renamedFile));
    }

    @Test
    void renameFileProtected_renamedFileIsFile() {
        this.renameFileProtected(existingFile, renamedFile.getFileName().toString());
        Assertions.assertTrue(Files.isRegularFile(renamedFile));
    }

    @Test
    void renameFileProtected_butRenamedFileAlreadyExists_throwException() {
        Assertions.assertThrows(FileExistsInDirectoryException.class,
                () -> this.renameFileProtected(existingFile, existingDirectory.getFileName().toString()));
    }

    @Test
    void deleteFileProtected_fileDeleted() {
        this.deleteFileProtected(existingFile);
        Assertions.assertFalse(Files.exists(existingFile));
    }

    @Test
    void deleteFileProtected_butItIsNotFile_throwNotAFileException() {
        Assertions.assertThrows(NotAFileException.class, () -> this.deleteFileProtected(existingDirectory));
    }

    @Test
    void getFileProtected() {
        Assertions.assertEquals(existingFile.toAbsolutePath(), this.getFileProtected(existingFile));
    }
}