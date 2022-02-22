package org.spbstu.file_host.service.file_storage;

import org.spbstu.file_host.entity.FileInfo;
import org.spbstu.file_host.exception.client.EntityNotFoundInDatabaseException;
import org.spbstu.file_host.service.crud.FileInfoCrudService;
import org.spbstu.file_host.service.file_system.VirtualFileSystemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileStorageServiceTest extends FileStorageService {
    @Mock
    private VirtualFileSystemService virtualFileSystemService;
    @Mock
    private FileInfoCrudService fileInfoCrudService;
    @Mock
    private FileStorageStructureService fileStorageStructureService;

    @BeforeEach
    void setUp() {
        this.setFileStorageStructureService(fileStorageStructureService);
        this.setFileInfoCrudService(fileInfoCrudService);
        this.setVirtualFileSystemService(virtualFileSystemService);
    }

    @Test
    void getFileAsResource_butThrowsEntityNotFindInDatabaseException() {
        when(fileInfoCrudService.get(any())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundInDatabaseException.class, () -> this.getFileAsResource(0));
    }

    @Test
    void getFileAsResource_thenSuccess() throws IOException, EntityNotFoundInDatabaseException {
        Path mockFile = Files.createFile(Path.of("mock"));
        Files.write(mockFile, new byte[]{1, 2, 3});
        when(fileInfoCrudService.get(any())).thenReturn(
                Optional.of(new FileInfo("", "", "", "", null)));
        when(virtualFileSystemService.getFile(any())).thenReturn(mockFile);
        byte[] expectedBytes = Files.readAllBytes(mockFile);
        byte[] actualBytes = Files.readAllBytes(Path.of(this.getFileAsResource(1).getURI()));
        Files.deleteIfExists(mockFile);
        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    void deleteFile_butThrowsEntityNotFindInDatabaseException() {
        when(fileInfoCrudService.get(any())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundInDatabaseException.class, () -> this.deleteFile(0));
    }
}