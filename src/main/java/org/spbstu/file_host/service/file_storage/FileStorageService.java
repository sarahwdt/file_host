package org.spbstu.file_host.service.file_storage;

import lombok.Setter;
import org.spbstu.file_host.entity.FileInfo;
import org.spbstu.file_host.exception.client.EntityNotFoundInDatabaseException;
import org.spbstu.file_host.exception.server.ServerSideException;
import org.spbstu.file_host.service.crud.FileInfoCrudService;
import org.spbstu.file_host.service.file_system.VirtualFileSystemService;
import org.spbstu.file_host.util.ChecksumUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;

/**
 * Сервис определяющий функционал для работы с файловым хранилищем
 */
@Service
public class FileStorageService {
    /**
     * Сервис выполняющий взаимодействие с файловой системой
     */
    @Setter(onMethod_ = {@Autowired})
    private VirtualFileSystemService virtualFileSystemService;
    /**
     * Сервис выполняющий взаимодействие с базой данных
     */
    @Setter(onMethod_ = {@Autowired})
    private FileInfoCrudService fileInfoCrudService;
    /**
     * Сервис предоставляющий информацию о структуре файлового хранилища
     */
    @Setter(onMethod_ = {@Autowired})
    private FileStorageStructureService fileStorageStructureService;

    /**
     * Получение файла в виде ресурса
     *
     * @param id идентификатор файла в базе данных
     * @return файл в формате ресурса
     * @throws EntityNotFoundInDatabaseException при отсутствии файла в базе данных
     * @throws MalformedURLException             при ошибке формирования url
     */
    public Resource getFileAsResource(long id) throws EntityNotFoundInDatabaseException, MalformedURLException {
        return new UrlResource(
                virtualFileSystemService.getFile(
                                Path.of(
                                        fileInfoCrudService.get(id)
                                                .orElseThrow(() -> new EntityNotFoundInDatabaseException("Файл", id))
                                                .getPath()))
                        .toUri());
    }

    /**
     * Сохранение файла в системе
     *
     * @param file файл
     * @return информация о файле, записанная в систему
     */
    public FileInfo storeFile(FileInfo entity, MultipartFile file) {
        try (InputStream inputStream = file.getInputStream();
             ReadableByteChannel channel = Channels.newChannel(inputStream)) {
            Path pathToFile = virtualFileSystemService.createFile(
                    fileStorageStructureService.getAvailableDirectories().stream().findFirst().orElse(null),
                    file.getOriginalFilename(),
                    channel);
            return fileInfoCrudService.create(new FileInfo(entity.getFileName(), entity.getDescription(),
                    pathToFile.toString(), DatatypeConverter.printHexBinary(ChecksumUtil.getChecksum(inputStream)),
                    entity.getDepartment()));
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new ServerSideException();
        }
    }

    /**
     * Удаление файла
     *
     * @param id идентификатор файла в базе данных
     * @return результат операции
     * @throws EntityNotFoundInDatabaseException при отсутствии файла в базе данных
     */
    public Boolean deleteFile(long id) throws EntityNotFoundInDatabaseException {
        return fileInfoCrudService.get(id).map(fileInfo -> {
            virtualFileSystemService.deleteFile(Path.of(fileInfo.getPath()));
            return fileInfoCrudService.delete(fileInfo);
        }).orElseThrow(() -> new EntityNotFoundInDatabaseException("Файл", id));
    }
}
