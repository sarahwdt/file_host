package org.spbstu.file_host.controller.abstraction;


import lombok.Setter;
import org.spbstu.file_host.dto.abstraction.AbstractDto;
import org.spbstu.file_host.entity.abstraction.AbstractEntity;
import org.spbstu.file_host.mapper.abstractions.Mapper;
import org.spbstu.file_host.service.abstraction.crud.RoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Базовый контроллер обработки запросов get
 *
 * @param <E>   используемая сущность
 * @param <Dto> представление сущности
 */
@RestController
public abstract class AbstractRoController<E extends AbstractEntity, Dto extends AbstractDto>
        implements RoController<E, Dto> {

    @Setter(onMethod_ = {@Autowired})
    protected RoService<E> roService;
    @Setter(onMethod_ = {@Autowired})
    protected Mapper<E, Dto> mapper;

    @Override
    public ResponseEntity<Page<Dto>> get(Pageable pageable) {
        return ResponseEntity.ok(roService.getAll(pageable).map(mapper::toDto));
    }

    @Override
    public ResponseEntity<Dto> get(@PathVariable Long id) {
        return roService.get(id).map(mapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
