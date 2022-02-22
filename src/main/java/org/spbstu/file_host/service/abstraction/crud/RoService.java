package org.spbstu.file_host.service.abstraction.crud;

import org.spbstu.file_host.entity.abstraction.EntityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс сервиса, осуществляющего получение сущностей из источника данных
 *
 * @param <E> {@link EntityType} класс сущности
 */
public interface RoService<E extends EntityType> {
    /**
     * Получение сущности используя ее уникальный идентификатор
     *
     * @param id уникальный идентификатор
     * @return {@link Optional} с сущностью
     */
    Optional<E> get(Long id);

    /**
     * Получение списка сущностей в формате {@link Page}
     *
     * @param pageable {@link Pageable} формат страницы
     * @return {@link Page} страница с набором сущностей
     */
    Page<E> getAll(Pageable pageable);

    /**
     * Получение списка всех сущностей в формате {@link Page}
     *
     * @return {@link Page} страница со всеми сущностями
     */
    Page<E> getAllAsOnePage();

    /**
     * Получение списка всех сущностей
     *
     * @return {@link List} со всеми сущностями
     */
    List<E> getAll();
}
