package org.spbstu.file_host.service.authority;

import lombok.Setter;
import org.spbstu.file_host.entity.Privilege;
import org.spbstu.file_host.service.authority.abstraction.Secured;
import org.spbstu.file_host.service.crud.PrivilegeCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Set;

@Service
public class PrivilegePreloaderService {
    @Setter(onMethod_ = {@Autowired})
    private PrivilegeCrudService privilegeCrudService;
    @Setter(onMethod_ = {@Autowired})
    private Set<Secured> securedComponents;

    @Transactional
    public void preload() {
        securedComponents.forEach(secured ->
                secured.getActions().stream()
                        .map(action -> (secured.getAuthorityPrefix() + "_" + action).toUpperCase(Locale.ROOT))
                        .forEach(authority -> privilegeCrudService.findByAuthority(authority).orElseGet(() ->
                                privilegeCrudService.create(new Privilege("", "", authority)))));
    }
}
