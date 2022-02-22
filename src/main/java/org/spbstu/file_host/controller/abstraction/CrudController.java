package org.spbstu.file_host.controller.abstraction;

import org.spbstu.file_host.dto.abstraction.DtoType;
import org.spbstu.file_host.entity.abstraction.EntityType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1")
public interface CrudController<E extends EntityType, Dto extends DtoType> extends RoController<E, Dto> {
    @PreAuthorize("hasPermission(this, 'create')")
    @PostMapping(value = "")
    ResponseEntity<Dto> create(@RequestBody Dto entity);

    @PreAuthorize("hasPermission(this, 'update')")
    @PutMapping(value = "/{id}")
    ResponseEntity<Dto> update(@PathVariable Long id, @RequestBody Dto entity);

    @PreAuthorize("hasPermission(this, 'delete')")
    @DeleteMapping(value = "/{id}")
    ResponseEntity<?> delete(@PathVariable Long id);
}
