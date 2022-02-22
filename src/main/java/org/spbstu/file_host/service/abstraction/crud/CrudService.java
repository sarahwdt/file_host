package org.spbstu.file_host.service.abstraction.crud;

import org.spbstu.file_host.entity.abstraction.EntityType;

/**
 * Интерфейс сервиса, осуществляющего операции с сущностями в источнике данных.
 * Наследует интерфейс сервиса получения данных {@link RoService}
 *
 * @param <E> {@link EntityType} класс сущности
 */
public interface CrudService<E extends EntityType> extends RoService<E> {
    /**
     * Создание сущности
     *
     * @param entity создаваемая сущность
     * @return созданная сущность
     */
    E create(E entity);

    /**
     * Обновление сущности
     *
     * @param entity обновленная сущность
     * @return результат обновления
     */
    E update(E entity);

    /**
     * Удаление сущности
     *
     * @param entity удаляемая сущность
     * @return результат удаления
     */
    boolean delete(E entity);
}
