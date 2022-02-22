package org.spbstu.file_host.service.file_system;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.spbstu.file_host.exception.server.ServerSideException;
import org.spbstu.file_host.exception.server.file_system.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VirtualFileSystemServiceTest extends VirtualFileSystemService {
    @Mock
    private FileSystemWrapperService fileSystemWrapperService;
    @Mock
    private VirtualFileSystemUniqueFileNameGeneratorService fileNameResolverService;
    @Mock
    private VirtualFileSystemDirectoryUniqueNameGeneratorService directoryResolverService;

    @BeforeAll
    static void beforeAll() {

    }

    @BeforeEach
    void setUp() {
        this.setFileSystemWrapperService(fileSystemWrapperService);
        this.setFileNameResolverService(fileNameResolverService);
        this.setDirectoryResolverService(directoryResolverService);
    }

    @Test
    void createFile_butFileGeneratorThrowsFileServiceException() {
        when(fileNameResolverService.getUniqueFileName(any(), any())).thenThrow(FileSystemException.class);
        assertThrows(FileSystemException.class, () -> this.createFile(Path.of("whatever"), "whatever",
                null));
    }

    @Test
    void createFile_butFileGeneratorThrowsNoAccessException() {
        when(fileNameResolverService.getUniqueFileName(any(), any())).thenThrow(NoAccessException.class);
        assertThrows(NoAccessException.class, () -> this.createFile(Path.of("whatever"), "whatever",
                null));
    }

    @Test
    void createFile_butDirectoryGeneratorThrowsFileServiceException() {
        when(directoryResolverService.getUniqueDirectoryName(any())).thenThrow(FileSystemException.class);
        assertThrows(FileSystemException.class, () -> this.createFile(null, "whatever",
                null));
    }

    @Test
    void createFile_butDirectoryGeneratorThrowsNoAccessException() {
        when(directoryResolverService.getUniqueDirectoryName(any())).thenThrow(NoAccessException.class);
        assertThrows(NoAccessException.class, () -> this.createFile(null, "whatever",
                null));
    }

    @Test
    void createFile_butFileSystemWrapperThrowsFileExistsInDirectoryException() {
        when(fileNameResolverService.getUniqueFileName(any(), any())).thenReturn("whatever");
        when(fileSystemWrapperService.createFileProtected(any())).thenThrow(FileExistsInDirectoryException.class);
        assertThrows(ServerSideException.class, () -> this.createFile(Path.of("whatever"), "whatever",
                null));
    }

    @Test
    void createFile_butFileSystemWrapperThrowsNoAccessException() {
        when(fileNameResolverService.getUniqueFileName(any(), any())).thenReturn("whatever");
        when(fileSystemWrapperService.createFileProtected(any())).thenThrow(NoAccessException.class);
        assertThrows(ServerSideException.class, () -> this.createFile(Path.of("whatever"), "whatever",
                null));
    }

    @Test
    void createFile_butFileSystemWrapperThrowsFileSystemException() {
        when(fileNameResolverService.getUniqueFileName(any(), any())).thenReturn("whatever");
        when(fileSystemWrapperService.createFileProtected(any())).thenThrow(NoAccessException.class);
        assertThrows(ServerSideException.class, () -> this.createFile(Path.of("whatever"), "whatever",
                null));
    }

    @Test
    void createFile_thenPathEqualsExpected() throws IOException {
        final String directoryName = "storage";
        final String fileName = "fileName";
        final Path directory = Files.createDirectory(Path.of(directoryName));
        final Path file = Files.createFile(Path.of(directoryName, fileName));
        when(fileNameResolverService.getUniqueFileName(any(), any())).thenReturn(fileName);
        when(fileSystemWrapperService.createFileProtected(any()))
                .thenReturn(FileChannel.open(file, StandardOpenOption.WRITE));
        Files.deleteIfExists(file);
        Files.deleteIfExists(directory);
        assertEquals(file, this.createFile(directory, fileName, Channels.newChannel(InputStream.nullInputStream())));
    }

    @Test
    void createFile_thenFileBytesEqualsExpected() throws IOException {
        final String directoryName = "storage";
        final String fileName = "fileName";
        final Path directory = Files.createDirectory(Path.of(directoryName));
        final Path file = Files.createFile(Path.of(directoryName, fileName));
        final byte[] fileData = "some data".getBytes();
        when(fileNameResolverService.getUniqueFileName(any(), any())).thenReturn(fileName);
        when(fileSystemWrapperService.createFileProtected(any()))
                .thenReturn(FileChannel.open(file, StandardOpenOption.WRITE));
        final byte[] resultFileBytes = Files.readAllBytes(this.createFile(directory, fileName,
                Channels.newChannel(new ByteArrayInputStream(fileData))));
        Files.deleteIfExists(file);
        Files.deleteIfExists(directory);
        assertArrayEquals(fileData, resultFileBytes);
    }

    @Test
    void deleteFile_butThrowsNotAFileException() {
        doThrow(NotAFileException.class).when(fileSystemWrapperService).deleteFileProtected(any());
        assertThrows(NotAFileException.class, () -> this.deleteFile(Path.of("")));
    }

    @Test
    void deleteFile_butThrowsFileSystemException() {
        doThrow(FileSystemException.class).when(fileSystemWrapperService).deleteFileProtected(any());
        assertThrows(FileSystemException.class, () -> this.deleteFile(Path.of("")));
    }

    @Test
    void deleteFile_butThrowsNoAccessException() {
        doThrow(NoAccessException.class).when(fileSystemWrapperService).deleteFileProtected(any());
        assertThrows(NoAccessException.class, () -> this.deleteFile(Path.of("")));
    }

    @Test
    void renameFile_butThrowsFileExistsInDirectoryExceptionException() {
        when(fileSystemWrapperService.renameFileProtected(any(), any())).thenThrow(FileExistsInDirectoryException.class);
        assertThrows(FileExistsInDirectoryException.class, () -> this.renameFile(Path.of(""), ""));
    }

    @Test
    void renameFile_butThrowsNoAccessException() {
        when(fileSystemWrapperService.renameFileProtected(any(), any())).thenThrow(NoAccessException.class);
        assertThrows(NoAccessException.class, () -> this.renameFile(Path.of(""), ""));
    }

    @Test
    void renameFile_butThrowsFileSystemException() {
        when(fileSystemWrapperService.renameFileProtected(any(), any())).thenThrow(FileSystemException.class);
        assertThrows(FileSystemException.class, () -> this.renameFile(Path.of(""), ""));
    }

    @Test
    void renameFile_thenSuccess() {
        final Path renamedPath = Path.of("renamedPath");
        when(fileSystemWrapperService.renameFileProtected(any(), any())).thenReturn(renamedPath);
        assertEquals(renamedPath, this.renameFile(Path.of(""), ""));
    }

    @Test
    void getFile_butThrowsNotAFileException() {
        when(fileSystemWrapperService.getFileProtected(any())).thenThrow(NotAFileException.class);
        assertThrows(NotAFileException.class, () -> this.getFile(Path.of("")));
    }

    @Test
    void getFile_butThrowsNoAccessException() {
        when(fileSystemWrapperService.getFileProtected(any())).thenThrow(NoAccessException.class);
        assertThrows(NoAccessException.class, () -> this.getFile(Path.of("")));
    }

    @Test
    void getFile_butThrowsDoesNotExistsException() {
        when(fileSystemWrapperService.getFileProtected(any())).thenThrow(DoesNotExistsException.class);
        assertThrows(DoesNotExistsException.class, () -> this.getFile(Path.of("")));
    }

    @Test
    void getFile_thenSuccess() {
        final Path pathFromWrapper = Path.of("pathFromWrapper");
        when(fileSystemWrapperService.getFileProtected(any())).thenReturn(pathFromWrapper);
        assertEquals(pathFromWrapper, this.getFile(Path.of("")));
    }
}