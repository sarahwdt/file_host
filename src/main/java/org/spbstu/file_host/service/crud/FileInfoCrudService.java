package org.spbstu.file_host.service.crud;

import lombok.Setter;
import org.spbstu.file_host.entity.FileInfo;
import org.spbstu.file_host.exception.client.EntityNotFoundInDatabaseException;
import org.spbstu.file_host.repository.FileInfoRepository;
import org.spbstu.file_host.service.UserContextService;
import org.spbstu.file_host.service.abstraction.crud.AbstractCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса, выполняющего операции с информацией о файле. Помимо стандартного функционала указывает время
 * операции и пользователя, выполнившего операцию.
 */
@Service
public class FileInfoCrudService extends AbstractCrudService<FileInfo> {

    private final List<Sort.Order> orders = List.of(Sort.Order.desc("createdDate").nullsFirst());
    /**
     * Сервис взаимодействия с текущим контекстом пользователя
     */
    @Setter(onMethod_ = {@Autowired})
    private UserContextService userContextService;

    /**
     * Получения списка сущностей с информацией о файлах, с учетом фильтра по названию файла в формате страницы
     * {@link Page}
     *
     * @param pageable   {@link Pageable} формат страницы
     * @param startsWith фильтр названия
     * @return {@link Page} список сущностей с информацией о файлах в формате страницы
     */
    public Page<FileInfo> getAllStartsWith(Pageable pageable, String startsWith) {
        return ((FileInfoRepository) crudRepository)
                .getAllByFileNameStartsWithAndDeletedFalse(pageable, startsWith);
    }

    @Override
    public FileInfo create(FileInfo entity) {
        entity.setCreatedBy(userContextService.getCurrentUser().orElse(null));
        entity.setCreatedDate(LocalDateTime.now());
        return super.create(entity);
    }

    @Override
    public FileInfo update(FileInfo entity) {
        entity.setLastModifiedBy(userContextService.getCurrentUser().orElse(null));
        entity.setLastModifiedDate(LocalDateTime.now());
        Optional.ofNullable(entity.getId()).map(crudRepository::findByIdAndDeletedFalse)
                .flatMap(fileInfo -> fileInfo)
                .map(fileInfo -> {
                    entity.setMd5(fileInfo.getMd5());
                    entity.setPath(fileInfo.getPath());
                    entity.setCreatedDate(fileInfo.getCreatedDate());
                    entity.setCreatedBy(fileInfo.getCreatedBy());
                    return fileInfo;
                }).orElseThrow(() -> new EntityNotFoundInDatabaseException("Информация о файле", entity.getId()));

        return super.update(entity);
    }

    @Override
    public boolean delete(FileInfo entity) {
        entity.setLastModifiedBy(userContextService.getCurrentUser().orElse(null));
        entity.setLastModifiedDate(LocalDateTime.now());
        return super.delete(entity);
    }

    @Override
    public Page<FileInfo> getAll(Pageable pageable) {
        return super.getAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(orders)));
    }
}
