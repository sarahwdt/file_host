package org.spbstu.file_host.controller;

import org.spbstu.file_host.controller.abstraction.AbstractCrudController;
import org.spbstu.file_host.dto.UserAuthInfoDto;
import org.spbstu.file_host.entity.UserAuthInfo;
import org.spbstu.file_host.service.authority.abstraction.Secured;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserInfoController extends AbstractCrudController<UserAuthInfo, UserAuthInfoDto> implements Secured {
    @PreAuthorize("hasPermission(this, 'read')")
    @Override
    public ResponseEntity<Page<UserAuthInfoDto>> get(Pageable pageable) {
        return super.get(pageable);
    }

    @PreAuthorize("hasPermission(this, 'read')")
    @Override
    public ResponseEntity<UserAuthInfoDto> get(Long id) {
        return super.get(id);
    }

    @PreAuthorize("hasPermission(this, 'create')")
    @Override
    public ResponseEntity<UserAuthInfoDto> create(UserAuthInfoDto entity) {
        return super.create(entity);
    }

    @Override
    public String getAuthorityPrefix() {
        return "user";
    }
}
