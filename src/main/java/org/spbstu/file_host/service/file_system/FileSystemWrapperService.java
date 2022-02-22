package org.spbstu.file_host.service.file_system;


import lombok.Setter;
import org.spbstu.file_host.exception.server.file_system.FileSystemException;
import org.spbstu.file_host.exception.server.file_system.*;
import org.spbstu.file_host.util.FileSystemValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

/**
 * Сервис предоставляет контролируемый доступ к файловой системе
 */
@Service
public class FileSystemWrapperService {

    /**
     * Сервис инициализации виртуальной файловой системы.
     * Используется для получения корневой директории, в которой будет развернута виртуальная файловая система
     */
    @Setter(onMethod_ = {@Autowired})
    private VirtualFileSystemInitializerService virtualFileSystemInitializerService;

    /**
     * @return реальный путь к корневой директории виртуальной файловой системы
     */
    protected Path getRoot() {
        return virtualFileSystemInitializerService.getRoot();
    }

    /**
     * Преобразования абсолютного пути к объекту, к относительному пути из корневой директории виртуальной файловой системы
     *
     * @param absolutePathInFileSystem абсолютный путь до файла
     * @return относительный путь до файла в виртуальной файловой системе
     */
    protected Path wrap(Path absolutePathInFileSystem) {
        return Path.of(absolutePathInFileSystem.toString().replaceFirst((getRoot().toString()
                .replace("\\", "\\\\") + "\\\\"), ""));
    }

    /**
     * Преобразование относительного пути в виртуальной файловой системе к абсолютному пути
     *
     * @param pathFromVirtualFileSystemRoot относительный путь до файла в виртуальной файловой системе
     * @return абсолютный путь до файла
     */
    protected Path unwrap(Path pathFromVirtualFileSystemRoot) {
        return getRoot().resolve(pathFromVirtualFileSystemRoot);
    }

    /**
     * Поиск файлов в виртуальной файловой системе по заданным критериям.
     * ОСТОРОЖНО! Отсутствие проверки прав доступа может вызывать FileSystemException
     *
     * @param pathFromVirtualFileSystemRoot путь в виртуальной файловой системе из которого ведется поиск
     * @param maxDepth                      максимальная глубина поиска (количество вложенных каталогов)
     * @param matcher                       {@link java.util.function.BiPredicate} функция предварительной проверки объекта
     * @return стрим из объектов в директории pathFromVirtualFileSystemRoot удовлетворяющих условию matcher с поиском в глубину на maxDepth уровней
     * @throws FileSystemException при возникновении {@link java.io.IOException}
     * @throws NoAccessException   при отсутствии доступа к чтению каталога pathFromVirtualFileSystemRoot
     */
    public Stream<Path> find(Path pathFromVirtualFileSystemRoot, int maxDepth,
                             BiPredicate<Path, BasicFileAttributes> matcher) throws FileSystemException {
        Path absolutePath = unwrap(pathFromVirtualFileSystemRoot);
        try {
            return Files.find(absolutePath, maxDepth, matcher).map(this::wrap)
                    .filter(path -> !path.equals(pathFromVirtualFileSystemRoot));
        } catch (SecurityException e) {
            throw new NoAccessException(pathFromVirtualFileSystemRoot, "Чтение");
        } catch (IOException e) {
            throw new FileSystemException("Ошибка ввода/вывода при попытке доступа к корню виртуальной файловой системе. "
                    + "Причина: " + e.getLocalizedMessage());
        }
    }

    /**
     * Поиск файлов в виртуальной файловой системе по заданным критериям.
     * ОСТОРОЖНО! Отсутствие проверки прав доступа может вызывать FileSystemException
     *
     * @param pathFromVirtualFileSystemRoot путь в виртуальной файловой системе из которого ведется поиск
     * @param matcher                       {@link java.util.function.BiPredicate} функция предварительной проверки объекта
     * @return стрим из объектов в директории pathFromVirtualFileSystemRoot удовлетворяющих условию matcher с поиском в максимальную допустимую глубину
     * @throws FileSystemException при возникновении {@link java.io.IOException}
     * @throws NoAccessException   при отсутствии доступа к чтению каталога pathFromVirtualFileSystemRoot
     */
    public Stream<Path> findAll(Path pathFromVirtualFileSystemRoot,
                                BiPredicate<Path, BasicFileAttributes> matcher) throws FileSystemException {
        return find(pathFromVirtualFileSystemRoot, Integer.MAX_VALUE, matcher);
    }

    /**
     * Создание файла по относительному пути в виртуальной файловой системе
     *
     * @param pathFromVirtualFileSystemRootToNewFile путь, по которому будет расположен файл в виртуальной файловой системе
     * @return Канал для записи данных в файл
     * @throws FileExistsInDirectoryException если файл уже существует
     * @throws NoAccessException              если отсутствуют права на запись
     * @throws FileSystemException            если происходит внутренняя ошибка ввода-вывода
     */
    public FileChannel createFileProtected(Path pathFromVirtualFileSystemRootToNewFile)
            throws FileSystemException {
        Path absolutePath = unwrap(pathFromVirtualFileSystemRootToNewFile);
        try {
            return FileChannel.open(Files.createFile(absolutePath), StandardOpenOption.WRITE);
        } catch (FileAlreadyExistsException e) {
            throw new FileExistsInDirectoryException(absolutePath);
        } catch (SecurityException e) {
            throw new NoAccessException(absolutePath, "Запись");
        } catch (IOException e) {
            try {
                Files.deleteIfExists(absolutePath);
            } catch (IOException ex) {
                throw new FileSystemException("Ошибка ввода/вывода при попытке удаления созданного файла," +
                        " при ошибке создания файла \""
                        + absolutePath + "\"."
                        + "Причина: " + e.getLocalizedMessage());
            }
            throw new FileSystemException("Ошибка ввода/вывода при попытке создания файла \""
                    + absolutePath + "\"."
                    + "Причина: " + e.getLocalizedMessage());
        }
    }

