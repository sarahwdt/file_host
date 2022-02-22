package org.spbstu.file_host.controller;

import org.spbstu.file_host.controller.abstraction.AbstractCrudController;
import org.spbstu.file_host.dto.RoleDto;
import org.spbstu.file_host.entity.Role;
import org.spbstu.file_host.service.authority.abstraction.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/role")
public class RoleController extends AbstractCrudController<Role, RoleDto> implements Secured {

    @Override
    public String getAuthorityPrefix() {
        return "role";
    }
}
