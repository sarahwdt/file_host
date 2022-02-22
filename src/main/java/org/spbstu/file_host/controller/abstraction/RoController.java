package org.spbstu.file_host.controller.abstraction;

import org.spbstu.file_host.dto.abstraction.DtoType;
import org.spbstu.file_host.entity.abstraction.EntityType;
import org.spbstu.file_host.service.authority.abstraction.Secured;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@RequestMapping("/api/v1")
public interface RoController<E extends EntityType, Dto extends DtoType> extends Secured {
    @PreAuthorize("permitAll()")
    @GetMapping(value = "")
    ResponseEntity<Page<Dto>> get(Pageable pageable);

    @PreAuthorize("permitAll()")
    @GetMapping(value = "/{id}")
    ResponseEntity<Dto> get(@PathVariable Long id);
}
