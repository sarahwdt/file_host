package org.spbstu.file_host.service.abstraction.crud;

import org.spbstu.file_host.entity.abstraction.AbstractEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Частичная реализация сервиса операций с сущностями в источнике данных. Наследует частичную реализацию сервиса
 * взаимодействия с данными {@link AbstractRoService}
 *
 * @param <E> {@link AbstractEntity} класс сущности
 */
@Service
public abstract class AbstractCrudService<E extends AbstractEntity> extends AbstractRoService<E>
        implements CrudService<E> {

    public E create(E entity) {
        return crudRepository.save(entity);
    }

    public E update(E entity) {
        return crudRepository.save(entity);
    }

    public boolean delete(E entity) {
        entity.setDeleted(true);
        crudRepository.save(entity);
        return crudRepository.existsByIdAndDeletedFalse(Objects.requireNonNull(entity.getId()));
    }
}
