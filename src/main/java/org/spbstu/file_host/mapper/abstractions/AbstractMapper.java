package org.spbstu.file_host.mapper.abstractions;


import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.spbstu.file_host.dto.abstraction.AbstractDto;
import org.spbstu.file_host.entity.abstraction.AbstractEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.util.Objects;

/**
 * Класс маппер из одного класса в другой
 *
 * @param <E>   первый класс (Сущность)
 * @param <Dto> второй класс(дто)
 */
public abstract class AbstractMapper<E extends AbstractEntity, Dto extends AbstractDto> implements Mapper<E, Dto> {

    @SuppressWarnings("unchecked")
    protected final Class<E> entityClass = ((Class<E>) ((ParameterizedType) getClass().getGenericSuperclass())
            .getActualTypeArguments()[0]);
    @SuppressWarnings("unchecked")
    protected final Class<Dto> dtoClass = ((Class<Dto>) ((ParameterizedType) getClass().getGenericSuperclass())
            .getActualTypeArguments()[1]);
    @Setter(onMethod_ = {@Autowired})
    protected ModelMapper mapper;

    @Override
    public E toEntity(Dto dto) {
        return Objects.isNull(dto)
                ? null
                : mapper.map(dto, entityClass);
    }

    @Override
    public Dto toDto(E entity) {
        return Objects.isNull(entity)
                ? null
                : mapper.map(entity, dtoClass);
    }

    public Converter<E, Dto> toDtoConverter() {
        return context -> {
            E source = context.getSource();
            Dto destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }

    public Converter<Dto, E> toEntityConverter() {
        return context -> {
            Dto source = context.getSource();
            E destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }

    protected void mapSpecificFields(E source, Dto destination) {
    }

    protected void mapSpecificFields(Dto source, E destination) {
    }
}