    /**
     * Создание директории по относительному адресу в виртуальной файловой системе
     *
     * @param pathFromVirtualFileSystemRootToNewDirectory относительный путь директории в виртуальной файловой системе
     * @return относительный путь до директории в виртуальной файловой системе
     * @throws DirectoryExistsInDirectoryException если директория уже существует
     * @throws NoAccessException                   если отсутствуют права на запись
     * @throws FileSystemException                 если произошла внутренняя ошибка ввода-вывода
     */
    public Path createDirectoryProtected(Path pathFromVirtualFileSystemRootToNewDirectory) throws FileSystemException {
        Path absolutePath = unwrap(pathFromVirtualFileSystemRootToNewDirectory);
        try {
            Files.createDirectory(absolutePath);
            return pathFromVirtualFileSystemRootToNewDirectory;
        } catch (FileAlreadyExistsException e) {
            throw new DirectoryExistsInDirectoryException(absolutePath);
        } catch (SecurityException e) {
            throw new NoAccessException(absolutePath, "Запись");
        } catch (IOException e) {
            throw new FileSystemException("Ошибка ввода/вывода при попытке создания директории \""
                    + absolutePath + "\"."
                    + "Причина: " + e.getLocalizedMessage());
        }
    }

    /**
     * Изменение имени файла по относительному пути в виртуальной файловой системе
     *
     * @param pathFromVirtualFileSystemRootToFileToCreate относительный путь до файла в виртуальной файловой системе
     * @param newName                                     новое имя файла
     * @return относительный путь до файла с изменённым названием
     * @throws FileExistsInDirectoryException если файл с новым именем уже существует
     * @throws NoAccessException              если у одного из файлов отсутствуют права на запись
     * @throws FileSystemException            если произошла внутренняя ошибка ввода-вывода
     */
    public Path renameFileProtected(Path pathFromVirtualFileSystemRootToFileToCreate, String newName)
            throws FileSystemException {
        Path absolutePath = unwrap(pathFromVirtualFileSystemRootToFileToCreate);
        Path newAbsolutePath = absolutePath.getParent().resolve(newName);
        try {
            return wrap(Files.move(absolutePath, newAbsolutePath));
        } catch (FileAlreadyExistsException e) {
            throw new FileExistsInDirectoryException(newAbsolutePath);
        } catch (SecurityException e) {
            throw new NoAccessException(newAbsolutePath, "Запись");
        } catch (IOException e) {
            throw new FileSystemException("Ошибка ввода/вывода при попытке переименования файла \""
                    + absolutePath + "\"."
                    + "Причина: " + e.getLocalizedMessage());
        }
    }

    /**
     * Удаление файла по относительному пути в виртуальной файловой системе
     *
     * @param pathFromVirtualFileSystemRootToFileToDelete относительный путь до файла в виртуальной файловой системе
     * @throws NoAccessException   при отсутствии прав на удаление файла
     * @throws FileSystemException при попытке удалить директорию или внутренней ошибке ввода-вывода
     */
    public void deleteFileProtected(Path pathFromVirtualFileSystemRootToFileToDelete) throws FileSystemException {
        Path absolutePath = unwrap(pathFromVirtualFileSystemRootToFileToDelete);
        if (Files.exists(absolutePath))
            FileSystemValidator.builder().file().build().validate(absolutePath);
        try {
            Files.deleteIfExists(absolutePath);
        } catch (DirectoryNotEmptyException e) {
            throw new FileSystemException("Попытка удалить директорию. Ожидается файл.");
        } catch (SecurityException e) {
            throw new NoAccessException(absolutePath, "Удаление");
        } catch (IOException e) {
            throw new FileSystemException("Ошибка ввода/вывода при попытке удаления файла \""
                    + absolutePath + "\"."
                    + "Причина: " + e.getLocalizedMessage());
        }
    }

    /**
     * Получить реальный адрес файла по относительному пути в виртуальной файловой системе
     *
     * @param pathFromVirtualFileSystemRootToFile относительный путь до файла в виртуальной файловой системе
     * @return абсолютный путь до файла
     * @throws NoAccessException      отсутствуют права на чтение файла
     * @throws NotAFileException      при попытке получить директорию
     * @throws DoesNotExistsException при отсутствии файла в виртуальной файловой системе
     */
    public Path getFileProtected(Path pathFromVirtualFileSystemRootToFile) throws FileSystemException {
        Path absolutePath = unwrap(pathFromVirtualFileSystemRootToFile);
        FileSystemValidator.builder().file().readable().exists().build().validate(absolutePath);
        return absolutePath;
    }
}
