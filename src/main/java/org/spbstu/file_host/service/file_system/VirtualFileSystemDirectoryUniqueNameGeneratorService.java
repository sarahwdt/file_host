package org.spbstu.file_host.service.file_system;

import lombok.Setter;
import org.spbstu.file_host.exception.server.file_system.FileSystemException;
import org.spbstu.file_host.exception.server.file_system.NoAccessException;
import org.spbstu.file_host.util.naming.NamingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

/**
 * Сервис генерации уникального имени директории
 */
@Service
public class VirtualFileSystemDirectoryUniqueNameGeneratorService {
    /**
     * Стратегия генерации уникального имени
     */
    @Setter(onMethod_ = {@Autowired, @Qualifier("directoryNamingStrategy")})
    private NamingStrategy namingStrategy;

    /**
     * Сервис, предоставляющий методы роботы с файловой системой
     */
    @Setter(onMethod_ = {@Autowired})
    private FileSystemWrapperService fileSystemWrapper;

    /**
     * Генерация уникального имени директории
     *
     * @param path путь, по которому будет находиться директория
     * @return уникальное имя
     * @throws FileSystemException при возникновении {@link java.io.IOException}
     * @throws NoAccessException   при отсутствии доступа к чтению каталога pathFromVirtualFileSystemRoot
     */
    public String getUniqueDirectoryName(Path path) throws FileSystemException {
        return namingStrategy.apply(fileSystemWrapper.find(path, 1, (p, attrs) -> true)
                        .map(Path::getFileName)
                        .map(Path::toString),
                "storage");
    }

}
