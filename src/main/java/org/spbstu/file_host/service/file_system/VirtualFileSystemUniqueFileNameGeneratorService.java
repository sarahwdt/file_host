package org.spbstu.file_host.service.file_system;

import lombok.Setter;
import org.spbstu.file_host.exception.server.file_system.FileSystemException;
import org.spbstu.file_host.util.naming.NamingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
public class VirtualFileSystemUniqueFileNameGeneratorService {
    @Setter(onMethod_ = {@Autowired, @Qualifier("fileNamingStrategy")})
    private NamingStrategy namingStrategy;

    @Setter(onMethod_ = {@Autowired})
    private FileSystemWrapperService fileSystemWrapper;

    public String getUniqueFileName(Path directory, String originalFileName) throws FileSystemException {
        return namingStrategy.apply(fileSystemWrapper
                        .find(directory, 1, (p, a) -> true)
                        .map(Path::getFileName)
                        .map(Path::toString),
                originalFileName);
    }
}
