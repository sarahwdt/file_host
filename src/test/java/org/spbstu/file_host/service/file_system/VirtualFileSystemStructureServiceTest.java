package org.spbstu.file_host.service.file_system;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class VirtualFileSystemStructureServiceTest extends VirtualFileSystemStructureService{
    private final List<Path> result = List.of(Path.of("storage"), Path.of("storage_0"), Path.of("storage_2"));
    @Mock
    private FileSystemWrapperService fileSystemWrapperService;

    @BeforeEach
    void setUp() {
        Mockito.when(fileSystemWrapperService.findAll(Mockito.any(), Mockito.any()))
                .thenReturn(result.stream());
    }

    @Test
    void getAllDirectoriesAsStream_equalsResult() {
        this.setFileSystemWrapperService(fileSystemWrapperService);
        assertArrayEquals(result.toArray(), this.getAllDirectoriesAsStream().toArray());
    }
}