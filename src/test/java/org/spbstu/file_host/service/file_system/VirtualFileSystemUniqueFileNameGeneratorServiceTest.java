package org.spbstu.file_host.service.file_system;

import org.spbstu.file_host.util.naming.IndexNumberNamingStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class VirtualFileSystemUniqueFileNameGeneratorServiceTest extends VirtualFileSystemUniqueFileNameGeneratorService{
    @Mock
    private FileSystemWrapperService fileSystemWrapperService;

    @BeforeEach
    void setUp() {
        Mockito.when(fileSystemWrapperService.find(Mockito.any(), Mockito.eq(1),  Mockito.any()))
                .thenReturn(Stream.of(Path.of("storage"), Path.of("storage_0"), Path.of("storage_2")));
    }

    @Test
    void getUniqueFileName_storage_1Equals() {
        this.setFileSystemWrapper(fileSystemWrapperService);
        this.setNamingStrategy(new IndexNumberNamingStrategy());
        assertEquals("storage_1", this.getUniqueFileName(Path.of(""), "storage"));
    }

    @Test
    void getUniqueFileName_randomEquals() {
        this.setFileSystemWrapper(fileSystemWrapperService);
        this.setNamingStrategy(new IndexNumberNamingStrategy());
        assertEquals("random", this.getUniqueFileName(Path.of(""), "random"));
    }
}