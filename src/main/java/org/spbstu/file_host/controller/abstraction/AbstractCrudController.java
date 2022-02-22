package org.spbstu.file_host.controller.abstraction;

import lombok.Setter;
import org.spbstu.file_host.dto.abstraction.AbstractDto;
import org.spbstu.file_host.entity.abstraction.AbstractEntity;
import org.spbstu.file_host.service.abstraction.crud.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Базовый контроллер для обработки запросов post, put, delete
 *
 * @param <E>   используемая сущность
 * @param <Dto> представление сущности
 */
@RestController
public abstract class AbstractCrudController<E extends AbstractEntity, Dto extends AbstractDto>
        extends AbstractRoController<E, Dto> implements CrudController<E, Dto> {

    @Setter(onMethod_ = {@Autowired})
    protected CrudService<E> crudService;


    @Override
    public ResponseEntity<Dto> create(@RequestBody Dto entity) {
        return ResponseEntity
                .ok(mapper
                        .toDto(crudService
                                .create(mapper
                                        .toEntity(entity))));
    }

    @Override
    public ResponseEntity<Dto> update(@PathVariable Long id, @RequestBody Dto entity) {
        return roService.get(id)
                .map(oldEntity -> mapper
                        .toDto(crudService
                                .update(mapper
                                        .toEntity(entity))))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @Override
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return roService.get(id)
                .map(entity -> crudService
                        .delete(entity))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}