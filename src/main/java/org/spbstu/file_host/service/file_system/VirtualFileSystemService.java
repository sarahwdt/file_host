package org.spbstu.file_host.service.file_system;

import lombok.Setter;
import org.spbstu.file_host.exception.server.ServerSideException;
import org.spbstu.file_host.exception.server.file_system.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class VirtualFileSystemService {
    @Setter(onMethod_ = {@Autowired})
    private FileSystemWrapperService fileSystemWrapperService;

    @Setter(onMethod_ = {@Autowired})
    private VirtualFileSystemDirectoryUniqueNameGeneratorService directoryResolverService;
    @Setter(onMethod_ = {@Autowired})
    private VirtualFileSystemUniqueFileNameGeneratorService fileNameResolverService;

    /**
     * Создание файла
     *
     * @param pathToDirectory зануляемая переменная, путь до директории в которой будет создан файл. Если директория не передана,
     *                        автоматически создает директорию
     * @param fileName        имя файла
     * @param inputChannel    канал с данными для файла
     * @return путь до файла из корня виртуальной файловой системы
     * @throws FileSystemException TODO: перечислить исключения
     */
    public Path createFile(Path pathToDirectory, String fileName, ReadableByteChannel inputChannel)
            throws FileSystemException {
        if (pathToDirectory == null) pathToDirectory = fileSystemWrapperService.createDirectoryProtected(Path.of("")
                .resolve(directoryResolverService.getUniqueDirectoryName(Path.of(""))));

        Path pathToFile = pathToDirectory.resolve(fileNameResolverService.getUniqueFileName(pathToDirectory, fileName));
        try (FileChannel outputChannel = fileSystemWrapperService.createFileProtected(pathToFile)) {
            outputChannel.transferFrom(inputChannel, 0, Long.MAX_VALUE);
        } catch (Exception e) {
            deleteFile(pathToFile);
            throw new ServerSideException("Критическая ошибка сервера!" + e.getLocalizedMessage(), e);
        }
        return pathToFile;
    }

    /**
     * Удаление файла
     *
     * @param pathToFile путь до файла из корня виртуальной файловой системы
     * @throws NoAccessException   при отсутствии прав на удаление файла
     * @throws FileSystemException при попытке удалить директорию или внутренней ошибке ввода-вывода
     */
    public void deleteFile(Path pathToFile) throws FileSystemException {
        fileSystemWrapperService.deleteFileProtected(pathToFile);
    }

    /**
     * Изменение имени файла
     *
     * @param pathToFile путь до файла из корня виртуальной файловой системы
     * @param newName    новое имя файла
     * @return путь до файла из виртуальной файловой системы
     * @throws FileExistsInDirectoryException если файл с новым именем уже существует
     * @throws NoAccessException              если у одного из файлов отсутствуют права на запись
     * @throws FileSystemException            если произошла внутренняя ошибка ввода-вывода
     */
    public Path renameFile(Path pathToFile, String newName) throws FileSystemException {
        return fileSystemWrapperService.renameFileProtected(pathToFile, newName);
    }

    /**
     * Получение реального пути файла в файловой системе
     *
     * @param pathToFile путь до файла из корня виртуальной файловой системы
     * @return рейльный путь до файла
     * @throws NoAccessException      отсутствуют права на чтение файла
     * @throws NotAFileException      при попытке получить директорию
     * @throws DoesNotExistsException при отсутствии файла в виртуальной файловой системе
     */
    public Path getFile(Path pathToFile) throws FileSystemException {
        return fileSystemWrapperService.getFileProtected(pathToFile);
    }

    public boolean checkFile(Path pathToFile) {
        try {
            return Files.exists(fileSystemWrapperService.getFileProtected(pathToFile));
        } catch (FileSystemException e) {
            return false;
        }
    }


}
