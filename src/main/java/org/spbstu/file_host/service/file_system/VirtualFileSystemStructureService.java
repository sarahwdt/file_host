package org.spbstu.file_host.service.file_system;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

@Service
public class VirtualFileSystemStructureService {
    @Setter(onMethod_ = {@Autowired})
    private FileSystemWrapperService fileSystemWrapperService;

    public Stream<Path> getAllDirectoriesAsStream() {
        return fileSystemWrapperService.findAll(Path.of(""), (path, basicFileAttributes) -> Files.isDirectory(path))
                .filter(path -> !Path.of("").equals(path));
    }
}
