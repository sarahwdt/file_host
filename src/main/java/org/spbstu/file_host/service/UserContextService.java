package org.spbstu.file_host.service;

import lombok.Setter;
import org.spbstu.file_host.entity.UserAuthInfo;
import org.spbstu.file_host.service.crud.UserAuthInfoCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserContextService {
    @Setter(onMethod_ = {@Autowired})
    private UserAuthInfoCrudService userAuthInfoCrudService;

    public Optional<UserAuthInfo> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication instanceof AnonymousAuthenticationToken ? Optional.empty()
                : userAuthInfoCrudService.findByLogin(authentication.getName());
    }
}
