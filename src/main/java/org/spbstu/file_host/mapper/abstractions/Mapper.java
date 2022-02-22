package org.spbstu.file_host.mapper.abstractions;


import org.spbstu.file_host.dto.abstraction.AbstractDto;
import org.spbstu.file_host.entity.abstraction.AbstractEntity;

/**
 * Интерфейс для использования маппера
 *
 * @param <E>      сущность
 * @param <Dto>дто
 */
public interface Mapper<E extends AbstractEntity, Dto extends AbstractDto> {
    E toEntity(Dto dto);

    Dto toDto(E entity);
}