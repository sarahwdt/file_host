package org.spbstu.file_host.controller;

import lombok.Setter;
import org.spbstu.file_host.dto.UserAuthInfoDto;
import org.spbstu.file_host.dto.abstraction.AbstractDto;
import org.spbstu.file_host.entity.Personal;
import org.spbstu.file_host.entity.UserAuthInfo;
import org.spbstu.file_host.mapper.UserAuthInfoMapper;
import org.spbstu.file_host.service.UserContextService;
import org.spbstu.file_host.service.authority.PreloadedUserRoleHolder;
import org.spbstu.file_host.service.authority.abstraction.Secured;
import org.spbstu.file_host.service.crud.UserAuthInfoCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController implements Secured {
    @Setter(onMethod_ = {@Autowired})
    private UserContextService userContextService;
    @Setter(onMethod_ = {@Autowired})
    private UserAuthInfoMapper mapper;
    @Setter(onMethod_ = {@Autowired})
    private UserAuthInfoCrudService userAuthInfoCrudService;
    @Setter(onMethod_ = {@Autowired})
    private PreloadedUserRoleHolder preloadedUserRoleHolder;

    @GetMapping
    public ResponseEntity<?> getCurrentUser() {
        return userContextService.getCurrentUser()
                .map(mapper::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @PreAuthorize("hasPermission(this, 'new_user')")
    @PostMapping("/registration")
    public ResponseEntity<UserAuthInfoDto> registration(String login, String password) {
        return ResponseEntity.ok(mapper.toDto(userAuthInfoCrudService
                .create(new UserAuthInfo(login, "{noop}" + password, new Personal(), preloadedUserRoleHolder.load()))));
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody UserAuthInfoDto entity) {
        return userContextService.getCurrentUser()
                .map(AbstractPersistable::getId)
                .filter(current -> Optional.of(entity).map(AbstractDto::getId).map(current::equals).orElse(false))
                .map(id -> mapper.toEntity(entity))
                .map(userAuthInfoCrudService::update)
                .map(mapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/update/credential")
    public ResponseEntity<?> update(@RequestParam String oldP, @RequestParam String newP) {
        return userContextService.getCurrentUser()
                .filter(user -> user.getPassword().replaceFirst("\\{noop\\}", "").equals(oldP))
                .map(userAuthInfo -> {
                    userAuthInfo.setPassword("{noop}" + newP);
                    return userAuthInfo;
                }).map(userAuthInfoCrudService::update)
                .map(mapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/authorities")
    public ResponseEntity<Set<String>> getAuthorities(){
        return userContextService.getCurrentUser()
                .map(UserAuthInfo::getAuthorities)
                .map(grantedAuthorities -> grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.ok(Set.of()));
    }

    @Override
    public String getAuthorityPrefix() {
        return "auth";
    }

    @Override
    public Set<String> getActions() {
        return Set.of("new_user", "update_user");
    }
}
