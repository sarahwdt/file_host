package org.spbstu.file_host.controller;

import org.spbstu.file_host.controller.abstraction.AbstractCrudController;
import org.spbstu.file_host.dto.PrivilegeDto;
import org.spbstu.file_host.entity.Privilege;
import org.spbstu.file_host.service.authority.abstraction.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/privilege")
public class PrivilegeController extends AbstractCrudController<Privilege, PrivilegeDto> implements Secured {

    @Override
    public String getAuthorityPrefix() {
        return "privilege";
    }
}
