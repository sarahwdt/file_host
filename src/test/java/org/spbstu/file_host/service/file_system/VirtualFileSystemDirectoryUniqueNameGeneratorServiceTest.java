package org.spbstu.file_host.service.file_system;

import org.spbstu.file_host.util.naming.IndexNumberNamingStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class VirtualFileSystemDirectoryUniqueNameGeneratorServiceTest
        extends VirtualFileSystemDirectoryUniqueNameGeneratorService{
    @Mock
    private FileSystemWrapperService fileSystemWrapperService;

    @BeforeEach
    void setUp() {
        Mockito.when(fileSystemWrapperService.find(Mockito.any(), Mockito.eq(1),  Mockito.any()))
                .thenReturn(Stream.of(Path.of("storage"), Path.of("storage_0"), Path.of("storage_2")));
    }

    @Test
    void getUniqueDirectoryName() {
        this.setFileSystemWrapper(fileSystemWrapperService);
        this.setNamingStrategy(new IndexNumberNamingStrategy());
        Assertions.assertEquals("storage_1", this.getUniqueDirectoryName(Path.of("")));
    }
}