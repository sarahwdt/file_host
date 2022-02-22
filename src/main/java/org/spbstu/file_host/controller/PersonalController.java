package org.spbstu.file_host.controller;

import org.spbstu.file_host.controller.abstraction.AbstractCrudController;
import org.spbstu.file_host.dto.PersonalDto;
import org.spbstu.file_host.entity.Personal;
import org.spbstu.file_host.service.authority.abstraction.Secured;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/personal")
public class PersonalController extends AbstractCrudController<Personal, PersonalDto> implements Secured {

    @Override
    public String getAuthorityPrefix() {
        return "personal";
    }
}
