package org.spbstu.file_host.service.authority.abstraction;

import lombok.Setter;
import org.spbstu.file_host.entity.Role;
import org.spbstu.file_host.service.crud.PrivilegeCrudService;
import org.spbstu.file_host.service.crud.RoleCrudService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;

public abstract class AbstractPreloadedRoleHolder
        implements PreloadedEntityHolder<Role> {
    @Setter(onMethod_ = {@Autowired})
    protected RoleCrudService crudService;
    @Setter(onMethod_ = {@Autowired})
    protected PrivilegeCrudService privilegeCrudService;

    @Override
    public Role load() {
        return crudService.findRoleByName(getRoleStringIdentity())
                .orElseGet(() -> {
                    Role toCreate = getInstance();
                    toCreate.setRole(getRoleStringIdentity());
                    return crudService.create(toCreate);
                });
    }

    protected abstract Role getInstance();

    protected abstract String getRoleStringIdentity();

}
