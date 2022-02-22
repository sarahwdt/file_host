package org.spbstu.file_host.service.file_system;

import lombok.extern.log4j.Log4j2;
import org.spbstu.file_host.exception.server.ConfigurationFatalError;
import org.spbstu.file_host.exception.server.file_system.FileSystemException;
import org.spbstu.file_host.util.FileSystemValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Сервис отвечает за развертывание виртуальной файловой системы
 */
@Log4j2
@Service
@PropertySource("classpath:application.properties")
public class VirtualFileSystemInitializerService {
    private final Path root;

    /**
     * Производит процесс инициализации - проверку корневой директории на то, удовлетворяет ли она условиям:
     * 1 это директория
     * 2 она существует
     * 3 ее можно считывать
     * 4 в нее можно записывать
     *
     * @param directory корневая директория, автоматически подтягивающаяся из проперти
     */
    public VirtualFileSystemInitializerService(@Autowired @Value("${service.filesystem.root}") String directory) {
        root = Paths.get(directory);
        try {
            FileSystemValidator.builder().exists().directory().readable().writable().build().validate(root);
        } catch (FileSystemException e) {
            throw new ConfigurationFatalError("application.properties:directory",
                    root.toAbsolutePath().toString(), e);
        }
    }

    /**
     * @return путь до корневой директории
     */
    public Path getRoot() {
        return root;
    }
}
